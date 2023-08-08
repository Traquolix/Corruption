package fr.traquolix.commands;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

/**
 * A command class to give a specific item to a player.
 */
public class GetItemCommand extends Command {

    /**
     * Constructs a new GetItemCommand.
     */
    public GetItemCommand() {
        super("item");
        setCondition(Conditions::playerOnly);

        var itemIdArgument = ArgumentType.String("item_id");

        // Add a syntax for the command
        addSyntax((sender, context) -> {
            final String itemId = context.get(itemIdArgument);
            Player player = (Player) sender;

            // Get the AbstractItem from the ItemRegistry based on the provided item identifier
            AbstractItem abstractItem = ItemRegistry.getInstance().getItem(new Identifier("item", itemId));
            if (abstractItem == null) {
                player.sendMessage(Component.text("Item not found with identifier: " + itemId));
                return;
            }

            // Create an ItemStack of the AbstractItem and give it to the player
            ItemStack itemStack = abstractItem.buildItemStack();
            player.getInventory().addItemStack(itemStack);
        }, itemIdArgument);
    }
}
