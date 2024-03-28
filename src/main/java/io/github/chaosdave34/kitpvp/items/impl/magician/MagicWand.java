package io.github.chaosdave34.kitpvp.items.impl.magician;

import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.abilities.Ability;
import io.github.chaosdave34.kitpvp.abilities.AbilityHandler;
import io.github.chaosdave34.kitpvp.items.CustomItem;
import io.github.chaosdave34.kitpvp.items.CustomItemHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class MagicWand extends CustomItem {
    public MagicWand() {
        super(Material.STICK, "magic_wand", false);
    }


    @Override
    public @NotNull Component getName() {
        return createSimpleItemName("Magic Wand");
    }

    @Override
    public @NotNull List<Component> getDescription() {
        return createSimpleDescription("Feel the magic flow through your body.");
    }

    @Override
    public @NotNull List<Ability> getAbilities() {
        return List.of(AbilityHandler.DEBUFF, AbilityHandler.LEVITATE);
    }


    @Override
    protected void additionalModifications(ItemStack itemStack) {
        setCustomModelData(itemStack, 1);
        itemStack.editMeta(itemMeta -> itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(), "default", -2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) return;

        if (ExtendedPlayer.from(p).inSpawn())
            return;

        if (getId().equals(CustomItemHandler.getCustomItemId(e.getItem()))) {
            if (e.getAction().isLeftClick()) {
                Location location = p.getEyeLocation().subtract(0, 0.25, 0);
                if (p.getAttackCooldown() == 1) {
                    for (int i = 0; i < 30; i++) {
                        p.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(location.getDirection().clone().multiply(i * 0.2)), 1, 0, 0, 0, new Particle.DustOptions(Color.PURPLE, 1));
                    }

                    Entity target = p.getTargetEntity(8);
                    if (target instanceof LivingEntity livingEntity) {
                        livingEntity.damage(4, DamageSource.builder(DamageType.MAGIC).withDirectEntity(p).withCausingEntity(p).build());
                        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.1f);
                    }
                }
            }
        }
    }
}
