package fr.traquolix.events;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;

import static fr.traquolix.Main.logger;

/**
 * A class that handles player interactions with gear (items) in the game.
 * It listens for two types of events: PlayerUseItemEvent and PlayerUseItemOnBlockEvent.
 */
public class PlayerUseGearEvent {

    public PlayerUseGearEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerUseItemEvent.class, this::handleUseItemEvent);
        //globalEventHandler.addListener(PlayerUseItemOnBlockEvent.class, this::handleUseItemOnBlockEvent);
    }

    private void handleUseItemEvent(PlayerUseItemEvent event) {
        Player player = event.getPlayer();

        if (event.getHand().equals(Player.Hand.OFF)) return;

        handlePlayerGearInteraction(player, event.getItemStack());
    }

    private void handleUseItemOnBlockEvent(PlayerUseItemOnBlockEvent event) {
        Player player = event.getPlayer();

        if (event.getHand().equals(Player.Hand.OFF)) return;

        // Check if the held item is the main hand item
        if (!event.getItemStack().equals(player.getInventory().getItemInMainHand())) return;

        handlePlayerGearInteraction(player, event.getItemStack());
    }

    private void handlePlayerGearInteraction(Player player, ItemStack heldItem) {
        if (heldItem.isAir()) return;

        // Check if the item has a global identifier tag
        if (!heldItem.hasTag(Identifier.getGlobalTag())) return;

        String identifier = heldItem.getTag(Identifier.getGlobalTag());
        if (identifier == null) {
            logger.error("No identifier found for item");
            return;
        }

        AbstractItem item = retrieveItemFromRegistry(identifier);
        if (item == null) {
            logger.error("No item found for identifier " + identifier);
            return;
        }

        CPlayer cplayer = PlayerRegistry.getInstance().getCPlayer(player.getUuid());
        if (cplayer == null || !playerMeetsItemRequirements(cplayer, item)) return;

        item.use(cplayer, item);
    }

    private AbstractItem retrieveItemFromRegistry(String identifier) {
        return ItemRegistry.getInstance().getItem(new Identifier(identifier));
    }

    private boolean playerMeetsItemRequirements(CPlayer cplayer, AbstractItem item) {
        return item.getRequirements().stream().allMatch(requirement -> requirement.isMet(cplayer));
    }
}
