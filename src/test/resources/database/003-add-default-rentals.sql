INSERT INTO rentals (rental_date, return_date, car_id, user_id, is_active)
VALUES (CURRENT_DATE, DATEADD('DAY',  5, CURRENT_DATE), 2, 1, true);