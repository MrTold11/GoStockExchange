# Frontend serving app

from concurrent import futures
import grpc
import base_pb2
import front_pb2, front_pb2_grpc
import dispatcher_pb2_grpc
import psycopg2
import asyncio

GATEWAY_PORT = "50051"

DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "12345678"
DB_HOST = "192.168.1.122"
DB_PORT = "5433"

DISPATCHER_HOST = "192.168.1.147"
DISPATCHER_PORT = "50150"

class AsyncFrontServicer(front_pb2_grpc.FrontServiceServicer):
    def __init__(self, cur, stub:dispatcher_pb2_grpc.DispatcherGatewayStub) -> None:
        super().__init__()
        self.opening_prices = dict()
        self.current_prices = dict()
        self.issued_tokens = dict()
        self.cur = cur
        self.stub = stub
        self.db_columns = []

        self.update_db_columns()

    def get_user_id(self, token):
        if token in self.issued_tokens:
            return self.issued_tokens[token]
        return token # will be removed when autorization done
        # return None 
    
    def update_db_columns(self):
        self.cur.execute("""
            SELECT column_name
            FROM information_schema.columns
            WHERE table_name = 'portfolios' AND (ordinal_position > 2 AND ordinal_position % 2 = 0 OR ordinal_position = 3)
        """)
        result = self.cur.fetchall()
        columns = [res[0] for res in result]
        self.db_columns = columns + [columns[col] + "bc" for col in range(1, len(columns))]

    async def GetStocksList(self, request, context): # TODO replace hard-coded test stock with request to DB
        stocks_list = []
        stock = front_pb2.Stock(
                isin="RUtest123456", 
                ticker="TEST", 
                full_name="TestStockNotForTrading", 
                about="Our first transmitted stock"
                )
        stocks_list.append(stock)
        return front_pb2.StocksList(stocks=stocks_list)
    
    async def GetCurrentPrice(self, request, context):
        isin = request.isin
        if isin in self.current_prices:
            return base_pb2.StockPrice(price=self.current_prices[isin]) # updates when passing back to client OrderStatus
        
        cur_price = self.stub.GetCurrentPrice(request) # grpc request to dispatcher to get price of latest transaction involving stocks with given isin
        self.current_prices[isin] = cur_price.price # storing price instead of cur_price because StockPrice isn't hashable
        return cur_price
        
    async def GetOpeningPrice(self, request, context):
        isin = request.isin
        if isin in self.opening_prices:
            return self.opening_prices[isin] 
        
        opn_price = self.stub.GetOpeningPrice(request) # grpc dispatcher request (may do an SQL query to transactions history)
        self.opening_prices[isin] = opn_price.price
        return opn_price

    async def GetPortfolio(self, request, context):
        columns = ", ".join(self.db_columns) # semi-static
        user_id = self.get_user_id(request.token)

        if user_id is not None:
            isin_amount = (len(self.db_columns) - 1) // 2 # semi-static

            self.cur.execute(f"SELECT {columns} FROM portfolios WHERE userid = %s", user_id)
            result = self.cur.fetchone()
            
            assets = [base_pb2.Asset(isin=self.db_columns[0], amount=1, price=result[0])]
            for ind in range(1, isin_amount):
                assets.append(base_pb2.Asset(isin=self.db_columns[ind], amount=result[ind], price=result[ind + isin_amount]))
            
            portfolio = front_pb2.Portfolio(assets=assets)
            
            return portfolio
        
        return front_pb2.Portfolio() # wrong token

    async def PlaceOrder(self, request, context):
        self.stub.PlaceOrder(request) # redirect PlaceOrder to dispatcher via grpc
        return base_pb2.Empty()
    
    async def CancelOrder(self, request, context):
        user_id = self.get_user_id(request.token)
        
        if user_id is not None:
            self.stub.CancelOrder(request) # redirect CancelOrder to dispatcher (TODO replace token in request with uid)
            return base_pb2.Empty()

    async def SendOrderStatus(self, request, context):
        print("Recieved OrderStatus from dispatcher: ", request)
        return base_pb2.Empty()

async def serve_front():
    conn = psycopg2.connect(dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT)
    cur = conn.cursor()

    channel = grpc.insecure_channel(f"{DISPATCHER_HOST}:{DISPATCHER_PORT}")
    stub = dispatcher_pb2_grpc.DispatcherGatewayStub(channel)

    server = grpc.aio.server(futures.ThreadPoolExecutor(max_workers=10))
    front_pb2_grpc.add_FrontServiceServicer_to_server(AsyncFrontServicer(cur=cur, stub=stub), server)
    server.add_insecure_port(f"[::]:{GATEWAY_PORT}")
    
    await server.start()
    await server.wait_for_termination()
    
    cur.close()
    conn.close()


if __name__ == "__main__":
    loop = asyncio.get_event_loop()
    try:
        loop.run_until_complete(serve_front())
    finally:
        loop.close()
