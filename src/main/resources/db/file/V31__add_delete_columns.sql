
-- ==========================
-- USERS
-- ==========================

ALTER TABLE users
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN deleted_at TIMESTAMP,
    ADD COLUMN deleted_by UUID;

DROP INDEX IF EXISTS idx_users_email;
CREATE INDEX idx_users_email_deleted_at ON users(email, deleted_at);

-- ==========================
-- CARS
-- ==========================

ALTER TABLE cars
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN deleted_at TIMESTAMP,
    ADD COLUMN deleted_by UUID;

DROP INDEX IF EXISTS idx_cars_nickname;
DROP INDEX IF EXISTS idx_cars_vin;
DROP INDEX IF EXISTS idx_cars_plate;

CREATE INDEX idx_cars_nickname_deleted_at ON cars(nickname, deleted_at);
CREATE INDEX idx_cars_vin_deleted_at ON cars(vin, deleted_at);
CREATE INDEX idx_cars_plate_deleted_at ON cars(plate_number, deleted_at);

-- ==========================
-- CAR MAINTENANCE
-- ==========================

ALTER TABLE car_maintenance
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN deleted_at TIMESTAMP,
    ADD COLUMN deleted_by UUID;

DROP INDEX IF EXISTS idx_car_maintenance_car;
DROP INDEX IF EXISTS idx_car_maintenance_user;

CREATE INDEX idx_car_maintenance_car_deleted_at
    ON car_maintenance(car_id, deleted_at);
CREATE INDEX idx_car_maintenance_user_deleted_at
    ON car_maintenance(user_id, deleted_at);


-- ==========================
-- MAINTENANCE REMINDER
-- ==========================

ALTER TABLE maintenance_reminders
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN deleted_at TIMESTAMP,
    ADD COLUMN deleted_by UUID;


DROP INDEX IF EXISTS idx_maintenance_reminder_schedule;
DROP INDEX IF EXISTS idx_maintenance_reminder_maintenance;

CREATE INDEX idx_maintenance_reminders_schedule_deleted_at
    ON maintenance_reminders(notification_status, scheduled_at, deleted_at);
CREATE INDEX idx_maintenance_reminders_car_maintenance_deleted_at
    ON maintenance_reminders(car_maintenance_id, deleted_at);


-- ==========================
-- MAINTENANCE HISTORY
-- ==========================

ALTER TABLE maintenance_notification_history
    ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN deleted_at TIMESTAMP,
    ADD COLUMN deleted_by UUID;


DROP INDEX IF EXISTS idx_maintenance_history_car_maintenance;
CREATE INDEX idx_maintenance_notification_history_reminder_deleted_at
    ON maintenance_notification_history(maintenance_reminder_id, deleted_at);
