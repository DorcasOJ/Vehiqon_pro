CREATE TABLE IF NOT EXISTS virtual_accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    account_reference VARCHAR(255) NOT NULL UNIQUE,
    account_name VARCHAR(255) NOT NULL,
    bank_name VARCHAR(255),
    currency VARCHAR(20),
    account_holder_id VARCHAR(255),
    expired BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_virtual_account_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_virtual_account_user
    ON virtual_accounts(user_id);

CREATE INDEX IF NOT EXISTS idx_virtual_account_number
    ON virtual_accounts(account_number);

CREATE INDEX IF NOT EXISTS idx_virtual_account_reference
    ON virtual_accounts(account_reference);

CREATE INDEX IF NOT EXISTS idx_virtual_account_holder
    ON virtual_accounts(account_holder_id);

