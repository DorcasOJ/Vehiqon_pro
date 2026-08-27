
CREATE TABLE IF NOT EXISTS cars (
    id UUID PRIMARY KEY,
    nickname VARCHAR(255),
    vin VARCHAR(255) NOT NULL UNIQUE,
    plate_number VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(255),
    year INTEGER,
    engine_number VARCHAR(255) UNIQUE,
    fuel_type VARCHAR(50),
    transmission VARCHAR(50),
    odometer BIGINT,
    purchase_date DATE,
    license_expiry DATE,
    status VARCHAR(50),
    user_id UUID NOT NULL,
    car_brand_id UUID NOT NULL,
    car_model_id UUID NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by UUID,

    CONSTRAINT fk_car_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_car_brand
        FOREIGN KEY (car_brand_id)
        REFERENCES car_brands(id),

    CONSTRAINT fk_car_model
        FOREIGN KEY (car_model_id)
        REFERENCES car_models(id)

);

CREATE INDEX idx_cars_id ON cars(id);
CREATE INDEX idx_cars_nickname_deleted_at ON cars(nickname, deleted_at);
CREATE INDEX idx_cars_vin_deleted_at ON cars(vin, deleted_at);
CREATE INDEX idx_cars_plate_deleted_at ON cars(plate_number, deleted_at);
CREATE INDEX idx_cars_brand_id ON cars(car_brand_id);
CREATE INDEX idx_cars_model_id ON cars(car_model_id);
