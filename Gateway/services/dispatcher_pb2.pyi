import base_pb2 as _base_pb2
from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class OrderStatus(_message.Message):
    __slots__ = ["asset", "is_buy", "order_id", "token", "transactions"]
    ASSET_FIELD_NUMBER: _ClassVar[int]
    IS_BUY_FIELD_NUMBER: _ClassVar[int]
    ORDER_ID_FIELD_NUMBER: _ClassVar[int]
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    TRANSACTIONS_FIELD_NUMBER: _ClassVar[int]
    asset: _base_pb2.Asset
    is_buy: bool
    order_id: int
    token: str
    transactions: _containers.RepeatedCompositeFieldContainer[Transaction]
    def __init__(self, token: _Optional[str] = ..., asset: _Optional[_Union[_base_pb2.Asset, _Mapping]] = ..., is_buy: bool = ..., order_id: _Optional[int] = ..., transactions: _Optional[_Iterable[_Union[Transaction, _Mapping]]] = ...) -> None: ...

class Transaction(_message.Message):
    __slots__ = ["amount", "price"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    amount: int
    price: float
    def __init__(self, price: _Optional[float] = ..., amount: _Optional[int] = ...) -> None: ...
