package cn.lexy.auth.mapper.utils;


import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by john on 16/8/7.
 */
public class ParamUtils {
    public static Map<String, Object> filterParams(Map<String, Object> params) {

        Map<String, Object> filtered = new HashMap<String, Object>();
        if(null == params){
            return filtered;
        }
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            Object value = stringObjectEntry.getValue();
            boolean hasVal = false;
            if (value instanceof CharSequence) {
                if (StringUtils.hasText((CharSequence) value)) {
                    hasVal = true;
                }
            } else if (value.getClass().isArray()) {
                if (Array.getLength(value) > 0) {
                    hasVal = true;
                }
            } else if (value instanceof Collection) {
                if (!((Collection) value).isEmpty()) {
                    hasVal = true;
                }
            } else if (null != value) {
                hasVal = true;
            }
            if (hasVal) {
                filtered.put(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            }
        }
        return filtered;
    }


    public static String getIDFromUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
