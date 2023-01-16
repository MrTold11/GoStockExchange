CREATE TABLE IF NOT EXISTS portfolios  (
	userid      serial,
	LCT        	bigint default 0,                     -- Last closed transaction 
    GTB         double precision default 0,   	   -- GoToBles
    RU0000000001         int default 0,	-- GoTo Stock Exchange
	RU0000000001BC         double precision default 1,
    RU0000000002         int default 0,-- Philosophical VK
    RU0000000002BC         double precision default 1,
	RU0000000003         int default 0,          			   -- SVinoFerma
	RU0000000003BC         double precision default 1, 
	US0378331005		int default 0,
	US0378331005BC		double precision default 1,
	US88160R1014		int default 0,
	US88160R1014BC		double precision default 1,
	US0231351067		int default 0,
	US0231351067BC		double precision default 1,
	US5949181045		int default 0,
	US5949181045BC		double precision default 1,
	US67066G1040		int default 0,
	US67066G1040BC		double precision default 1
);

CREATE UNIQUE INDEX userid ON portfolios (userid);


