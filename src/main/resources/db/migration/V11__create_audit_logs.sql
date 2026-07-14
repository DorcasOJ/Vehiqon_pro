CREATE TABLE IF NOT EXISTS audit_logs (
     id UUID PRIMARY KEY,
--     DEFAULT gen_random_uuid(),
    action VARCHAR(255) NOT NULL,
    entity VARCHAR(255) NOT NULL,
    entity_id VARCHAR(255),
    ip_address VARCHAR(255),

    user_id UUID,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_audit_log_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_audit_logs_user
    ON audit_logs(user_id);
