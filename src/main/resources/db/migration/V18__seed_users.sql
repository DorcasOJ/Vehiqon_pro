INSERT INTO users (
    id,
    first_name,
    last_name,
    email,
    bvn,
    phone_number,
    password,
    status,
    is_verified,
    created_at,
    updated_at
)
SELECT
    gen_random_uuid(),
    'System',
    'Administrator',
    'admin@vehiqon.com',
    '12345678901',
    '01000000000',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ACTIVE',
    TRUE,
    NOW(),
    NOW()
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE email = 'admin@vehiqon.com'
);
--password Admin@123


INSERT INTO user_roles (
    user_id,
    role
)
SELECT id, 'ROLE_ADMIN'
FROM users WHERE email='admin@vehiqon.com'
ON CONFLICT DO NOTHING;


