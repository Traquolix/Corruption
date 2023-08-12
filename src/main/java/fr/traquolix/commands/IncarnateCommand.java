package fr.traquolix.commands;

import fr.traquolix.mercenaries.AbstractMercenary;
import fr.traquolix.mercenaries.MercenaryRegistry;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.condition.Conditions;
import net.minestom.server.entity.Player;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;

import java.util.Objects;

import static fr.traquolix.Main.logger;

/**
 * A command class to give a specific item to a player.
 */
public class IncarnateCommand extends Command {

    /**
     * Constructs a new GetItemCommand.
     */
    public IncarnateCommand() {
        super("incarnate");
        setCondition(Conditions::playerOnly);

        var mercenaryIdArgument = ArgumentType.String("incarnate");

        // Add a syntax for the command
        addSyntax((sender, context) -> {
            final String mercernaryid = context.get(mercenaryIdArgument);
            Player player = (Player) sender;

            if (Objects.equals(mercernaryid, "-d")) {
                CPlayer cplayer = PlayerRegistry.getInstance().getCPlayer(player.getUuid());
                cplayer.removeMercenary();
                player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, Integer.MAX_VALUE, Potion.ICON_FLAG));
                return;
            }

            logger.info("[Mercenary] - Setting player (" + player.getUuid() + ") to : " + mercernaryid);
            CPlayer cplayer = PlayerRegistry.getInstance().getCPlayer(player.getUuid());
            AbstractMercenary mercenaryX = cplayer.getMercenaryCamp().getMercenaryById(mercernaryid);
            cplayer.setMercenary(mercenaryX);
            player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, Integer.MAX_VALUE, Potion.ICON_FLAG));

        }, mercenaryIdArgument);
    }
}
