CREATE TABLE user_events
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    user_session_id UUID,

    feature_session_id UUID,

    feature VARCHAR(50),

    event_type VARCHAR(80) NOT NULL,

    entity_type VARCHAR(80),

    entity_id UUID,

    metadata JSONB,

    occurred_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP,

    updated_at TIMESTAMP
);

CREATE INDEX idx_events_user
ON user_events(user_id);

CREATE INDEX idx_events_type
ON user_events(event_type);

CREATE INDEX idx_events_feature
ON user_events(feature);

CREATE INDEX idx_events_occurred
ON user_events(occurred_at);

CREATE INDEX idx_events_metadata
ON user_events
USING GIN(metadata);