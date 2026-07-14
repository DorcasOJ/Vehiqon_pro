
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference VARCHAR(255),
    amount NUMERIC(12,2),
    currency VARCHAR(20),
    provider VARCHAR(255),
    payment_date TIMESTAMP,
    status VARCHAR(50),
    user_subscription_id UUID NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (user_subscription_id)
        REFERENCES user_subscriptions(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_payments_reference
    ON payments(reference);

