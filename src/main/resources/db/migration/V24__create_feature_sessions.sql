CREATE TABLE feature_sessions
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    user_session_id UUID NOT NULL,

    feature VARCHAR(50) NOT NULL,

    started_time TIMESTAMP NOT NULL,

    ended_time TIMESTAMP,

    last_activity_time TIMESTAMP,

    duration_seconds BIGINT,

    created_at TIMESTAMP,

    updated_at TIMESTAMP
);

CREATE INDEX idx_feature_user
ON feature_sessions(user_id);

CREATE INDEX idx_feature_name
ON feature_sessions(feature);