DROP TABLE IF EXISTS transactions;
CREATE TABLE transactions (
    "timestamp" VARCHAR2(21),
    product_name VARCHAR2(100),
    expiry_date VARCHAR2(10),
    quantity INTEGER,
    unit_price NUMBER,
    channel VARCHAR2(50),
    payment_method VARCHAR2(50),
    discount NUMBER,
    final_price_after_discount NUMBER
);
