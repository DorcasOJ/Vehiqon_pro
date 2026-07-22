CREATE TABLE user_feature_statistics
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    feature VARCHAR(50) NOT NULL,

    visit_count INTEGER,

    total_events INTEGER,

    total_duration_seconds BIGINT,

    average_duration_seconds BIGINT,

    first_visited_at TIMESTAMP,

    last_visited_at TIMESTAMP,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

     CONSTRAINT uk_user_feature_statistics_user_feature
        UNIQUE (user_id, feature),

     CONSTRAINT fk_user_feature_statistic_user
            FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE


);

CREATE INDEX idx_user_feature_statistics_user
ON user_feature_statistics(user_id);

CREATE INDEX idx_user_feature_statistics_feature
ON user_feature_statistics(feature);