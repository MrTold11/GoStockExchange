# Frontend serving app

from concurrent import futures
import grpc
import FE_pb2
import FE_pb2_grpc


class FEServicer(FE_pb2_grpc.FEServiceServicer):
    def __init__(self) -> None:
        super().__init__()
        self.opening_prices = dict()
        self.current_prices = dict()
        self.issued_tokens = dict()

    def get_user_id(self, token):
        if token in self.issued_tokens:
            return self.issued_tokens[token]
        return None

    def GetStocksList(self, request, context):
        stocks_list = []
        stock = FE_pb2.Stock(
                ISIN="RUtest123456", 
                ticker="TEST", 
                full_name="TestStockNotForTrading", 
                about="Our first transmitted stock"
                )
        stocks_list.append(stock)
        return FE_pb2.StocksList(stocks=stocks_list)
    
    def GetCurrentPrice(self, request, context):
        if request in self.current_prices:
            return self.current_prices[request] # updates when passing back to client OrderStatus
        
        cur_price = 0 # grpc request to dispatcher to get price of latest transaction involving stocks with given isin
        self.current_prices[request] = cur_price
        return cur_price
        
    def GetOpeningPrice(self, request, context):
        if request in self.opening_prices:
            return self.opening_prices[request] 
        
        opn_price = 0 # make SQL query to transactions history
        self.opening_prices[request] = opn_price
        return opn_price

    def GetPortfolio(self, request, context):
        user_id = self.get_user_id(request)
        if user_id is not None:
            portfolio = [] # make SQL query to Users DB to get user's portfolio
            return portfolio
        return [] # wrong token

    def PlaceOrder(self, request, context):
        pass # redirect PlaceOrder to dispatcher via grpc

    def CancelOrder(self, request, context):
        user_id = self.get_user_id(request.token)
        if user_id is not None:
            pass # make SQL query to Oreders DB to check if user with user_id has an order with request.order_id


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    FE_pb2_grpc.add_FEServiceServicer_to_server(FEServicer(), server)
    server.add_insecure_port('[::]:50051')
    
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()