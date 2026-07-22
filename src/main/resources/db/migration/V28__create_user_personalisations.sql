CREATE TABLE user_personalisation
(
--    id UUID PRIMARY KEY,

    user_id UUID PRIMARY KEY,

    favourite_feature VARCHAR(50),

    favourite_car_id UUID,

    favourite_mechanic_id UUID,

    preferred_notification_hour INTEGER,

    preferred_channel VARCHAR(20),

    prefers_dark_mode BOOLEAN,

    onboarding_completed BOOLEAN,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    CONSTRAINT fk_user_personalisation_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE

);