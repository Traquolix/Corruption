package fr.traquolix.commands;

import fr.traquolix.time.TimeManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import org.jetbrains.annotations.NotNull;

/**
 * A command class to give the night vision effect to a player forever.
 */
public class TellTimeCommand extends Command {

    /**
     * Constructs a new EffectCommand.
     */
    public TellTimeCommand() {
        super("telltime");
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
        commandSender.sendMessage("Time is currently set to " + TimeManager.getInstance().getCurrentTime() + ".");
    }
}
