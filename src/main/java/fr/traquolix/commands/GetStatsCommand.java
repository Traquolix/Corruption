package fr.traquolix.commands;

import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import fr.traquolix.stats.Stat;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A command class to get the current stats of a player.
 */
public class GetStatsCommand extends Command {

    /**
     * Constructs a new GetStatsCommand.
     */
    public GetStatsCommand() {
        super("stats");
        addSyntax(this::execute);
        setCondition(Conditions::playerOnly);
    }

    /**
     * Executes the command to display the current stats of a player.
     *
     * @param commandSender The sender of the command.
     * @param commandContext The command context.
     */
    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        Player player = (Player) commandSender;
        CPlayer cPlayer = PlayerRegistry.getInstance().getCPlayer(player);
        cPlayer.getCurrentStats().forEach((stat, value) -> {
            player.sendMessage(Component.text(Utils.capitalizeFirstLetter(stat.getIdentifier().getId()) + ": " + value));
        });
    }
}
