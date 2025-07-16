INSERT INTO cars (model, brand, car_type, inventory, daily_fee, is_deleted)
VALUES
    ('Model 3', 'Tesla', 'SEDAN', 5, 89.99, false),
    ('CX-5', 'Mazda', 'SUV', 7, 59.50, false),
    ('Golf', 'Volkswagen', 'HATCHBACK', 10, 45.00, false),
    ('Passat', 'Volkswagen', 'UNIVERSAL', 3, 65.25, false);

INSERT INTO users (first_name, last_name, email, password, role, is_deleted)
VALUES
    ('Alice', 'Johnson', 'alice@example.com', 'test123', 'CUSTOMER', false),
    ('Bob', 'Smith', 'bob@example.com', 'test123', 'CUSTOMER', false),
    ('Charlie', 'Admin', 'charlie@example.com', 'admin123', 'MANAGER', false);

INSERT INTO rentals (rental_date, return_date, car_id, user_id, is_active)
VALUES (CURRENT_DATE, DATEADD('DAY',  5, CURRENT_DATE), 2, 1, true);

INSERT INTO payments (amount, rental_id, session_id, session_url, status, type)
VALUES (150.00,
        1,
        'cs_test_sampleSessionId123',
        'https://checkout.stripe.com/c/pay/cs_test_sampleSessionUrl1234567890...',
        'PENDING',
        'PAYMENT');
