CREATE TABLE user_sessions
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    login_at TIMESTAMP NOT NULL,

    logout_at TIMESTAMP,

    active BOOLEAN DEFAULT TRUE,

    last_activity_at TIMESTAMP,

    duration_seconds BIGINT,

    device VARCHAR(100),

    browser VARCHAR(100),

    platform VARCHAR(100),

    operating_system VARCHAR(100),

    app_version VARCHAR(30),

    device_id VARCHAR(150),

    ip_address VARCHAR(50),

    city VARCHAR(100),

    country VARCHAR(100),

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

  CONSTRAINT fk_user_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_session_user
ON user_sessions(user_id);

CREATE INDEX idx_user_session_login
ON user_sessions(login_at);