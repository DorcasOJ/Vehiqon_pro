
CREATE TABLE IF NOT EXISTS subscription_plans (
    id UUID PRIMARY KEY,
--     DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price NUMERIC(18,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(10) NOT NULL DEFAULT 'NGN',
    billing_cycle VARCHAR(30) NOT NULL,
    max_cars INTEGER NOT NULL,
    maintenance_reminders BOOLEAN DEFAULT TRUE,
    priority_support BOOLEAN DEFAULT FALSE,
    roadside_assistance BOOLEAN DEFAULT FALSE,
    analytics BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP

--    CONSTRAINT fk_subscription_plan_parent
--        FOREIGN KEY (subscription_plan_id)
--        REFERENCES subscription_plans(id)
--        ON DELETE SET NULL,

--    CONSTRAINT fk_subscription_plan_user
--        FOREIGN KEY (user_id)
--        REFERENCES users(id)
--        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_subscription_plan_name
ON subscription_plans(name);

CREATE INDEX IF NOT EXISTS idx_subscription_plan_active
ON subscription_plans(active);