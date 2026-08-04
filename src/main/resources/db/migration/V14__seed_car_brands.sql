

INSERT INTO car_brands (id, name, created_at, updated_at)
VALUES
(gen_random_uuid(), 'Toyota', NOW(), NOW()),
(gen_random_uuid(), 'Honda', NOW(), NOW()),
(gen_random_uuid(), 'Nissan', NOW(), NOW()),
(gen_random_uuid(), 'Mazda', NOW(), NOW()),
(gen_random_uuid(), 'Mitsubishi', NOW(), NOW()),
(gen_random_uuid(), 'Suzuki', NOW(), NOW()),
(gen_random_uuid(), 'Hyundai', NOW(), NOW()),
(gen_random_uuid(), 'Kia', NOW(), NOW()),
(gen_random_uuid(), 'Ford', NOW(), NOW()),
(gen_random_uuid(), 'Chevrolet', NOW(), NOW()),
(gen_random_uuid(), 'BMW', NOW(), NOW()),
(gen_random_uuid(), 'Mercedes-Benz', NOW(), NOW()),
(gen_random_uuid(), 'Audi', NOW(), NOW()),
(gen_random_uuid(), 'Volkswagen', NOW(), NOW()),
(gen_random_uuid(), 'Lexus', NOW(), NOW()),
(gen_random_uuid(), 'Acura', NOW(), NOW()),
(gen_random_uuid(), 'Infiniti', NOW(), NOW()),
(gen_random_uuid(), 'Land Rover', NOW(), NOW()),
(gen_random_uuid(), 'Jeep', NOW(), NOW()),
(gen_random_uuid(), 'Volvo', NOW(), NOW()),
(gen_random_uuid(), 'Peugeot', NOW(), NOW()),
(gen_random_uuid(), 'Renault', NOW(), NOW()),
(gen_random_uuid(), 'Subaru', NOW(), NOW()),
(gen_random_uuid(), 'Tesla', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;