package fr.traquolix.locations.cave.generator.structures.planets;

import fr.traquolix.utils.Utils;
import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;

import java.util.*;

import static fr.traquolix.Main.logger;

@Getter
public class MoonStructure extends PlanetStructure {
    final Block outerShell = Block.GRAY_CONCRETE_POWDER;
    final Block core = Block.AMETHYST_BLOCK;
    final Block coreDecoration = Block.AMETHYST_CLUSTER;
    final List<Point> craterPoints = new ArrayList<>();
    final Set<Point> corePoints = new HashSet<>();
    public MoonStructure(InstanceContainer instance) {
        super(instance, 15, 15, 15);
    }

    @Override
    public void generateStructure(Point toBeGeneratedAt) {
        if (toBeGeneratedAt == null) {
            logger.error("No suitable location found to generate " + this.getClass().getSimpleName() + " at.");
            return;
        }

        Set<Point> mainMoonPoints = generateComponentsPoints(toBeGeneratedAt);
        Random random = new Random();
        Point craterSource = getCraterPoints().get(random.nextInt(getCraterPoints().size()-1));
        Set<Point> crater = generateComponentsPoints(craterSource,
                15 + random.nextInt(5),
                15 + random.nextInt(5),
                15 + random.nextInt(5));



        for (Point point : mainMoonPoints) {
            if (!crater.contains(point)) {
                getBatch().setBlock(point, getOuterShell());
            }
        }

        for (Point point : getCorePoints()) {
            if (!crater.contains(point)) {
                getBatch().setBlock(point, getCore());
            } else if (crater.contains(point)
                    && mainMoonPoints.contains(point)
                    && !crater.contains(point.add(0, -1, 0))){
                if (random.nextInt(5) == 0) {
                    getBatch().setBlock(point, getCoreDecoration());
                }
            }
        }

        getBatch().apply(this.getInstance(), null);
    }

    protected Set<Point> generateComponentsPoints(Point toBeGeneratedAt, double radiusX, double radiusY, double radiusZ) {

        double tempRadiusX = getRadiusX();
        double tempRadiusY = getRadiusY();
        double tempRadiusZ = getRadiusZ();

        setRadiusX(radiusX);
        setRadiusY(radiusY);
        setRadiusZ(radiusZ);

        Set<Point> craterDigger = this.generateComponentsPoints(toBeGeneratedAt);

        setRadiusX(tempRadiusX);
        setRadiusY(tempRadiusY);
        setRadiusZ(tempRadiusZ);

        return craterDigger;
    }

    @Override
    protected Set<Point> generateComponentsPoints(Point center) {
        Set<Point> points = new HashSet<>();

        setRadiusX(getRadiusX()+0.5);
        setRadiusY(getRadiusY()+0.5);
        setRadiusZ(getRadiusZ()+0.5);

        final double invRadiusX = 1 / getRadiusX();
        final double invRadiusY = 1 / getRadiusY();
        final double invRadiusZ = 1 / getRadiusZ();

        final int ceilRadiusX = (int) Math.ceil(getRadiusX());
        final int ceilRadiusY = (int) Math.ceil(getRadiusY());
        final int ceilRadiusZ = (int) Math.ceil(getRadiusZ());

        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = Utils.lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break;
                    }

                    Pos pos = new Pos(center.blockX(), center.blockY(), center.blockZ());


                    if (isOnEdge(x, y, z, getRadiusX(), getRadiusY(), getRadiusZ()) && y>=5 && y<=getRadiusY()-2) {
                        getCraterPoints().add(pos.add(x-6, y, z-6));
                        getCraterPoints().add(pos.add(x-6, y, -z-6));
                        getCraterPoints().add(pos.add(-x-6, y, z-6));
                        getCraterPoints().add(pos.add(-x-6, y, -z-6));
                    }

                    if (!isOnEdge(x, y, z, getRadiusX(), getRadiusY(), getRadiusZ())) {
                        getCorePoints().add(pos.add(x, y, z));
                        getCorePoints().add(pos.add(x, y, -z));
                        getCorePoints().add(pos.add(x, -y, z));
                        getCorePoints().add(pos.add(x, -y, -z));
                        getCorePoints().add(pos.add(-x, y, z));
                        getCorePoints().add(pos.add(-x, y, -z));
                        getCorePoints().add(pos.add(-x, -y, z));
                        getCorePoints().add(pos.add(-x, -y, -z));
                    }


                    Pos[] positions = {
                            pos.add(x, y, z),
                            pos.add(x, y, -z),
                            pos.add(x, -y, z),
                            pos.add(x, -y, -z),
                            pos.add(-x, y, z),
                            pos.add(-x, y, -z),
                            pos.add(-x, -y, z),
                            pos.add(-x, -y, -z)
                    };

                    // Set the blocks in the block batch
                    points.addAll(Arrays.asList(positions));

                }
            }
        }
        return points;
    }
}