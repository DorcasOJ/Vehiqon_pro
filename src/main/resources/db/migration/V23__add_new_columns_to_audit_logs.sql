
ALTER TABLE audit_logs
    ADD COLUMN user_agent VARCHAR(255),
    ADD COLUMN status VARCHAR(255),
    ADD COLUMN description VARCHAR(255);
