CREATE TABLE IF NOT EXISTS maintenance_notification_history (
    id UUID PRIMARY KEY,
    maintenance_reminder_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL,
    provider VARCHAR(100),
    provider_message_id VARCHAR(255),
    error_message TEXT,
    attempted_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_maintenance_notification_history
        FOREIGN KEY (maintenance_reminder_id)
        REFERENCES maintenance_reminders (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_maintenance_history_car_maintenance
    ON maintenance_notification_history(maintenance_reminder_id);

