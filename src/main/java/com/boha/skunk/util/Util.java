package com.boha.skunk.util;

import com.boha.skunk.data.User;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static Map<String, Object> objectToMap(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        // Get all fields of the User class
        Field[] fields = User.class.getDeclaredFields();

        // Iterate over the fields and add them to the map
        for (Field field : fields) {
            String fieldName = field.getName();
            Object fieldValue = field.get(object);
            map.put(fieldName, fieldValue);
        }

        return map;
    }

}
