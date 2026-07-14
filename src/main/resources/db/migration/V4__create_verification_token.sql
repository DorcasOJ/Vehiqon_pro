
CREATE TABLE IF NOT EXISTS verification_tokens (
    id UUID PRIMARY KEY,
    token VARCHAR(512) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    used_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_verification_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_verification_token_user
    ON verification_tokens(user_id);

CREATE INDEX IF NOT EXISTS idx_verification_token_token
    ON verification_tokens(token);

CREATE INDEX IF NOT EXISTS idx_verification_token_type
    ON verification_tokens(type);

CREATE INDEX IF NOT EXISTS idx_verification_token_used
    ON verification_tokens(used);

CREATE INDEX IF NOT EXISTS idx_verification_token_expires
    ON verification_tokens(expires_at);
