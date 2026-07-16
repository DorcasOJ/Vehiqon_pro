CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id UUID PRIMARY KEY,
    token TEXT NOT NULL,
    user_id UUID NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_user_password_reset_token_user
           FOREIGN KEY (user_id)
           REFERENCES users(id)
           ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_password_reset_token_user
ON password_reset_tokens(user_id);