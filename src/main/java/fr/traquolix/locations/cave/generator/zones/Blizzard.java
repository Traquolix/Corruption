package fr.traquolix.locations.cave.generator.zones;

import fr.traquolix.content.generalities.requirements.ResistColdRequirement;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.particle.Particle;

public class Blizzard extends Zone {
    public Blizzard(Instance instance) {
        super(instance);
    }

    @Override
    public void loadRequirements() {
        getRequirements().add(new ResistColdRequirement());
    }

    @Override
    public void startEvent() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent.class, (event) -> {
            Player player = event.getPlayer();
            if (player.getInstance() == instance) {
                manageParticleTask(player, Particle.SNOWFLAKE);

                if (isInZone(player.getPosition())) {
                    if (doNotHaveRequirements(player)) {
                        player.getEntityMeta().setTickFrozen(Math.min(100, player.getEntityMeta().getTickFrozen() + 10));
                        player.damage(DamageType.VOID, 0.1f);
                    } else {
                        player.getEntityMeta().setTickFrozen(Math.max(0, player.getEntityMeta().getTickFrozen() - 10));
                    }
                } else {
                    player.getEntityMeta().setTickFrozen(Math.max(0, player.getEntityMeta().getTickFrozen() - 10));
                }
            }
        });
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, (event) -> {
            if(playerTasks.containsKey(event.getPlayer())) {
                playerTasks.get(event.getPlayer()).cancel();
                playerTasks.remove(event.getPlayer());
            }
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, (event) -> {
            manageParticleTask(event.getPlayer(), Particle.SNOWFLAKE);
        });

    }

}
