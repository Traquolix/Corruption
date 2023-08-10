package fr.traquolix.events;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.PureItem;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.ItemStack;

import java.util.Optional;
import java.util.Random;

import static fr.traquolix.Main.logger;

/**
 * A class that handles custom block breaking events for players.
 * It listens for PlayerBlockBreakEvent and performs custom block breaking logic.
 */
public class PlayerCustomBlockBreakEvent {

    public PlayerCustomBlockBreakEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, this::handleBlockBreakEvent);
    }

    private void handleBlockBreakEvent(PlayerBlockBreakEvent event) {
        Random random = new Random();
        CPlayer cPlayer = PlayerRegistry.getInstance().getCPlayer(event.getPlayer().getUuid());
        assert cPlayer != null;

        if (event.getBlock().hasTag(Identifier.getGlobalTag())) {

            if (shouldCancelBreakForCreative(cPlayer, event)) {
                event.setCancelled(true);
                return;
            }

            if (shouldCancelBreakForBlockRequirements(event) || shouldCancelBreakForItemRequirements(cPlayer)) {
                event.setCancelled(true);
                return;
            }
            boolean natural = event.getBlock().getTag(Identifier.getNaturalTag());
            AbstractBlock abstractBlock = BlockRegistry.getInstance().getBlock(new Identifier(event.getBlock().getTag(Identifier.getGlobalTag())));
            if (natural) {
                cPlayer.getPlayer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 0.3f, random.nextFloat(1.85f, 2f)));
                abstractBlock.brokeNatural(cPlayer);
            } else {
                cPlayer.getPlayer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 0.3f, random.nextFloat(1.85f, 2f)));
                abstractBlock.broke(cPlayer);
            }
        } else {
            handlePureItemCheck(cPlayer, event);
        }
    }

    private boolean shouldCancelBreakForCreative(CPlayer cPlayer, PlayerBlockBreakEvent event) {
        AbstractBlock block = BlockRegistry.getInstance().getBlock(new Identifier(event.getBlock().getTag(Identifier.getGlobalTag())));
        return cPlayer.getPlayer().getGameMode() == GameMode.CREATIVE
                && !cPlayer.getEquipment().getItemInMainHand().isAir()
                && block.getIdentifier().toString().contains("indestructible");
    }

    private boolean shouldCancelBreakForBlockRequirements(PlayerBlockBreakEvent event) {
        AbstractBlock abstractBlock = BlockRegistry.getInstance().getBlock(new Identifier(event.getBlock().getTag(Identifier.getGlobalTag())));
        return abstractBlock.getRequirements().stream().anyMatch(req -> !req.isMet(PlayerRegistry.getInstance().getCPlayer(event.getPlayer().getUuid())));
    }

    private boolean shouldCancelBreakForItemRequirements(CPlayer cPlayer) {
        ItemStack heldItem = cPlayer.getItemInMainHand();
        if (heldItem.isAir()) return false;
        if (!heldItem.hasTag(Identifier.getGlobalTag())) return false;

        return getItemFromStack(heldItem)
                .map(item -> item.getRequirements().stream().anyMatch(req -> !req.isMet(cPlayer)))
                .orElse(true);
    }

    private Optional<AbstractItem> getItemFromStack(ItemStack heldItem) {
        String itemIdentifier = heldItem.getTag(Identifier.getGlobalTag());

        if (itemIdentifier == null) {
            logger.error("No identifier found for item");
            return Optional.empty();
        }

        AbstractItem item = ItemRegistry.getInstance().getItem(new Identifier(itemIdentifier));

        if (item == null) {
            logger.error("No item found for identifier " + itemIdentifier);
            return Optional.empty();
        }

        return Optional.of(item);
    }

    private void handlePureItemCheck(CPlayer cPlayer, PlayerBlockBreakEvent event) {
        if (!cPlayer.brokeBlock(event.getBlock())) {
            event.setCancelled(true);
            return;
        }
        if (cPlayer.getItemInMainHand().isAir()) return;
        Random random = new Random();

        ItemStack itemStack = PureItem.getPureItem(event.getBlock());
        cPlayer.getPlayer().getInventory().addItemStack(itemStack);
        cPlayer.getPlayer().playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 0.3f, random.nextFloat(1.85f, 2f)));
    }
}
