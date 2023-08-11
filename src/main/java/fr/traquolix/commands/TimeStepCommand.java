package fr.traquolix.commands;

import fr.traquolix.time.TimeManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.traquolix.Main.logger;

/**
 * A command class to give the night vision effect to a player forever.
 */
public class TimeStepCommand extends Command {

    /**
     * Constructs a new EffectCommand.
     */
    public TimeStepCommand() {
        super("timestep");
        addSyntax(this::execute);
        setCondition(Conditions::playerOnly);
    }

    /**
     * Executes the effect command.
     *
     * @param commandSender  The command sender who executed the command.
     * @param commandContext The context of the command, containing the arguments and sender information.
     */
    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        TimeManager.getInstance().stepTime();
        Player player = (Player) commandSender;
        logger.info("[Player] - " + player.getUsername() + " stepped the time to : " + TimeManager.getInstance().getCurrentTime());
    }
}
