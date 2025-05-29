CREATE TABLE cars
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    model     VARCHAR(255)   NOT NULL,
    brand     VARCHAR(255)   NOT NULL,
    car_type  VARCHAR(20)    NOT NULL DEFAULT 'SEDAN',
    inventory INT            NOT NULL DEFAULT 0,
    daily_fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE

);
