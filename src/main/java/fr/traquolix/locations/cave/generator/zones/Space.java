package fr.traquolix.locations.cave.generator.zones;

import fr.traquolix.content.generalities.requirements.SkillLevelRequirement;
import fr.traquolix.skills.Skill;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.particle.Particle;

public class Space extends Zone {
    public Space(Instance instance) {
        super(instance);
    }

    @Override
    public void loadRequirements() {
        getRequirements().add(new SkillLevelRequirement(Skill.MINING, 4));
    }

    @Override
    public void startEvent() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerMoveEvent.class, (event) -> {
            if (event.getPlayer().getInstance() == instance) {
                instance.getPlayers().forEach((player -> manageParticleTask(player, Particle.ASH)));
                if (isInZone(event.getPlayer().getPosition())) {
                    if (doNotHaveRequirements(event.getPlayer())) {
                        event.getPlayer().damage(DamageType.OUT_OF_WORLD, 0.2f);
                    }
                }
            }
        });
    }
}
