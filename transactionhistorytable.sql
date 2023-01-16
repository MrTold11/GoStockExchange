CREATE TABLE IF NOT EXISTS transaction_history (
	sellerid      int,
	buyerid int,
	transactionid bigint,
	transaction_time timestamp,
    GTB_cost         double precision  ,   	   -- GoToBles cost 
    stock_type         char(16),           			   -- exchanged stock type
	stock_amount  int    
	);
	
CREATE UNIQUE INDEX transactionid ON transaction_history (transactionid);
CREATE INDEX sellerid ON transaction_history (sellerid);
CREATE INDEX buyerid ON transaction_history (buyerid);
