package fr.traquolix.content.generalities.requirements;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.inventory.PlayerInventory;

public class BlockBrokenRequirement implements Requirement, ActualizedRequirement {

    private int amountToAttain;
    private int currentAmount;
    private AbstractBlock resource;
    boolean hasStartedCheckingFlag = false;
    PlayerInventory previousInventory;
    public BlockBrokenRequirement(int amountToAttain, int currentAmount, AbstractBlock resource) {
        this.amountToAttain = amountToAttain;
        this.currentAmount = currentAmount;
        this.resource = resource;

    }

    @Override
    public boolean isMet(CPlayer player) {
        // Première fois que c'est faux, on commence à décompter.
        if (!hasStartedCheckingFlag) {
            hasStartedCheckingFlag = true;
            startCheckingResourceCollectedRequirement(player);
        }
        if (currentAmount >= amountToAttain) {
            return true;
        } else {
            return false;
        }
    }

    private void startCheckingResourceCollectedRequirement(CPlayer player) {
        MinecraftServer.getGlobalEventHandler().addListener(EventListener.builder(PlayerBlockBreakEvent.class)
                .expireWhen(event -> currentAmount >= amountToAttain) // Stop once the predicate returns true
                .handler(event -> {
                    if (event.getPlayer().getUuid().equals(player.getPlayer().getUuid())) {
                        if (event.getBlock().hasTag(Identifier.getNaturalTag()) && event.getBlock().hasTag(Identifier.getGlobalTag())) {
                            BlockRegistry.getInstance().getBlock(new Identifier(event.getBlock().getTag(Identifier.getGlobalTag())));
                            if (new Identifier(event.getBlock().getTag(Identifier.getGlobalTag())).equals(resource.getIdentifier())) {
                                currentAmount++;
                                player.sendProgressionActionBar(String.valueOf(currentAmount), String.valueOf(amountToAttain), NamedTextColor.AQUA, NamedTextColor.GRAY);
                            }
                        }
                    }
                })
                .build());
    }

    @Override
    public TextComponent getText() {
        return Component.text("→ Collect " + amountToAttain + " " + Utils.cleanName(resource.getIdentifier().getId()))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW);
    }
}
