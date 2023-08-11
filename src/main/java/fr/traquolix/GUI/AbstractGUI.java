package fr.traquolix.GUI;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.Flag;
import fr.traquolix.player.PlayerRegistry;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
@Getter
public abstract class AbstractGUI {
    protected Inventory inventory;
    ItemStack closeItem = ItemStack.of(Material.BARRIER).withDisplayName(Component.text("Close")
            .decoration(TextDecoration.ITALIC, false)
            .color(NamedTextColor.RED));
    protected ItemStack backGroundItem = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE).withDisplayName(Component.text(""));
    protected Identifier identifier;

    protected AbstractGUI(InventoryType inventoryType, String name) {
        inventory = new Inventory(inventoryType, name);
        inventory.addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);
        });
    }

    public abstract void initIdentifier();

    public void fillInventoryWith(ItemStack itemStack) {
        fill(0, inventory.getSize(), itemStack);
    }

    public void setItemStack(int slot, ItemStack itemStack) {
        inventory.setItemStack(slot, itemStack);
    }

    public void addInventoryCondition(InventoryCondition inventoryCondition) {
        inventory.addInventoryCondition(inventoryCondition);
    }


    public void open(CPlayer cPlayer) {
        refresh(cPlayer);
        cPlayer.openGui(inventory);
    }

    public void refresh(CPlayer cPlayer) {
        inventory.getInventoryConditions().clear();
        inventory.clear();
        inventory.addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);
        });
        fillInventoryWith(backGroundItem);
    }

    public void close(CPlayer cPlayer) {
        cPlayer.closeGui();
    }

    public void addCloseItem(int slot) {
        inventory.setItemStack(slot, closeItem);
        inventory.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                close(PlayerRegistry.getInstance().getCPlayer(p.getUuid()));
            }
        });
    }

    public void addToggleFlagItem(ItemStack itemStackOn, ItemStack itemStackOff, int slot, Flag flag, CPlayer cPlayer) {
        if (cPlayer.hasFlag(flag)) {
            inventory.setItemStack(slot, itemStackOn);
        } else {
            inventory.setItemStack(slot, itemStackOff);
        }
        inventory.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                cPlayer.toggle(flag);
                refresh(cPlayer);
            }
        });
    }

    public void fill(int start, int end, ItemStack itemStack) {
        for (int i = start; i < end; i++) {
            setItemStack(i, itemStack);
        }
    }

    public void addInventoryOpener(int slot, ItemStack itemStack, AbstractGUI guiToOpen) {
        inventory.setItemStack(slot, itemStack);
        inventory.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                guiToOpen.open(PlayerRegistry.getInstance().getCPlayer(p.getUuid()));
            }
        });
    }
}
