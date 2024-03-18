package net.gamershub.kitpvp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class Utils {
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

    @Nullable
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
            fileInputStream.close();

            return new Gson().fromJson(stringBuilder.toString(), clazz);
        } catch (IOException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while reading object from file! " + e.getMessage());
            return null;
        }
    }

    public static int binomialCoefficient(int n, int k) {
        if (k > n - k)
            k = n - k;

        int b = 1;
        for (int i = 1, m = n; i <= k; i++, m--)
            b = b * m / i;

        return b;
    }

    public static Map<UUID, Integer> loadHighescore(String name) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(KitPvpPlugin.INSTANCE.getDataFolder(), name));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            @SuppressWarnings("Convert2Diamond")
            Map<UUID, Integer> highscore = new Gson().fromJson(stringBuilder.toString(), new TypeToken<Map<UUID, Integer>>() {
            });

            return highscore == null ? Collections.emptyMap() : highscore;


        } catch (IOException e) {
            KitPvpPlugin.INSTANCE.getLogger().warning("Error while reading high scores from file! " + e.getMessage());
        }
        return Collections.emptyMap();
    }

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        List<Location> sphereBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        sphereBlocks.add(l);
                    }
                }
            }
        }

        return sphereBlocks;
    }

    public static List<Location> generateCircle(Location centerBlock, int radius, boolean hollow) {
        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int z = bz - radius; z <= bz + radius; z++) {
                double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)));

                if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                    Location l = new Location(centerBlock.getWorld(), x, centerBlock.getBlockY(), z);
                    circleBlocks.add(l);
                }

            }
        }
        return circleBlocks;
    }

    public static void spawnNmsEntity(Player p, Entity entity) {
        CraftPlayer cp = (CraftPlayer) p;
        ServerPlayer sp = cp.getHandle();
        ServerGamePacketListenerImpl connection = sp.connection;

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(entity);
        connection.send(addEntityPacket);

        List<SynchedEntityData.DataValue<?>> nonDefaultValues = entity.getEntityData().getNonDefaultValues();
        if (nonDefaultValues != null) {
            ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData().getNonDefaultValues());
            connection.send(setEntityDataPacket);
        }
    }
}
