package fr.traquolix.commands;

import fr.traquolix.GUI.rewardStash.RewardStashGUI;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import static fr.traquolix.Main.logger;

/**
 * A command class to give the night vision effect to a player forever.
 */
public class RewardStashCommand extends Command {

    /**
     * Constructs a new EffectCommand.
     */
    public RewardStashCommand() {
        super("rewardstash");
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
        CPlayer cPlayer = PlayerRegistry.getInstance().getCPlayer(player.getUuid());

        RewardStashGUI rewardStashGUI = new RewardStashGUI();
        rewardStashGUI.open(cPlayer);
    }
}
