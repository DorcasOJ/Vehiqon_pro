CREATE TABLE IF NOT EXISTS maintenance_notification_history (
    id UUID PRIMARY KEY,
    maintenance_reminder_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL,
    provider VARCHAR(100),
    provider_message_id VARCHAR(255),
    error_message TEXT,
    attempted_at TIMESTAMP NOT NULL,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_maintenance_notification_history
        FOREIGN KEY (maintenance_reminder_id)
        REFERENCES maintenance_reminders (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_maintenance_notification_history_reminder_deleted_at
    ON maintenance_notification_history(maintenance_reminder_id, deleted_at)