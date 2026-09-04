
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
--    DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    user_subscription_id UUID NOT NULL,
    provider VARCHAR(255),
    provider_transaction_id VARCHAR(2000),
    order_reference VAR(500),
    reference VARCHAR(255),
    amount NUMERIC(12,2),
    currency VARCHAR(20),

    status VARCHAR(50),
    payment_method VARCHAR(100) NOT NULL,
    paid_at TIMESTAMP,

--    user_subscription_id UUID NOT NULL UNIQUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (user_subscription_id)
        REFERENCES user_subscriptions(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_payments_reference
    ON payments(reference);

