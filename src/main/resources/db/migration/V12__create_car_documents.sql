CREATE TABLE IF NOT EXISTS car_documents (
    id UUID PRIMARY KEY,
--     DEFAULT gen_random_uuid(),
    car_id UUID NOT NULL,
    document_type VARCHAR(50),
    file_url TEXT,
    expiry_date DATE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_document_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_documents_car
    ON car_documents(car_id);

CREATE INDEX IF NOT EXISTS idx_documents_expiry
    ON car_documents(expiry_date);
