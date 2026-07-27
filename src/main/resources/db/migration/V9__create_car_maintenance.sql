CREATE TABLE IF NOT EXISTS car_maintenance (
    id UUID PRIMARY KEY,

    car_id UUID NOT NULL,
    user_id UUID,

    title VARCHAR(255),
    description VARCHAR(1200),

    type VARCHAR(50),
    status VARCHAR(50),

    appointment_date DATE,
    appointment_time TIME,

    odometer INTEGER,
    estimated_cost NUMERIC(12,2),

    workshop VARCHAR(255),
    notes TEXT,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_car_maintenance_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_maintenance_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_car_maintenance_car
    ON car_maintenance(car_id);

CREATE INDEX IF NOT EXISTS idx_car_maintenance_user
    ON car_maintenance(user_id);

