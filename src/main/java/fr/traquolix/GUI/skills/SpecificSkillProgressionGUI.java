package fr.traquolix.GUI.skills;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.AbstractSkill;
import fr.traquolix.skills.Skill;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

// TODO Système de palier ? Genre 5 paliers, par tranche de 20 niveaux ? Ou par tranche de 10 niveaux ? Faut voir si naviguer dans les menus est pas trop pénible. Peut être avoir les infos les plus importantes rapidement à portée de main serait le mieux.
// TODO Ouvrir l'inventaire directement au niveau du joueur en fonction du skill est absolument nécessaire aussi je pense. De toute façon, je pense pas que ce sera un écran si souvent visité. Quoi que. Tout dépend de ce que l'on peut obtenir en montant les niveaux.
public class SpecificSkillProgressionGUI extends AbstractGUI {
    Identifier identifier = new Identifier("gui", "specific_skill_progression");
    Skill skill;
    AbstractSkill abstractSkill;
    int tier; // 1 = 20, 2 = 40, 3 = 60, 4 = 80, 5 = 100

    public SpecificSkillProgressionGUI(Skill skill, AbstractSkill abstractSkill, int tier) {
        super(InventoryType.CHEST_6_ROW, "Tier " + Utils.toRomanNumeral(tier) + " " + Utils.capitalizeFirstLetter(skill.name()));
        this.skill = skill;
        this.abstractSkill = abstractSkill;
        this.tier = tier;
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);


        List<Integer> slotOrder = List.of(0, 9, 10, 11, 20, 29,
                30, 31, 22, 13, 4, 5, 6, 15, 24, 25, 26, 35, 44, 53);


        switch (tier) {
            case 1 -> {
                int counter = 1;
                generateTimeline(slotOrder, counter);
                addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Go back").decoration(TextDecoration.ITALIC, false)), new SkillGUI(cPlayer));
                addCloseItem(49);
                addInventoryOpener(50, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Next page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 2));
                addResetPerksItemStack(cPlayer);
            }
            case 2 -> {
                int counter = 21;
                generateTimeline(slotOrder, counter);
                addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Previous page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 1));
                addCloseItem(49);
                addInventoryOpener(50, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Next page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 3));
                addResetPerksItemStack(cPlayer);
            }
            case 3 -> {
                int counter = 41;
                generateTimeline(slotOrder, counter);
                addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Previous page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 2));
                addCloseItem(49);
                addInventoryOpener(50, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Next page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 4));
                addResetPerksItemStack(cPlayer);
            }
            case 4 -> {
                int counter = 61;
                for (int i: slotOrder){
                    if (counter == abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.YELLOW_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)).withLore(Utils.generateLore(skill, abstractSkill)));
                    }
                    if (counter > abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.RED_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
                    }
                    if (counter < abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.LIME_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
                    }
                    counter++;
                }
                addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Previous page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 3));
                addCloseItem(49);
                addInventoryOpener(50, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Next page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 5));
                addResetPerksItemStack(cPlayer);
            }
            case 5 -> {
                int counter = 81;
                for (int i: slotOrder){
                    if (counter == abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.YELLOW_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)).withLore(Utils.generateLore(skill, abstractSkill)));
                    }
                    if (counter > abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.RED_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
                    }
                    if (counter < abstractSkill.getLevel()) {
                        setItemStack(i, ItemStack.of(Material.LIME_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
                    }
                    counter++;
                }
                addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Previous page").decoration(TextDecoration.ITALIC, false)), new SpecificSkillProgressionGUI(skill, abstractSkill, 4));
                addCloseItem(49);
                addResetPerksItemStack(cPlayer);
            }
        }

    }

    private void addResetPerksItemStack(CPlayer cPlayer) {
        setItemStack(45, ItemStack.of(Material.BELL).withDisplayName(Component.text("Click to reset Tier " + Utils.toRomanNumeral(tier) + " perks", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true)));
        addInventoryOpener(46, ItemStack.of(Material.FIRE_CORAL).withDisplayName(Component.text("Click to open perk tree of " + Utils.capitalizeFirstLetter(skill.name()), NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)), new SkillPerksTreeGUI(skill));
        addResetPerkInventoryCondition(cPlayer);
    }

    private void addResetPerkInventoryCondition(CPlayer cPlayer) {
        addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
            if (slot == 45) {
                cPlayer.resetPerks(skill, tier);
            }
        });
        setItemStack(28, skill.getTier(tier));
        setItemStack(7, skill.getTier(tier));
        setItemStack(52, skill.getTier(tier));
    }

    private void generateTimeline(List<Integer> slotOrder, int counter) {
        for (int i: slotOrder){
            if (counter == abstractSkill.getLevel()) {
                setItemStack(i, ItemStack.of(Material.YELLOW_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)).withLore(Utils.generateLore(skill, abstractSkill)));
            }
            if (counter > abstractSkill.getLevel()) {
                setItemStack(i, ItemStack.of(Material.RED_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
            }
            if (counter < abstractSkill.getLevel()) {
                setItemStack(i, ItemStack.of(Material.GREEN_STAINED_GLASS_PANE).withDisplayName(Component.text("Level " + Utils.toRomanNumeral(counter)).decoration(TextDecoration.ITALIC, false)));
            }
            counter++;
        }
    }
}
