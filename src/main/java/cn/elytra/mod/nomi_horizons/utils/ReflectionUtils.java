package cn.elytra.mod.nomi_horizons.utils;

import cn.elytra.mod.nomi_horizons.utils.reflection.FieldAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    private static final Field MODIFIERS_FIELD;

    static {
        try {
            MODIFIERS_FIELD = Field.class.getDeclaredField("modifiers");
            MODIFIERS_FIELD.setAccessible(true);
        } catch(NoSuchFieldException e) {
            throw new RuntimeException("Exception occurred while initializing ReflectionUtils!", e);
        }
    }

    public static <T> FieldAccessor<T> getDeclaredFieldInClass(Class<?> clazz, String fieldName) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            if(Modifier.isFinal(field.getModifiers())) {
                unlockFinalField(field);
            }

            return new FieldAccessor<>() {
                @SuppressWarnings("unchecked")
                @Override
                public T get(Object self) {
                    try {
                        return (T) field.get(self);
                    } catch(IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void set(Object self, T value) {
                    try {
                        field.set(self, value);
                    } catch(IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        } catch(NoSuchFieldException e) {
            throw new IllegalStateException("Cannot find field " + fieldName + " in class " + clazz.getName(), e);
        } catch(IllegalAccessException e) {
            throw new IllegalStateException("Cannot access field " + fieldName + " in class " + clazz.getName(), e);
        }
    }

    public static void unlockFinalField(Field field) throws IllegalAccessException {
        MODIFIERS_FIELD.setInt(field, MODIFIERS_FIELD.getInt(field) & ~Modifier.FINAL);
    }

}
