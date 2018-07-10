
package jzt.datamapper.engine.utils;

import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.output.formatters.MapOutputFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataMapperEngineUtils {

    public static Map<String, Object> getMapFromNativeArray(Object value) throws JSException {
        try {
            final Class<?> cls = Class.forName(MapOutputFormatter.RHINO_NATIVE_ARRAY_FULL_QUALIFIED_CLASS_NAME);
            if (cls.isAssignableFrom(value.getClass())) {
                Map<String, Object> tempValue = new HashMap();
                final Method getIds = cls.getMethod("getIds");
                Method get = null;
                Method[] allMethods = cls.getDeclaredMethods();
                for (Method method : allMethods) {
                    String methodName = method.getName();
                    // find the method with name get
                    if ("get".equals(methodName)) {
                        Type[] pType = method.getGenericParameterTypes();
                        //find the get method with two parameters and first one a int
                        if ((pType.length == 2) && ((Class) pType[0]).getName().equals("int")) {
                            get = method;
                            break;
                        }
                    }
                }
                final Object[] result = (Object[]) getIds.invoke(value);
                for (Object id : result) {
                    Object childValue = get.invoke(value, id, value);
                    tempValue.put(id.toString(), childValue);
                }
                return tempValue;
            }
            throw new JSException(
                    "Un-assignable class found for sun.org.mozilla.javascript.internal.NativeArray as :" + cls
                            .toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new JSException("Error while parsing rhino native array values",e);
        }
    }
}
