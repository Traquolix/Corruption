package fr.traquolix.commands;

import static fr.traquolix.Main.logger;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

/**
 * A command class to give the night vision effect to a player forever.
 */
public class EffectCommand extends Command {

    /**
     * Constructs a new EffectCommand.
     */
    public EffectCommand() {
        super("effect");
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

        // Add the night vision effect to the player with infinite duration and no amplifier
        player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, Integer.MAX_VALUE, Potion.ICON_FLAG));

        logger.info("[Player] - " + player.getUsername() + " got night vision forever.");
    }
}
