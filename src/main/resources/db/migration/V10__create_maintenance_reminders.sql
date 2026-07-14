CREATE TABLE IF NOT EXISTS maintenance_reminders (
    id UUID PRIMARY KEY,
    car_id UUID NOT NULL,
--    user_id UUID,
    title VARCHAR(255),
    description VARCHAR(1200),
    type VARCHAR(50),
    due_date DATE,
    status VARCHAR(50),
    appointment_date DATE,
    appointment_time TIME,
    odometer INTEGER,
    estimated_cost NUMERIC(12,2),
    workshop VARCHAR(255),
    notes TEXT,
    notification_sent BOOLEAN DEFAULT FALSE,
    notification_date DATE,
    notification_sent_at TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_maintenance_car
        FOREIGN KEY (car_id)
        REFERENCES cars(id)
        ON DELETE CASCADE

--    CONSTRAINT fk_maintenance_user
--        FOREIGN KEY (user_id)
--        REFERENCES users(id)
--        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_maintenance_car
    ON maintenance_reminders(car_id);

--CREATE INDEX IF NOT EXISTS idx_maintenance_user
--    ON maintenance_reminders(user_id);

CREATE INDEX IF NOT EXISTS idx_maintenance_due_date
    ON maintenance_reminders(due_date);

CREATE INDEX IF NOT EXISTS idx_maintenance_notification
    ON maintenance_reminders(notification_sent, notification_date);
