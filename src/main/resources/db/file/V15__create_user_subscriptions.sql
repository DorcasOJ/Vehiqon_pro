CREATE TABLE IF NOT EXISTS user_subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    subscription_plan_id UUID NOT NULL,
--    start_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    renewal_type VARCHAR(30),
    payment_reference VARCHAR(255),
    status VARCHAR(50) NOT NULL,

    started_at TIMESTAMP,
    current_period_start DATE,
    current_period_end DATE,
    cancelled_at TIMESTAMP,

    auto_renew BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_user_subscription_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_subscription_plan
        FOREIGN KEY (subscription_plan_id)
        REFERENCES subscription_plans(id)
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_user_subscription_user
ON user_subscriptions(user_id);

CREATE INDEX IF NOT EXISTS idx_user_subscription_plan
ON user_subscriptions(subscription_plan_id);

CREATE INDEX IF NOT EXISTS idx_user_subscription_status
ON user_subscriptions(status);
