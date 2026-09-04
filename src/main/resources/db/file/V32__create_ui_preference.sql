
CREATE TABLE IF NOT EXISTS ui_preference (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    template_id UUID NOT NULL,
    theme VARCHAR(20) NOT NULL,
    sidebar_mode VARCHAR(100),
    density VARCHAR(100),

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

   CONSTRAINT fk_ui_template
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

   CONSTRAINT fk_ui_template
           FOREIGN KEY (template_id)
           REFERENCES ui_template(id)
           ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_ui_preference_user_id
ON ui_preference(user_id);

CREATE INDEX IF NOT EXISTS idx_ui_preference_id
ON ui_preference(id);