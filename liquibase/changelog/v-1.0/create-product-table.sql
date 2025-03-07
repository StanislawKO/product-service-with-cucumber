CREATE SEQUENCE product_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS product
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('product_sequence'),
    summary     VARCHAR(100)  NOT NULL,
    description VARCHAR(100)  NOT NULL,
    price       NUMERIC(5, 2) NOT NULL,
    duration    SMALLINT      NOT NULL,
    discount_id BIGINT        NOT NULL,
    active      BOOLEAN       NOT NULL,
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL,
    FOREIGN KEY (discount_id) REFERENCES discount (id)
);
