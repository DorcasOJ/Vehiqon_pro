package com.vehiqon.features.insights.auditLog.dto.requestScope;

import com.vehiqon.common.exception.BadRequestException;
import com.vehiqon.features.onboarding.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Component
@RequestScope
@Getter
@Setter
@Slf4j
public class AuditContext {
    private final Map<String, Object> metadata = new HashMap<>();

    public void put(String key, Object value) {
        if(value != null) {
            metadata.put(key, value);
        }
    }

    public <T> void recordChange(Map<String, Object> updatedFields, T entity) {
        if (entity == null || updatedFields == null || updatedFields.isEmpty()) return;

        Map<String, Object> changes = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : updatedFields.entrySet()) {
            String fieldName = entry.getKey();
            Object newValue = entry.getValue();
            try {
                Field field = findField(entry.getClass(), fieldName);
                if (field == null) {
                    log.warn("Field '{}' not found on class '{}'", fieldName, entity.getClass().getSimpleName());
                    continue;
                }
                field.setAccessible(true);
                Object oldValue = field.get(entity);
                if (!Objects.equals(oldValue, newValue)) {
                    changes.put(fieldName, Map.of(
                            "old", oldValue,
                            "new", newValue
                    ));
                }

            } catch (IllegalAccessException e) {
                log.error("Unable to access field '{}' on entity '{}'",
                        fieldName, entity.getClass().getSimpleName(), e); throw new RuntimeException(e);
            }
        }
        metadata.put("changes", changes);

    }

    private Field findField(Class<?> aClass, String fieldName) {
        Class<?> current = aClass;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                log.warn("Unknown Field '{}' while recording audit changes", fieldName);
                current = current.getSuperclass();
            }
        }
        return null;
    }

    public void recordContactChange(Map<String, Object> updatedFields, UserEntity user) {
        LinkedHashMap<String, Object> changes = new LinkedHashMap<>();
        if (updatedFields.get("email") != null && !Objects.equals(updatedFields.get("email"), user.getEmail())) {
            changes.put("email", Map.of(
                    "old", updatedFields.get("email"),
                    "new", user.getEmail()
            ));
        }

        if (updatedFields.get("phoneNumber") != null &&
                !Objects.equals(updatedFields.get("phoneNumber"), user.getPhoneNumber())) {
            changes.put("phoneNumber", Map.of(
                    "old", updatedFields.get("phoneNumber"),
                    "new", user.getEmail()
            ));
        }
        changes.put("updateSource", "user");
        metadata.put("changes", changes);
    }

    public void recordMultipleRestored(List<UUID> ids, Integer restoredCount,  String updateSource) {
        if (ids == null || ids.isEmpty()) return;

        Map<String, Object> changes = new LinkedHashMap<>();
        changes.put("ids", ids);
        changes.put("deletedCount", restoredCount);
        changes.put("updateSource", updateSource);
        metadata.put("restored", changes);
    }


    public void recordMultipleDelete(List<UUID> ids,UUID deleteByUserId,  Integer deletedCount,  String updateSource) {
        if (ids == null || ids.isEmpty()) return;

        Map<String, Object> changes = new LinkedHashMap<>();
        changes.put("ids", ids);
        changes.put("deletedCount", deletedCount);
        changes.put("deletedByUserId", deleteByUserId);
        changes.put("updateSource", updateSource);
        metadata.put("deleted", changes);
    }

    public void recordDelete(UUID id, UUID deleteByUserId, String updateSource) {
        if (id == null) return;
        Map<String, Object> changes = new LinkedHashMap<>();
        changes.put("id", id);
        changes.put("deletedByUserId", deleteByUserId);
        changes.put("updateSource", updateSource);
        metadata.put("deleted", changes);
    }

    public void recordRestore(UUID id, UUID deleteByUserId, String updateSource) {
        if (id == null) return;
        Map<String, Object> changes = new LinkedHashMap<>();
        changes.put("id", id);
        changes.put("deletedBy", deleteByUserId);
        changes.put("updateSource", updateSource);
        metadata.put("restored", changes);
    }

}
