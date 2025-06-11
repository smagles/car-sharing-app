UPDATE rentals
SET is_active = false
WHERE actual_return_date IS NOT NULL;
