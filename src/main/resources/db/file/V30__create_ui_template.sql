
CREATE TABLE IF NOT EXISTS ui_template (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    preview_url TEXT,
    version NUMERIC(10) NOT NULL DEFAULT 1.0,
    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ui_template_id
ON ui_template(id);

CREATE INDEX IF NOT EXISTS idx_ui_template_slug
ON ui_template(slug);