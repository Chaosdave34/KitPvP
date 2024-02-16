package net.gamershub.kitpvp;

import com.google.gson.Gson;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {
    public static Object getPrivateFieldValue(Object instance, String mojangMapping, String obfuscated) {
        Object result = null;

        try {
            for (Field field : instance.getClass().getDeclaredFields()) {
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


    public static void sendPacketToOnlinePlayers(Packet<?> packet) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer cp = (CraftPlayer) player;
            ServerPlayer sp = cp.getHandle();
            ServerGamePacketListenerImpl connection = sp.connection;

            connection.send(packet);
        }
    }

    public static void registerEvents(@NotNull Listener listener) {
        KitPvpPlugin.INSTANCE.getServer().getPluginManager().registerEvents(listener, KitPvpPlugin.INSTANCE);
    }

    public static void writeObjectToFile(File file, Object object) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(new Gson().toJson(object).getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while writing object to file! " + e.getMessage());
        }
    }

    public static <T> T readObjectFromFile(File file, Class<T> clazz) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            ;
            fileInputStream.close();

            return new Gson().fromJson(stringBuilder.toString(), clazz);
        } catch (IOException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while reading object from file! " + e.getMessage());
            return null;
        }
    }
}
