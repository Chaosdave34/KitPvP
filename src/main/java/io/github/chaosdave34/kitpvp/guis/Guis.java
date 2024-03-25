package io.github.chaosdave34.kitpvp.guis;

import io.github.chaosdave34.ghutils.gui.Gui;
import io.github.chaosdave34.kitpvp.KitPvp;

public class Guis {
    public static Gui COSMETICS = new CosmeticsGui();
    public static Gui PROJECTILE_TRAILS = new CosmeticSubMenuGui("Projectile Trails", KitPvp.INSTANCE.getCosmeticHandler().getProjectileTrails().values());
    public static Gui KILL_EFFECTS = new CosmeticSubMenuGui("Kill Effects", KitPvp.INSTANCE.getCosmeticHandler().getKillEffects().values());
}
