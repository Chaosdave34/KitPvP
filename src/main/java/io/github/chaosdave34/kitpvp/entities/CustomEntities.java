package io.github.chaosdave34.kitpvp.entities;

import io.github.chaosdave34.ghutils.entity.CustomEntity;
import io.github.chaosdave34.kitpvp.entities.impl.Turret;
import org.bukkit.event.Listener;

public class CustomEntities implements Listener {
    public static CustomEntity TURRET = new Turret();
}
