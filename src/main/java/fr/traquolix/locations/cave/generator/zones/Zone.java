package fr.traquolix.locations.cave.generator.zones;

import fr.traquolix.content.generalities.requirements.Requirement;
import fr.traquolix.player.PlayerRegistry;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.*;

public abstract class Zone {
    Instance instance;
    Map<Player, Task> playerTasks = new HashMap<>();
    @Getter
    Set<Requirement> requirements = new HashSet<>();
    Point topCorner;
    Point bottomCorner;
    private final int PARTICLE_RADIUS = 5;
    Zone(Instance instance) {
        this.instance = instance;
        loadRequirements();
    }

    abstract public void loadRequirements();

    public void generateZone(Point topCorner, Point bottomCorner) {
        this.topCorner = topCorner;
        this.bottomCorner = bottomCorner;
        startEvent();
    }

    public abstract void startEvent();

    void manageParticleTask(Player player, Particle particle) {
        if (isNearZone(topCorner, bottomCorner, player) && doNotHaveRequirements(player)) {
            if (!playerTasks.containsKey(player)) {
                startParticleTask(player, particle);
            }
        } else if (playerTasks.containsKey(player)) {
            playerTasks.get(player).cancel();
            playerTasks.remove(player);
        }
    }

    void startParticleTask(Player player, Particle particle) {
        Task newTask = MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (isNearZone(topCorner, bottomCorner, player) && doNotHaveRequirements(player)) {
                generateParticlesAroundPlayer(player, particle);
            } else {
                playerTasks.get(player).cancel();
                playerTasks.remove(player);
            }
        }).repeat(TaskSchedule.tick(5)).schedule();

        playerTasks.put(player, newTask);
    }

    void generateParticlesAroundPlayer(Player player, Particle particle) {
        Point position = player.getPosition();
        double minX = position.x() - PARTICLE_RADIUS;
        double maxX = position.x() + PARTICLE_RADIUS;
        double minY = position.y() - PARTICLE_RADIUS;
        double maxY = position.y() + PARTICLE_RADIUS;
        double minZ = position.z() - PARTICLE_RADIUS;
        double maxZ = position.z() + PARTICLE_RADIUS;

        for (double x = minX; x <= maxX; x++) {
            for (double y = minY; y <= maxY; y++) {
                for (double z = minZ; z <= maxZ; z++) {
                    if (isInZone(new Pos(x, y, z))) {
                        generateParticle(particle, x, y, z, player);
                    }
                }
            }
        }
    }

    void generateParticle(Particle particle, double x, double y, double z, Player player) {
        Random random = new Random();
        ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                particle,
                false,
                x, y, z,
                random.nextFloat(-0.5f,0.5f),
                random.nextFloat(-0.5f,0.5f),
                random.nextFloat(-0.5f,0.5f),
                0f, 1, null);
        player.sendPacket(particlePacket);
    }


    private boolean isNearZone(Point topCorner, Point bottomCorner, Player player) {
        return true;
    }

    public boolean doNotHaveRequirements(Player player) {
        return getRequirements().stream().anyMatch(req -> !req.isMet(PlayerRegistry.getInstance().getCPlayer(player.getUuid())));
    }

    public boolean isInZone(Point point) {
        if (point.x() >= topCorner.x() && point.x() <= bottomCorner.x()) {
            if (point.y() >= topCorner.y() && point.y() <= bottomCorner.y()) {
                return point.z() >= topCorner.z() && point.z() <= bottomCorner.z();
            }
        }
        return false;
    }
}
