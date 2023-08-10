package fr.traquolix.gui;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import lombok.Getter;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
@Getter
public abstract class AbstractGui {
    protected Inventory inventory;
    ItemStack closeItem = ItemStack.of(Material.BARRIER);
    protected ItemStack backGroundItem = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);
    protected Identifier identifier;

    protected AbstractGui(InventoryType inventoryType, String name) {
        initIdentifier();
        inventory = new Inventory(inventoryType, name);
        inventory.addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);
        });

        GuiRegistry.getInstance().registerGui(this);
    }

    public abstract void initIdentifier();

    public void fillInventoryWith(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItemStack(i, itemStack);
        }
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

    public abstract void refresh(CPlayer cPlayer);

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
}
