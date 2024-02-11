package net.gamershub.kitpvp.abilities;

import lombok.Getter;
import net.gamershub.kitpvp.KitPvpPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;


@Getter
public abstract class Ability {
    protected int id;
    protected AbilityType type;
    protected String name;
    protected long cooldown;

    protected Map<UUID, Long> playerCooldown = new HashMap<>();

    public Ability(int id, String name, AbilityType type, long cooldown) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cooldown = cooldown;
    }

    @NotNull
    public abstract List<Component> getDescription();

    public ItemStack apply(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(KitPvpPlugin.INSTANCE, "abilities");

        if (container.has(key)) {
            int[] current_abilities = container.get(key, PersistentDataType.INTEGER_ARRAY);
            if (current_abilities == null) return itemStack;
            int[] new_abilities = new int[current_abilities.length + 1];
            new_abilities[current_abilities.length] = id;
            container.set(key, PersistentDataType.INTEGER_ARRAY, new_abilities);
        } else {
            container.set(key, PersistentDataType.INTEGER_ARRAY, new int[]{id});
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public void handleAbility(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SPECTATOR) return;

        if (playerCooldown.containsKey(p.getUniqueId())) {
            p.sendMessage(Component.text("This ability is on cooldown for " + playerCooldown.get(p.getUniqueId()) + "s.", NamedTextColor.RED));
        } else {
            playerCooldown.put(p.getUniqueId(), cooldown);

            new BukkitRunnable() {
                @Override
                public void run() {
                    long current_cooldown = playerCooldown.get(p.getUniqueId());
                    current_cooldown--;
                    playerCooldown.put(p.getUniqueId(), current_cooldown);
                    if (current_cooldown == 0) {
                        playerCooldown.remove(p.getUniqueId());

                        Component availableMessage = Component.text("Ability ", NamedTextColor.GREEN)
                                .append(Component.text(name, NamedTextColor.GOLD))
                                .append(Component.text(" is now available.", NamedTextColor.GREEN));
                        p.sendMessage(availableMessage);
                        this.cancel();
                    }
                }
            }.runTaskTimer(KitPvpPlugin.INSTANCE, 20, 20);

            onAbility(e);
        }
    }

    public abstract void onAbility(PlayerInteractEvent e);
}
