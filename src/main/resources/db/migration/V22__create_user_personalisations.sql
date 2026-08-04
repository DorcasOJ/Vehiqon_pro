CREATE TABLE user_personalisation
(
--    id UUID PRIMARY KEY,
--    favourite_feature VARCHAR(50),
--    favourite_car_id UUID,
--    favourite_mechanic_id UUID,
--    preferred_notification_hour INTEGER,
    user_id UUID PRIMARY KEY,
    feature_weights JSONB DEFAULT '{}'::jsonb,
    preferred_login_time TIME,
    preferred_login_day VARCHAR(20),
    favourite_maintenance_type VARCHAR(80),
    preferred_payment_method VARCHAR(50),
    average_session_minutes INT DEFAULT 0,
    likes_push_notifications BOOLEAN DEFAULT TRUE,
    likes_email_notifications BOOLEAN DEFAULT TRUE,
    reminder_lead_hours INT DEFAULT 24,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_user_personalisation_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE

);
CREATE INDEX idx_user_personalisation_user_id ON user_personalisation(user_id);




