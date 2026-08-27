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

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by UUID,
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

CREATE INDEX idx_car_maintenance_car_deleted_at
    ON car_maintenance(car_id, deleted_at);
CREATE INDEX idx_car_maintenance_user_deleted_at
    ON car_maintenance(user_id, deleted_at);
