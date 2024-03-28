package io.github.chaosdave34.kitpvp.companions;

import io.github.chaosdave34.ghutils.utils.PDCUtils;
import io.github.chaosdave34.kitpvp.ExtendedPlayer;
import io.github.chaosdave34.kitpvp.KitPvp;
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomFollowOwnerGoal;
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomMeleeAttackGoal;
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomOwnerHurtByTarget;
import io.github.chaosdave34.kitpvp.pathfindergoals.CustomOwnerHurtTarget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMob;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class Companion {
    protected final String name;
    protected final double attackDamage;

    public Companion(String name, int attackDamage) {
        this.name = name;
        this.attackDamage = attackDamage;
    }

    public abstract PathfinderMob createVanillaMob(Level world);

    public org.bukkit.entity.Mob createCompanion(Player owner) {
        PathfinderMob companion = createVanillaMob(((CraftWorld) owner.getLocation().getWorld()).getHandle());
        companion.setPos(CraftVector.toNMS(owner.getLocation().toVector()));

        companion.getAttributes().registerAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance attribute = companion.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attribute != null) attribute.setBaseValue(attackDamage);

        companion.setCustomName(Component.literal(name));
        companion.setCustomNameVisible(true);

        companion.goalSelector.removeAllGoals(goal -> true);

        companion.goalSelector.addGoal(1, new FloatGoal(companion));
        companion.goalSelector.addGoal(2, new CustomMeleeAttackGoal(companion, owner, 1, false));
        companion.goalSelector.addGoal(3, new CustomFollowOwnerGoal(companion, owner, 1.0D, 10.0F, 2.0F, false));

        companion.targetSelector.removeAllGoals(goal -> true);

        companion.targetSelector.addGoal(1, new CustomOwnerHurtByTarget(companion, owner));
        companion.targetSelector.addGoal(2, new CustomOwnerHurtTarget(companion, owner));
        companion.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(companion, net.minecraft.world.entity.player.Player.class, 10, true, true, livingEntity -> {
            if (ExtendedPlayer.from(owner).inSpawn()) return false;

            if (!livingEntity.getUUID().equals(owner.getUniqueId())) {
                if (livingEntity.getBukkitLivingEntity() instanceof Player player) {
                    return ExtendedPlayer.from(player).inGame();
                } else return true;
            }
            return false;
        }));

        CraftMob bukkitMob = companion.getBukkitMob();
        bukkitMob.setMetadata("companion", new FixedMetadataValue(KitPvp.INSTANCE, true));
        PDCUtils.setOwner(bukkitMob, owner.getUniqueId());

        return bukkitMob;
    }

    public PathfinderMob createDummyCompanion(Location location) {
        PathfinderMob companion = createVanillaMob(((CraftWorld) location.getWorld()).getHandle());
        companion.setPos(CraftVector.toNMS(location.toVector()));
        companion.setRot(location.getYaw(), location.getPitch());
        companion.setYHeadRot(location.getYaw());
        companion.setInvulnerable(true);
        companion.setNoAi(true);
        companion.setSilent(true);

        companion.setCustomName(Component.literal(name));
        companion.setCustomNameVisible(true);

        return companion;
    }

}
