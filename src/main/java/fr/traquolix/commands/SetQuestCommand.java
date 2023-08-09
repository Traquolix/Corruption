package fr.traquolix.commands;

import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestRegistry;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;

public class SetQuestCommand extends Command {

    public SetQuestCommand() {
        super("quest");
        setCondition(Conditions::playerOnly);

        // Define the arguments
        var id = ArgumentType.Integer("id");

        // Set a default executor for when the command is incomplete or invalid
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /quest [quest_id]");
        });

        // Callback for the main quest.json command structure
        addSyntax((sender, context) -> {
            int questId = context.get(id);
            CPlayer player = PlayerRegistry.getInstance().getCPlayer(((Player) sender).getUuid());

            AbstractQuest quest = QuestRegistry.getInstance().getQuest(questId);
            if (quest == null) {
                player.sendMessage(Component.text("Quest with id " + questId + " not found"));
                return;
            }
            quest.start(player);
        }, id);
    }
}
