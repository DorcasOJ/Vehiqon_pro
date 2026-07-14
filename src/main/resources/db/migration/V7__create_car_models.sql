
CREATE TABLE IF NOT EXISTS car_models (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    car_brand_id UUID NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_models_brand
        FOREIGN KEY (car_brand_id)
        REFERENCES car_brands(id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_models_brand_id
    ON car_models (car_brand_id);

--CREATE UNIQUE INDEX IF NOT EXISTS uk_car_model_name_brand_year
--    ON car_models(name, car_brand_id);
