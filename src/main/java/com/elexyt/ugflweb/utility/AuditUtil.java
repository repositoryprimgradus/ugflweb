package com.elexyt.ugflweb.utility;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class AuditUtil {

    public static <T> T setCreated(String username, T obj) {
        try {
            Method setCreatedBy = obj.getClass().getMethod("setCreatedBy", String.class);
            Method setCreatedDate = obj.getClass().getMethod("setCreatedDate", LocalDateTime.class);
            setCreatedBy.invoke(obj, username);
            setCreatedDate.invoke(obj, LocalDateTime.now());
        } catch (Exception e) {
            // Handle exception or log as needed
        }
        return obj;
    }

    public static <T> T setModified(String username, T obj) {
        try {
            Method setModifiedBy = obj.getClass().getMethod("setModifiedBy", String.class);
            Method setModifiedDate = obj.getClass().getMethod("setModifiedDate", LocalDateTime.class);
            setModifiedBy.invoke(obj, username);
            setModifiedDate.invoke(obj, LocalDateTime.now());
        } catch (Exception e) {
            // Handle exception or log as needed
        }
        return obj;
    }
}
