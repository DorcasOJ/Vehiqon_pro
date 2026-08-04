INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Corolla',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Camry',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Hilux',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Land Cruiser',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Prado',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Fortuner',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'RAV4',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Highlander',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Avalon',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';

INSERT INTO car_models(id,name,car_brand_id,created_at,updated_at)
SELECT gen_random_uuid(),'Yaris',id,NOW(),NOW() FROM car_brands WHERE name='Toyota';



-- Honda
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Civic', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Accord', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CR-V', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'HR-V', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Pilot', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Odyssey', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Fit', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'City', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Ridgeline', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Passport', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Honda';


-- Nissan
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Altima', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Maxima', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Sentra', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Versa', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Rogue', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Pathfinder', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Murano', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Patrol', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Frontier', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'X-Trail', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Nissan';


-- Mazda
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Mazda2', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Mazda3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Mazda6', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CX-3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CX-5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CX-9', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CX-30', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'BT-50', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mazda';


-- Mitsubishi
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Lancer', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Outlander', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Pajero', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Montero', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'ASX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Eclipse Cross', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Triton', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mitsubishi';


-- Suzuki
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Swift', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Baleno', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Vitara', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Jimny', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'S-Cross', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Ertiga', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Celerio', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Suzuki';


-- Hyundai
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Elantra', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Sonata', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Tucson', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Santa Fe', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Kona', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Venue', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Accent', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Creta', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Palisade', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'i10', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Hyundai';


-- Kia
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Rio', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Cerato', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Sportage', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Sorento', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Seltos', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Telluride', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Carnival', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Picanto', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'K5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Soul', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Kia';


-- Ford
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Ranger', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Everest', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Escape', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Explorer', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'F-150', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Edge', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Focus', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Fusion', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Mustang', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Expedition', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Ford';


-- Chevrolet
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Malibu', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Cruze', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Tahoe', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Suburban', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Silverado', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Equinox', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Traverse', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Colorado', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Chevrolet';


-- BMW
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '1 Series', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '2 Series', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '3 Series', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '5 Series', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '7 Series', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'X1', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'X3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'X5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'X7', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'M3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'M5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'BMW';


-- Mercedes-Benz
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A-Class', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'C-Class', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'E-Class', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'S-Class', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'GLA', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'GLC', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'GLE', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'GLS', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'G-Class', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'CLA', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Mercedes-Benz';


-- Audi
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A4', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A6', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'A8', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q5', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q7', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q8', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'TT', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Audi';


-- Volkswagen
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Golf', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Passat', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Tiguan', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Touareg', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Polo', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Jetta', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Atlas', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Arteon', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volkswagen';


-- Lexus
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'ES350', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'IS250', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'IS300', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'RX350', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'RX450h', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'GX460', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'LX570', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'NX300', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'UX250h', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'LS500', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Lexus';


-- Acura
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'MDX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Acura';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'RDX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Acura';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'TLX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Acura';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'ILX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Acura';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'NSX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Acura';


-- Infiniti
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q50', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Infiniti';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Q60', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Infiniti';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'QX50', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Infiniti';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'QX60', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Infiniti';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'QX80', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Infiniti';


-- Land Rover
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Defender', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Discovery', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Discovery Sport', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Range Rover', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Range Rover Sport', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Range Rover Evoque', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Range Rover Velar', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Land Rover';


-- Jeep
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Wrangler', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Cherokee', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Grand Cherokee', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Compass', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Gladiator', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Renegade', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Jeep';


-- Volvo
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'XC40', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'XC60', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'XC90', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'S60', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'S90', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'V60', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Volvo';


-- Peugeot
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '208', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Peugeot';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '308', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Peugeot';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '3008', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Peugeot';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '5008', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Peugeot';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), '508', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Peugeot';


-- Renault
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Clio', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Renault';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Megane', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Renault';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Captur', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Renault';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Koleos', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Renault';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Duster', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Renault';


-- Subaru
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Impreza', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Legacy', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Outback', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Forester', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'WRX', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'BRZ', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Crosstrek', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Subaru';


-- Tesla
INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Model 3', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Tesla';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Model S', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Tesla';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Model X', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Tesla';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Model Y', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Tesla';

INSERT INTO car_models (id, name, car_brand_id, created_at, updated_at)
SELECT gen_random_uuid(), 'Cybertruck', id, NOW(), NOW()
FROM car_brands
WHERE name = 'Tesla';

