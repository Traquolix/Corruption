package fr.traquolix.commands;

import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;

public class QuestStepCommand extends Command {

    public QuestStepCommand() {
        super("step");
        setCondition(Conditions::playerOnly);

        // Define the argument
        var idArg = ArgumentType.Integer("id");

        // Set a default executor for when the command is incomplete or invalid
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /step [quest_id]");
        });

        // Callback for the main quest.json command structure
        addSyntax((sender, context) -> {
            CPlayer player = PlayerRegistry.getInstance().getCPlayer(((Player) sender).getUuid());

            int questId = context.get(idArg);
            player.getCurrentQuests().forEach((id, quest) -> {
                if (id == questId) {
                    quest.step(player);
                }
            });

        }, idArg);
    }
}
