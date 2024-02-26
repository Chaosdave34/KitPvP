package net.gamershub.kitpvp.items;

import net.gamershub.kitpvp.KitPvpPlugin;
import net.gamershub.kitpvp.abilities.Ability;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class CustomItem implements Listener {
    protected Material material;
    protected String id;
    protected boolean stackable;

    public CustomItem(Material material, String id, boolean stackable) {
        this.material = material;
        this.id = id;
        this.stackable = stackable;
    }

    @NotNull
    public abstract Component getName();

    @NotNull
    public abstract List<Component> getDescription();

    @NotNull
    public List<Ability> getAbilities() {
        return Collections.emptyList();
    }

    public ItemStack build(int count) {
        ItemStack itemStack = new ItemStack(material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.displayName(getName());

        container.set(new NamespacedKey(KitPvpPlugin.INSTANCE, "id"), PersistentDataType.STRING, String.valueOf(id));

        if (!stackable) {
            container.set(new NamespacedKey(KitPvpPlugin.INSTANCE, "uuid"), PersistentDataType.STRING, String.valueOf(UUID.randomUUID()));
        }

        itemStack.setItemMeta(itemMeta);

        for (Ability ability : getAbilities()) {
            ability.apply(itemStack);
        }

        setCustomLore(itemStack);

        additionalModifications(itemStack);

        return itemStack;

    }

    private void setCustomLore(ItemStack itemStack) {
        // Description
        List<Component> lore = new ArrayList<>(getDescription());

        if (!getDescription().isEmpty()) lore.add(Component.empty());

        // Enchantments
        List<Component> enchantmentLore = new ArrayList<>();

        for (Map.Entry<Enchantment, Integer> enchantment : itemStack.getEnchantments().entrySet()) {
            Component component = enchantment.getKey().displayName(enchantment.getValue()).decoration(TextDecoration.ITALIC, false);
            enchantmentLore.add(component);
        }

        enchantmentLore.sort(Comparator.comparing(s -> PlainTextComponentSerializer.plainText().serialize(s).toLowerCase()));

        lore.addAll(enchantmentLore);

        if (!enchantmentLore.isEmpty()) lore.add(Component.empty());

        // Abilities
        Iterator<Ability> abilities = KitPvpPlugin.INSTANCE.getAbilityHandler().getItemAbilities(itemStack).iterator();
        while (abilities.hasNext()) {
            Ability ability = abilities.next();

            Component name = Component.text("Ability: ", NamedTextColor.GREEN)
                    .append(Component.text(ability.getName(), NamedTextColor.GOLD))
                    .append(Component.text("  "))
                    .append(Component.text(ability.getType().getDisplayName(), NamedTextColor.YELLOW, TextDecoration.BOLD))
                    .decoration(TextDecoration.ITALIC, false);

            lore.add(name);

            lore.addAll(ability.getDescription());

            Component cooldown = Component.text("Cooldown: ", NamedTextColor.DARK_GRAY)
                    .append(Component.text(ability.getCooldown(), NamedTextColor.GREEN))
                    .append(Component.text("s", NamedTextColor.GREEN))
                    .decoration(TextDecoration.ITALIC, false);

            lore.add(cooldown);

            if (abilities.hasNext()) lore.add(Component.empty());
        }

        if (!lore.isEmpty() && PlainTextComponentSerializer.plainText().serialize(lore.get(lore.size() - 1)).isEmpty()) {
            lore.remove(lore.size() - 1);
        }

        itemStack.lore(lore);
    }

    protected void additionalModifications(ItemStack itemStack) {
    }


}
