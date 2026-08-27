CREATE TABLE IF NOT EXISTS maintenance_reminders (
    id UUID PRIMARY KEY,
    car_maintenance_id UUID NOT NULL,

    reminder_name VARCHAR(255),
    scheduled_at TIMESTAMP NOT NULL,
    notification_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    queued_at TIMESTAMP,
    sent_at TIMESTAMP,
    failed_at TIMESTAMP,

    failure_reason VARCHAR(1000),
    notification_channel VARCHAR(30) NOT NULL DEFAULT 'EMAIL',
    attempt_count INTEGER NOT NULL DEFAULT 0,

    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by UUID,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_reminder_maintenance_car_maintenance
        FOREIGN KEY (car_maintenance_id)
        REFERENCES car_maintenance(id)
        ON DELETE CASCADE

);

CREATE INDEX idx_maintenance_reminders_schedule_deleted_at
    ON maintenance_reminders(notification_status, scheduled_at, deleted_at);
CREATE INDEX idx_maintenance_reminders_car_maintenance_deleted_at
    ON maintenance_reminders(car_maintenance_id, deleted_at);