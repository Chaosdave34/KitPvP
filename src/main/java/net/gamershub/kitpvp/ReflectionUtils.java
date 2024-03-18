package net.gamershub.kitpvp;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;

@SuppressWarnings("unused")
public class ReflectionUtils {
    @Nullable
    public static Object getPrivateFieldValue(Class<?> clazz, Object instance, String mojangMapping, String obfuscated) {
        Object result = null;

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(mojangMapping) || field.getName().equals(obfuscated)) {
                    field.setAccessible(true);
                    result = field.get(instance);
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            String message = "Error while getting private field value for " + mojangMapping + " in " + instance.getClass().getName() + ". " + e.getMessage();
            KitPvpPlugin.INSTANCE.getLogger().warning(message);
        }
        return result;
    }

    @Nullable
    public static Object getPrivateFieldValue(Object instance, String mojangMapping, String obfuscated) {
        return getPrivateFieldValue(instance.getClass(), instance, mojangMapping, obfuscated);
    }


    @Nullable
    public static Object getPrivateMethodReturn(Object instance, String mojangMapping, String obfuscated, Object... args) {
        Object result = null;

        try {
            for (Method method : instance.getClass().getDeclaredMethods()) {
                if (method.getName().equals(mojangMapping) || method.getName().equals(obfuscated)) {
                    if (method.getParameterCount() != args.length) continue;
                    method.setAccessible(true);
                    result = method.invoke(instance, args);
                    method.setAccessible(false);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            String message = "Error while getting private method return value for " + mojangMapping + " in " + instance.getClass().getName() + ". " + e.getMessage();
            KitPvpPlugin.INSTANCE.getLogger().warning(message);
        }
        return result;
    }

    public static void setPrivateFieldValue(Class<?> clazz, Object instance, String mojangMapping, String obfuscated, Object value) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(mojangMapping) || field.getName().equals(obfuscated)) {
                    field.setAccessible(true);
                    field.set(instance, value);
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while setting private field value.");
        }
    }

    public static void unfreezeRegistry(Registry<?> registry) {
        try {
            for (Field field : MappedRegistry.class.getDeclaredFields()) {
                // private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders
                if (field.getName().equals("unregisteredIntrusiveHolders") || field.getName().equals("m")) {
                    field.setAccessible(true);
                    field.set(registry, new IdentityHashMap<>());
                    field.setAccessible(false);
                }
                // private boolean frozen
                else if (field.getName().equals("frozen") || field.getName().equals("l")) {
                    field.setAccessible(true);
                    field.set(registry, false);
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while to unfreeze registry.");
        }
    }
}
