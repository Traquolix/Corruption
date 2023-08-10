package fr.traquolix.GUI;

import fr.traquolix.player.CPlayer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PaginatedGUI {
    ConcurrentMap<AbstractGUI, AbstractGUI> pages = new ConcurrentHashMap<>(); // Source -> Target
    AbstractGUI currentPage;

    public void nextPage(CPlayer cPlayer) {
        if (currentPage != null) {
            if (pages.containsKey(currentPage)) {
                currentPage = pages.get(currentPage);
                openPage(cPlayer, currentPage);
            }
        }
    }

    public void previousPage(CPlayer cPlayer) {
        if (currentPage != null) {
            for (AbstractGUI abstractGui : pages.keySet()) {
                if (pages.get(abstractGui) == currentPage) {
                    currentPage = abstractGui;
                    openPage(cPlayer, currentPage);
                }
            }
        }
    }


    void setNextPageItem(CPlayer player, AbstractGUI abstractGui, int slot) {
        abstractGui.setItemStack(slot, ItemStack.of(Material.ARROW));
        abstractGui.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                nextPage(player);
            }
        });
    }


    void setPreviousPageItem(CPlayer player, AbstractGUI abstractGui, int slot) {
        abstractGui.setItemStack(slot, ItemStack.of(Material.ARROW));
        abstractGui.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                previousPage(player);
            }
        });
    }

    public PaginatedGUI addPage(AbstractGUI abstractGui, AbstractGUI target) {
        pages.put(abstractGui, target);
        return this;
    }

    public void openPaginatedInventory(CPlayer cPlayer, AbstractGUI abstractGui) {
        if (!pages.isEmpty()) {
            pages.get(abstractGui).open(cPlayer);
        }
    }

    public void openPage(CPlayer cPlayer, AbstractGUI page) {
        pages.get(page).open(cPlayer);
    }
}
