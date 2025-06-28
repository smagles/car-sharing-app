CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_url VARCHAR(512),
    session_id VARCHAR(255),
    amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    type VARCHAR(20),
    rental_id BIGINT NOT NULL,
    FOREIGN KEY (rental_id) REFERENCES rentals(id)
)
