package fr.traquolix.events;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import fr.traquolix.utils.Utils;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.item.ItemStack;

import static fr.traquolix.Main.logger;

public class EntityChangeGearEvent {

    private final PlayerRegistry playerRegistry = PlayerRegistry.getInstance();
    private final ItemRegistry itemRegistry = ItemRegistry.getInstance();

    public EntityChangeGearEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerChangeHeldSlotEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer().getUuid());
            cPlayer.refreshBonuses();
            handleItemInHandEvent(event.getPlayer().getInventory().getItemStack(event.getSlot()), cPlayer);
        });

        globalEventHandler.addListener(PlayerSwapItemEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer().getUuid());
            cPlayer.refreshBonuses();
            handleItemInHandEvent(event.getMainHandItem(), cPlayer);
        });

        globalEventHandler.addListener(PlayerInventoryItemChangeEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer().getUuid());
            cPlayer.refreshBonuses();
            if (Utils.isArmorSlot(event.getSlot())) {
                handleArmorEquippedEvent(event.getSlot(), event.getNewItem(), cPlayer);
            } else {
                handleItemInHandEvent(event.getPlayer().getItemInMainHand(), cPlayer);
            }
        });

        globalEventHandler.addListener(ItemDropEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer().getUuid());
            cPlayer.refreshBonuses();
            handleItemInHandEvent(event.getPlayer().getItemInMainHand(), cPlayer);
        });
    }

    private void handleArmorEquippedEvent(int slot, ItemStack armorItem, CPlayer cPlayer) {
        if (armorItem.isAir()) {
            cPlayer.removeArmor(slot);
            cPlayer.refreshBonuses();
            return;
        }

        if (!armorItem.hasTag(Identifier.getGlobalTag())) return;

        String identifier = armorItem.getTag(Identifier.getGlobalTag());
        if (identifier == null) {
            logger.error("No identifier found for item");
            return;
        }

        AbstractItem item = itemRegistry.getItem(new Identifier(identifier));
        if (item == null) {
            logger.error("No item found for identifier " + identifier);
            return;
        }

        if (item.getType().isArmor() && Utils.isArmorSlot(slot)) {
            boolean canApplyBonuses = item.getRequirements().stream().allMatch(requirement -> requirement.isMet(cPlayer));
            if (!canApplyBonuses) {
                cPlayer.removeArmor(slot);
                cPlayer.refreshBonuses();
                return;
            }

            cPlayer.addArmor(slot, item);
            cPlayer.refreshBonuses();
        }
    }

    private void handleItemInHandEvent(ItemStack heldItem, CPlayer cPlayer) {
        if (heldItem.isAir()) {
            cPlayer.removeItemInMainHandFromEquipment();
            cPlayer.refreshBonuses();
            return;
        }

        if (!heldItem.hasTag(Identifier.getGlobalTag())) return;

        String identifier = heldItem.getTag(Identifier.getGlobalTag());
        if (identifier == null) {
            logger.error("No identifier found for item");
            return;
        }

        AbstractItem item = itemRegistry.getItem(new Identifier(identifier));
        if (item == null) {
            logger.error("No item found for identifier " + identifier);
            return;
        }

        if (item.getType().isArmor()) {
            return;
        }

        boolean canApplyBonuses = item.getRequirements().stream().allMatch(requirement -> requirement.isMet(cPlayer));
        if (!canApplyBonuses) {
            cPlayer.removeItemInMainHandFromEquipment();
            cPlayer.refreshBonuses();
            return;
        }

        cPlayer.addItemInMainHandToEquipment(item);
        cPlayer.refreshBonuses();
    }
}
