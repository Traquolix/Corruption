package fr.traquolix.content.generalities.requirements;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;

public class ItemCollectedRequirement implements Requirement, ActualizedRequirement {

    private int amountToAttain;
    private int currentAmount;
    private AbstractItem resource;
    boolean hasStartedCheckingFlag = false;
    public ItemCollectedRequirement(int amountToAttain, int currentAmount, AbstractItem resource) {
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
        MinecraftServer.getGlobalEventHandler().addListener(EventListener.builder(PlayerInventoryItemChangeEvent.class)
                .expireWhen(event -> currentAmount >= amountToAttain) // Stop once the predicate returns true
                .handler(event -> {
                    if (event.getPlayer().getUuid().equals(player.getPlayer().getUuid())) {
                        if (event.getNewItem().hasTag(Identifier.getGlobalTag())) {
                            ItemRegistry.getInstance().getItem(new Identifier(event.getNewItem().getTag(Identifier.getGlobalTag())));
                            if (new Identifier(event.getNewItem().getTag(Identifier.getGlobalTag())).equals(resource.getIdentifier())) {
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
