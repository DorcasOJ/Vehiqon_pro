package com.vehiqon.features.insights.analytics.repository;

import com.vehiqon.features.insights.analytics.entities.aggregation.UserPersonalisationEntity;
import com.vehiqon.features.insights.analytics.entities.aggregation.UserStatisticsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserPersonalisationRepository extends JpaRepository<UserPersonalisationEntity, UUID> {
    Optional<UserPersonalisationEntity> findByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query(value = """
        WITH 
        -- 1. Calculate raw engagement score per user & feature
        feature_scores AS (
            SELECT 
                user_id,
                feature,
                (total_duration_seconds + (visit_count * 300))::numeric AS raw_score
            FROM user_feature_statistics
        ),
        
        -- 2. Compute total engagement score per user
        user_totals AS (
            SELECT 
                user_id,
                SUM(raw_score) AS total_user_score
            FROM feature_scores
            GROUP BY user_id
        ),

        -- 3. Calculate normalized decimal weights and group into a JSON map
        calculated_weights AS (
            SELECT 
                fs.user_id,
                jsonb_object_agg(
                    fs.feature, 
                    ROUND(fs.raw_score / NULLIF(ut.total_user_score, 0), 2)
                ) AS weights_json
            FROM feature_scores fs
            JOIN user_totals ut ON fs.user_id = ut.user_id
            GROUP BY fs.user_id
        ),

        -- 4. Preferred Login Day & Time
        login_habits AS (
            SELECT DISTINCT ON (user_id)
                user_id,
                TO_CHAR(started_time, 'DAY') AS pref_day,
                CAST(DATE_TRUNC('hour', started_time) AS TIME) AS pref_time
            FROM feature_sessions
            GROUP BY user_id, TO_CHAR(started_time, 'DAY'), CAST(DATE_TRUNC('hour', started_time) AS TIME)
            ORDER BY user_id, COUNT(*) DESC
        ),

        -- 5. Average Session Length
        avg_session AS (
            SELECT 
                user_id,
                CASE 
                    WHEN total_sessions > 0 THEN CAST(ROUND((total_time_spent::numeric / total_sessions) / 60.0) AS INTEGER)
                    ELSE 0
                END AS avg_session_mins
            FROM user_statistics
        ),

        -- 6. Favourite Maintenance Type
        fav_maint AS (
            SELECT DISTINCT ON (user_id)
                user_id,
                metadata->>'maintenanceType' AS favourite_maint_type
            FROM user_events
            WHERE entity_type = 'MAINTENANCE' 
              AND metadata->>'maintenanceType' IS NOT NULL
            GROUP BY user_id, metadata->>'maintenanceType'
            ORDER BY user_id, COUNT(*) DESC
        ),

        -- 7. Preferred Payment Method
        pref_payment AS (
            SELECT DISTINCT ON (user_id)
                user_id,
                metadata->>'paymentMethod' AS pref_payment_method
            FROM user_events
            WHERE feature = 'PAYMENT' 
              AND metadata->>'paymentMethod' IS NOT NULL
            GROUP BY user_id, metadata->>'paymentMethod'
            ORDER BY user_id, COUNT(*) DESC
        )

        -- Perform bulk UPSERT into user_personalisation
        INSERT INTO user_personalisation (
            user_id, 
            feature_weights, 
            preferred_login_day, 
            preferred_login_time, 
            average_session_minutes, 
            favourite_maintenance_type, 
            preferred_payment_method,
            likes_push_notifications,
            likes_email_notifications,
            reminder_lead_hours
        )
        SELECT 
            u.id,
            COALESCE(cw.weights_json, '{}'::jsonb),
            TRIM(UPPER(lh.pref_day))::VARCHAR,
            lh.pref_time,
            COALESCE(asess.avg_session_mins, 0),
            fm.favourite_maint_type,
            pp.pref_payment_method,
            TRUE,
            TRUE,
            24
        FROM users u
        LEFT JOIN calculated_weights cw ON cw.user_id = u.id
        LEFT JOIN login_habits lh ON lh.user_id = u.id
        LEFT JOIN avg_session asess ON asess.user_id = u.id
        LEFT JOIN fav_maint fm ON fm.user_id = u.id
        LEFT JOIN pref_payment pp ON pp.user_id = u.id
        ON CONFLICT (user_id) DO UPDATE SET
            feature_weights = EXCLUDED.feature_weights,
            preferred_login_day = EXCLUDED.preferred_login_day,
            preferred_login_time = EXCLUDED.preferred_login_time,
            average_session_minutes = EXCLUDED.average_session_minutes,
            favourite_maintenance_type = EXCLUDED.favourite_maintenance_type,
            preferred_payment_method = EXCLUDED.preferred_payment_method;
    """, nativeQuery = true)
    void refreshUserPersonalisationFavourites();
}
