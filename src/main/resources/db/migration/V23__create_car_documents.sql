CREATE TABLE IF NOT EXISTS car_documents (
    id UUID PRIMARY KEY,
    car_id UUID NOT NULL,

    document_type VARCHAR(80),
    document_name VARCHAR(80),
    original_file_name VARCHAR(255),
    storage_path TEXT,
    content_type VARCHAR(80),
    file_size BIGINT,

    document_number VARCHAR(255),
    issuer VARCHAR(255),
    document_status VARCHAR(80),

    verification_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    verified_by UUID,
    verified_at TIMESTAMP,

    rejected_by UUID,
    rejection_reason VARCHAR(255),
    rejected_at TIMESTAMP,

    issued_at TIMESTAMP,
    expires_at TIMESTAMP,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by UUID,

    CONSTRAINT fk_document_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_document_deleted_by
        FOREIGN KEY (deleted_by)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_document_verified_by
        FOREIGN KEY (verified_by)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_document_rejected_by
            FOREIGN KEY (rejected_by)
            REFERENCES users(id)
            ON DELETE CASCADE
);
CREATE INDEX IF NOT EXISTS idx_documents_car
    ON car_documents(id);

CREATE INDEX IF NOT EXISTS idx_documents_car
    ON car_documents(car_id);

CREATE INDEX IF NOT EXISTS idx_documents_expires_at
    ON car_documents(expires_at);

CREATE INDEX IF NOT EXISTS idx_documents_document_type
    ON car_documents(document_type);

CREATE INDEX IF NOT EXISTS idx_documents_verification_status
    ON car_documents(verification_status);
