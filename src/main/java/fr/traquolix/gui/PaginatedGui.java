package fr.traquolix.gui;

import fr.traquolix.player.CPlayer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PaginatedGui {
    ConcurrentMap<AbstractGui, AbstractGui> pages = new ConcurrentHashMap<>(); // Source -> Target
    AbstractGui currentPage;

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
            for (AbstractGui abstractGui : pages.keySet()) {
                if (pages.get(abstractGui) == currentPage) {
                    currentPage = abstractGui;
                    openPage(cPlayer, currentPage);
                }
            }
        }
    }


    void setNextPageItem(CPlayer player, AbstractGui abstractGui, int slot) {
        abstractGui.setItemStack(slot, ItemStack.of(Material.ARROW));
        abstractGui.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                nextPage(player);
            }
        });
    }


    void setPreviousPageItem(CPlayer player, AbstractGui abstractGui,int slot) {
        abstractGui.setItemStack(slot, ItemStack.of(Material.ARROW));
        abstractGui.addInventoryCondition((p, slot1, clickType1, inventoryConditionResult) -> {
            if (slot1 == slot) {
                previousPage(player);
            }
        });
    }

    public PaginatedGui addPage(AbstractGui abstractGui, AbstractGui target) {
        pages.put(abstractGui, target);
        return this;
    }

    public void openPaginatedInventory(CPlayer cPlayer, AbstractGui abstractGui) {
        if (!pages.isEmpty()) {
            pages.get(abstractGui).open(cPlayer);
        }
    }

    public void openPage(CPlayer cPlayer, AbstractGui page) {
        pages.get(page).open(cPlayer);
    }
}
