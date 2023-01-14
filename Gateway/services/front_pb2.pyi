import base_pb2 as _base_pb2
from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class Portfolio(_message.Message):
    __slots__ = ["assets"]
    ASSETS_FIELD_NUMBER: _ClassVar[int]
    assets: _containers.RepeatedCompositeFieldContainer[_base_pb2.Asset]
    def __init__(self, assets: _Optional[_Iterable[_Union[_base_pb2.Asset, _Mapping]]] = ...) -> None: ...

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
