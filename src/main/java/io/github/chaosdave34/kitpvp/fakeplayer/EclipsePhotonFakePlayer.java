package io.github.chaosdave34.kitpvp.fakeplayer;

import io.github.chaosdave34.ghutils.fakeplayer.FakePlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

public class EclipsePhotonFakePlayer extends FakePlayer {
    public EclipsePhotonFakePlayer() {
        super("EclipsePhoton", "world_elytra", new Location(null, -69.5, 107, -14.5), -90, 15, true);

        texture = "ewogICJ0aW1lc3RhbXAiIDogMTU5NDY3Mjg5NzYyNiwKICAicHJvZmlsZUlkIiA6ICJmZDYwZjM2ZjU4NjE0ZjEyYjNjZDQ3YzJkODU1Mjk5YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWFkIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I5YTllMjUwOTY2YjE2NmYwYjY4ZWVmNGVmZjI1ZmJmNzg0YmQ3YjhhYjk5MzE0YzcyNWQ3ZTdkYzdkMzMxNTQiCiAgICB9CiAgfQp9";
        textureSignature = "FA+Kd+HuRBqkBJx0xK253TtP37txmvWQ2YzzJS6yZO2TC9saRF0WEDsp6XRx+mJfFX6YsIEdGjSuAYv8++KjhLrKfJNxzMGt4XuhPilbLbrTlsBcjyXWQK3qUXKhGfyx8HOTGBZwjSPcbHmEXeDQ4zSpNqMbvC41kv/718uiLKPAQoZvSB9+wDBGwl/bMBnd6j+irPMVRz3cQFHF4w8tSg5yuwdtod+abnp4KWanWFq5WXVMcicJvMn0UaQDlBH/3T51nh32y2VzLFdwGwUDkSCsFGnQNH7H8VLB1X5mNV4J95aIuqtCzaCeD3ST9uQevf+/pVrAKtUTvllcO5i+KU/8+r8esJWVtUxxdP6AThV4rddLN+PjVM4RLQ9nVLc2Z6c26VCjZlQ7aErt9MdNRNGBHNKTXMvfqeLBp4iS0HSlOpJeeTQ8trpOpAkVJvfxTq9/xmyFNE0uqQ0UwV2D+NWVBb+3GSl6cuvzxtjSrDClWa+qjF6A7gOAutNfVuubqdD5vMNliPRCbUMmBunZze43tTmKRiUaxpTNj8OoTM/FvFmiMJVzuLa9e9RduFkyZp/DJWZqGEWhs/VGM4kdiBDiJT/txrk9hTyAQZlyNLWX7GTvKkFzmUT0kR9bvKlvR1zRORZfZuHjyApEJpPyvjZfOdEmOC6J/7Ik4bnAkFw=";
    }

    @Override
    public void onAttack(Player p) {
        p.sendMessage(Component.text("Ich werde euren Server zerstüren!"));
    }

    @Override
    public void onInteract(Player p, EquipmentSlot hand) {
        p.sendMessage(Component.text("Ich werde euren Server zerstüren!"));
    }
}
