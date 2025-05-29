CREATE TABLE rentals
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    rental_date        DATE    NOT NULL,
    return_date        DATE    NOT NULL,
    actual_return_date DATE,
    user_id            BIGINT  NOT NULL,
    car_id             BIGINT  NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (car_id) REFERENCES cars (id)

)