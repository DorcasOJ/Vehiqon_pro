
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY,
--     DEFAULT gen_random_uuid(),

    title VARCHAR(255),
    message TEXT,
    type VARCHAR(100),
    status VARCHAR(100),
    sent_at VARCHAR(255),

    user_id UUID NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_notifications_user
    ON notifications(user_id);
