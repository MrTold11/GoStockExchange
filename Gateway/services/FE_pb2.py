# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: FE.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x08\x46\x45.proto\"\x07\n\x05\x45mpty\"$\n\nStocksList\x12\x16\n\x06stocks\x18\x01 \x03(\x0b\x32\x06.Stock\"G\n\x05Stock\x12\x0c\n\x04ISIN\x18\x01 \x01(\t\x12\x0e\n\x06ticker\x18\x02 \x01(\t\x12\x11\n\tfull_name\x18\x03 \x01(\t\x12\r\n\x05\x61\x62out\x18\x04 \x01(\t\"\x14\n\x04ISIN\x12\x0c\n\x04ISIN\x18\x01 \x01(\t\"\x1b\n\nStockPrice\x12\r\n\x05price\x18\x01 \x01(\x01\"\x16\n\x05Token\x12\r\n\x05token\x18\x01 \x01(\t\"#\n\tPortfolio\x12\x16\n\x06\x61ssets\x18\x01 \x03(\x0b\x32\x06.Asset\"4\n\x05\x41sset\x12\x0c\n\x04ISIN\x18\x01 \x01(\t\x12\x0e\n\x06\x61mount\x18\x02 \x01(\x05\x12\r\n\x05price\x18\x03 \x01(\x01\"W\n\x05Order\x12\r\n\x05token\x18\x01 \x01(\t\x12\x15\n\x05\x61sset\x18\x02 \x01(\x0b\x32\x06.Asset\x12\x18\n\x04type\x18\x03 \x01(\x0e\x32\n.OrderType\x12\x0e\n\x06is_buy\x18\x04 \x01(\x08\"5\n\x12\x43\x61ncelOrderRequest\x12\r\n\x05token\x18\x01 \x01(\t\x12\x10\n\x08order_id\x18\x02 \x01(\x04*3\n\tOrderType\x12\x0f\n\x0bUNSPECIFIED\x10\x00\x12\n\n\x06MARKET\x10\x01\x12\t\n\x05LIMIT\x10\x02\x32\xf9\x01\n\tFEService\x12&\n\rGetStocksList\x12\x06.Empty\x1a\x0b.StocksList\"\x00\x12\'\n\x0fGetCurrentPrice\x12\x05.ISIN\x1a\x0b.StockPrice\"\x00\x12\'\n\x0fGetOpeningPrice\x12\x05.ISIN\x1a\x0b.StockPrice\"\x00\x12$\n\x0cGetPortfolio\x12\x06.Token\x1a\n.Portfolio\"\x00\x12\x1e\n\nPlaceOrder\x12\x06.Order\x1a\x06.Empty\"\x00\x12,\n\x0b\x43\x61ncelOrder\x12\x13.CancelOrderRequest\x1a\x06.Empty\"\x00\x62\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'FE_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  _ORDERTYPE._serialized_start=442
  _ORDERTYPE._serialized_end=493
  _EMPTY._serialized_start=12
  _EMPTY._serialized_end=19
  _STOCKSLIST._serialized_start=21
  _STOCKSLIST._serialized_end=57
  _STOCK._serialized_start=59
  _STOCK._serialized_end=130
  _ISIN._serialized_start=132
  _ISIN._serialized_end=152
  _STOCKPRICE._serialized_start=154
  _STOCKPRICE._serialized_end=181
  _TOKEN._serialized_start=183
  _TOKEN._serialized_end=205
  _PORTFOLIO._serialized_start=207
  _PORTFOLIO._serialized_end=242
  _ASSET._serialized_start=244
  _ASSET._serialized_end=296
  _ORDER._serialized_start=298
  _ORDER._serialized_end=385
  _CANCELORDERREQUEST._serialized_start=387
  _CANCELORDERREQUEST._serialized_end=440
  _FESERVICE._serialized_start=496
  _FESERVICE._serialized_end=745
# @@protoc_insertion_point(module_scope)
