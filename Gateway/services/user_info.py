# Frontend serving app

from concurrent import futures
import grpc
import FE_pb2
import FE_pb2_grpc
import psycopg2
import asyncio

DB_NAME = "postgres"
DB_USER = "postgres"
DB_PASSWORD = "12345678"
DB_HOST = "192.168.1.122"
DB_PORT = "5433"


class AsyncFEServicer(FE_pb2_grpc.FEServiceServicer):
    def __init__(self, cur) -> None:
        super().__init__()
        self.opening_prices = dict()
        self.current_prices = dict()
        self.issued_tokens = dict()
        self.cur = cur
        self.columns = []

        self.update_columns()

    def get_user_id(self, token):
        if token in self.issued_tokens:
            return self.issued_tokens[token]
        return token # will be removed when autorization done
        # return None 
    
    def update_columns(self):
        self.cur.execute("""
            SELECT column_name
            FROM information_schema.columns
            WHERE table_name = 'portfolios' AND (ordinal_position > 2 AND ordinal_position % 2 = 0 OR ordinal_position = 3)
        """)
        result = self.cur.fetchall()
        columns = [res[0] for res in result]
        self.columns = columns + [columns[col] + "bc" for col in range(1, len(columns))]

    async def GetStocksList(self, request, context):
        stocks_list = []
        stock = FE_pb2.Stock(
                isin="RUtest123456", 
                ticker="TEST", 
                full_name="TestStockNotForTrading", 
                about="Our first transmitted stock"
                )
        stocks_list.append(stock)
        return FE_pb2.StocksList(stocks=stocks_list)
    
    async def GetCurrentPrice(self, request, context):
        if request in self.current_prices:
            return self.current_prices[request] # updates when passing back to client OrderStatus
        
        cur_price = 0 # grpc request to dispatcher to get price of latest transaction involving stocks with given isin
        self.current_prices[request] = cur_price
        return cur_price
        
    async def GetOpeningPrice(self, request, context):
        if request in self.opening_prices:
            return self.opening_prices[request] 
        
        opn_price = 0 # make SQL query to transactions history
        self.opening_prices[request] = opn_price
        return opn_price

    async def GetPortfolio(self, request, context):
        columns = ", ".join(self.columns) # semi-static
        user_id = self.get_user_id(request.token)

        if user_id is not None:
            isin_amount = (len(self.columns) - 1) // 2 # semi-static

            self.cur.execute(f"SELECT {columns} FROM portfolios WHERE userid = %s", user_id)
            result = self.cur.fetchone()
            
            assets = [FE_pb2.Asset(isin=self.columns[0], amount=1, price=result[0])]
            for ind in range(1, isin_amount):
                assets.append(FE_pb2.Asset(isin=self.columns[ind], amount=result[ind], price=result[ind + isin_amount]))
            
            portfolio = FE_pb2.Portfolio(assets=assets)
            
            return portfolio
        
        return FE_pb2.Portfolio() # wrong token

    async def PlaceOrder(self, request, context):
        pass # redirect PlaceOrder to dispatcher via grpc

    async def CancelOrder(self, request, context):
        user_id = self.get_user_id(request.token)
        
        if user_id is not None:
            pass # redirect CancelOrder to dispatcher via grpc to check if user with user_id has an order with request.order_id


async def serve():
    conn = psycopg2.connect(dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT)
    cur = conn.cursor()

    server = grpc.aio.server(futures.ThreadPoolExecutor(max_workers=10))
    FE_pb2_grpc.add_FEServiceServicer_to_server(AsyncFEServicer(cur=cur), server)
    server.add_insecure_port('[::]:50051')
    
    await server.start()
    await server.wait_for_termination()
    
    cur.close()
    conn.close()


if __name__ == "__main__":
    loop = asyncio.get_event_loop()
    loop.run_until_complete(serve())