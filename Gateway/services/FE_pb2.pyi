from google.protobuf.internal import containers as _containers
from google.protobuf.internal import enum_type_wrapper as _enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor
LIMIT: OrderType
MARKET: OrderType
UNSPECIFIED: OrderType

class Asset(_message.Message):
    __slots__ = ["amount", "isin", "price"]
    AMOUNT_FIELD_NUMBER: _ClassVar[int]
    ISIN_FIELD_NUMBER: _ClassVar[int]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    amount: int
    isin: str
    price: float
    def __init__(self, isin: _Optional[str] = ..., amount: _Optional[int] = ..., price: _Optional[float] = ...) -> None: ...

class CancelOrderRequest(_message.Message):
    __slots__ = ["order_id", "token"]
    ORDER_ID_FIELD_NUMBER: _ClassVar[int]
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    order_id: int
    token: str
    def __init__(self, token: _Optional[str] = ..., order_id: _Optional[int] = ...) -> None: ...

class Empty(_message.Message):
    __slots__ = []
    def __init__(self) -> None: ...

class ISIN(_message.Message):
    __slots__ = ["isin"]
    ISIN_FIELD_NUMBER: _ClassVar[int]
    isin: str
    def __init__(self, isin: _Optional[str] = ...) -> None: ...

class Order(_message.Message):
    __slots__ = ["asset", "is_buy", "token", "type"]
    ASSET_FIELD_NUMBER: _ClassVar[int]
    IS_BUY_FIELD_NUMBER: _ClassVar[int]
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    TYPE_FIELD_NUMBER: _ClassVar[int]
    asset: Asset
    is_buy: bool
    token: str
    type: OrderType
    def __init__(self, token: _Optional[str] = ..., asset: _Optional[_Union[Asset, _Mapping]] = ..., type: _Optional[_Union[OrderType, str]] = ..., is_buy: bool = ...) -> None: ...

class Portfolio(_message.Message):
    __slots__ = ["assets"]
    ASSETS_FIELD_NUMBER: _ClassVar[int]
    assets: _containers.RepeatedCompositeFieldContainer[Asset]
    def __init__(self, assets: _Optional[_Iterable[_Union[Asset, _Mapping]]] = ...) -> None: ...

class Stock(_message.Message):
    __slots__ = ["about", "full_name", "isin", "ticker"]
    ABOUT_FIELD_NUMBER: _ClassVar[int]
    FULL_NAME_FIELD_NUMBER: _ClassVar[int]
    ISIN_FIELD_NUMBER: _ClassVar[int]
    TICKER_FIELD_NUMBER: _ClassVar[int]
    about: str
    full_name: str
    isin: str
    ticker: str
    def __init__(self, isin: _Optional[str] = ..., ticker: _Optional[str] = ..., full_name: _Optional[str] = ..., about: _Optional[str] = ...) -> None: ...

class StockPrice(_message.Message):
    __slots__ = ["price"]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    price: float
    def __init__(self, price: _Optional[float] = ...) -> None: ...

class StocksList(_message.Message):
    __slots__ = ["stocks"]
    STOCKS_FIELD_NUMBER: _ClassVar[int]
    stocks: _containers.RepeatedCompositeFieldContainer[Stock]
    def __init__(self, stocks: _Optional[_Iterable[_Union[Stock, _Mapping]]] = ...) -> None: ...

class Token(_message.Message):
    __slots__ = ["token"]
    TOKEN_FIELD_NUMBER: _ClassVar[int]
    token: str
    def __init__(self, token: _Optional[str] = ...) -> None: ...

class OrderType(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = []
