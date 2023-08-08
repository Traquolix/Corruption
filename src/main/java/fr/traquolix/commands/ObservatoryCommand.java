package fr.traquolix.commands;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import static fr.traquolix.Main.logger;

/**
 * A command class to give the night vision effect to a player forever.
 */
public class ObservatoryCommand extends Command {

    public static Point observatoryPosition;

    /**
     * Constructs a new EffectCommand.
     */
    public ObservatoryCommand() {
        super("observatory");
        addSyntax(this::execute);
        setCondition(Conditions::playerOnly);
    }

    /**
     * Executes the effect command.
     *
     * @param commandSender The command sender who executed the command.
     * @param commandContext The context of the command, containing the arguments and sender information.
     */
    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        Player player = (Player) commandSender;
        if (observatoryPosition == null) {
            logger.warn("Observatory Position is null");
        } else {
            Pos pos = new Pos(observatoryPosition.x(), observatoryPosition.y(), observatoryPosition.z());
            player.teleport(pos.withY(pos.y() + 25));
        }

    }
}
