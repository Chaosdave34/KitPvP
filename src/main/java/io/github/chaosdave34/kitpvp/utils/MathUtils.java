package io.github.chaosdave34.kitpvp.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class MathUtils {
    public static int binomialCoefficient(int n, int k) {
        if (k > n - k)
            k = n - k;

        int b = 1;
        for (int i = 1, m = n; i <= k; i++, m--)
            b = b * m / i;

        return b;
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
}
