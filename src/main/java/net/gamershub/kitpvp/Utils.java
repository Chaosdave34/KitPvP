package net.gamershub.kitpvp;

import com.google.gson.Gson;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Field;

public class Utils {
    public static Object getPrivateField(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);

            result = field.get(instance);

            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while getting private field.");
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

    public static void spawnPlayer(Player p) {
        p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 100.5, -8.5, 0, 0));

        AttributeInstance maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null)
            p.setHealth(maxHealth.getValue());

        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setExhaustion(0);

        p.setFireTicks(0);

        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }
}
