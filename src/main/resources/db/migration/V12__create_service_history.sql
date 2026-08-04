CREATE TABLE IF NOT EXISTS service_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    car_id UUID NOT NULL,
    service_type VARCHAR(255),
    description TEXT,
    cost NUMERIC(12,2),
    service_date VARCHAR(255),
    next_service_date VARCHAR(255),
    mechanic VARCHAR(255),
    workshop VARCHAR(255),
    odometer VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_service_history_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_service_history_car
    ON service_history(car_id);
