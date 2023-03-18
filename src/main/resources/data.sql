INSERT INTO status (status_id, name)
    VALUES (0, 'WAITING'),
    (1, 'APPROVED'),
    (2, 'REJECTED'),
    (3, 'CANCELED')
ON CONFLICT (status_id) DO NOTHING;