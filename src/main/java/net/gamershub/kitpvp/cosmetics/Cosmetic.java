package net.gamershub.kitpvp.cosmetics;

import lombok.Getter;
import org.bukkit.Material;

@Getter
public abstract class Cosmetic {
    protected String id;
    protected String name ;
    protected int levelRequirement;
    protected Material icon;

    public Cosmetic(String id, String name, int levelRequirement, Material icon) {
        this.id = id;
        this.name = name;
        this.levelRequirement = levelRequirement;
        this.icon = icon;
    }
}
