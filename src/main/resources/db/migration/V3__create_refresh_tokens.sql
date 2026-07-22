CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
--    session_id VARCHAR(512) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    device_name VARCHAR(255),
    device_id VARCHAR(255),
    ip_address VARCHAR(255),
    expires_at TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_refresh_token_user
    ON refresh_tokens(user_id);

CREATE UNIQUE INDEX IF NOT EXISTS idx_refresh_token_token
    ON refresh_tokens(token);

CREATE INDEX IF NOT EXISTS idx_refresh_token_revoked
    ON refresh_tokens(revoked);

CREATE INDEX IF NOT EXISTS idx_refresh_token_expired
    ON refresh_tokens(expired);

CREATE INDEX IF NOT EXISTS idx_refresh_token_expires_at
    ON refresh_tokens(expires_at);

CREATE INDEX IF NOT EXISTS idx_refresh_token_user_active
    ON refresh_tokens(user_id, revoked, expired);

