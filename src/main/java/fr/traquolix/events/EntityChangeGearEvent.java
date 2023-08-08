package fr.traquolix.events;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
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
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer());
            cPlayer.resetBonusStats();
            handleItemEvent(event.getPlayer().getInventory().getItemStack(event.getSlot()), cPlayer);
        });

        globalEventHandler.addListener(PlayerSwapItemEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer());
            cPlayer.resetBonusStats();
            handleItemEvent(event.getMainHandItem(), cPlayer);
        });

        globalEventHandler.addListener(PlayerInventoryItemChangeEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer());
            cPlayer.resetBonusStats();
            handleItemEvent(event.getPlayer().getItemInMainHand(), cPlayer);
        });

        globalEventHandler.addListener(ItemDropEvent.class, event -> {
            CPlayer cPlayer = playerRegistry.getCPlayer(event.getPlayer());
            cPlayer.resetBonusStats();
            handleItemEvent(event.getPlayer().getItemInMainHand(), cPlayer);
        });
    }

    private void handleItemEvent(ItemStack heldItem, CPlayer cPlayer) {
        if (heldItem.isAir()) return;

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

        boolean canApplyBonuses = item.getRequirements().stream().allMatch(requirement -> requirement.isMet(cPlayer));
        if (!canApplyBonuses) return;

        item.getBonuses().forEach(cPlayer::addBonusStatValue);
    }
}
