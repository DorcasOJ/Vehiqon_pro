CREATE TABLE user_statistics
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    total_sessions BIGINT,

    total_events BIGINT,

    total_time_spent BIGINT,

    total_maintenance_cost BIGINT,

    maintenance_count BIGINT,

    total_payments BIGINT,

    payment_count BIGINT,

    activity_score DOUBLE PRECISION,

    engagement_score DOUBLE PRECISION,

    vehicle_health_score DOUBLE PRECISION,

    last_active TIMESTAMP,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

  CONSTRAINT fk_user_statistic_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE

);