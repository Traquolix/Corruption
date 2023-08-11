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
public class RingedPlanetStructure extends PlanetStructure {
    final Block outerShell = Block.BLUE_CONCRETE_POWDER;
    final Block spotBlock = Block.BLUE_CONCRETE;
    final List<Point> spotPoints = new ArrayList<>();
    final Block ringBlock = Block.ICE;
    public RingedPlanetStructure(InstanceContainer instance) {
        super(instance, 15, 15, 15);
    }

    @Override
    public void generateStructure(Point toBeGeneratedAt) {
        if (toBeGeneratedAt == null) {
            logger.error("No suitable location found to generate " + this.getClass().getSimpleName() + " at.");
            return;
        }

        Set<Point> mainPlanetPoints = generateComponentsPoints(toBeGeneratedAt);
        Random random = new Random();
        Point spotSource = spotPoints.get(random.nextInt(spotPoints.size()-1));
        Set<Point> ring = generateRing(toBeGeneratedAt, 0.5, 0, 5, 12);
        Set<Point> spot = generateComponentsPoints(spotSource,
                3 + random.nextInt(5),
                3 + random.nextInt(5),
                3 + random.nextInt(5));

        for (Point point : mainPlanetPoints) {
            if (!spot.contains(point)) {
                getBatch().setBlock(point, getOuterShell());
            }
        }

        for (Point point : spot) {
            if (mainPlanetPoints.contains(point)) {
                getBatch().setBlock(point, getSpotBlock());
            }
        }

        for (Point point : ring) {
            getBatch().setBlock(point, getRingBlock());
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
                        getSpotPoints().add(pos.add(x, y, z));
                        getSpotPoints().add(pos.add(x, y, -z));
                        getSpotPoints().add(pos.add(-x, y, z));
                        getSpotPoints().add(pos.add(-x, y, -z));
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


    protected Set<Point> generateRing(Point center, double slope, double yOffset, int start, int end) {
        Set<Point> points = new HashSet<>();


        Random random = new Random();
        random.nextBoolean();
        int ringOrientation = random.nextInt(4);

        for (int i = start; i <= end; i++) {


                double radiusXTemp = getRadiusX() + 0.5 + i;
                double radiusYTemp = getRadiusY() + 0.5 + i;
                double radiusZTemp = getRadiusZ() + 0.5 + i;

                final double invRadiusX = 1 / radiusXTemp;
                final double invRadiusY = 1 / radiusYTemp;
                final double invRadiusZ = 1 / radiusZTemp;

                final int ceilRadiusX = (int) Math.ceil(radiusXTemp);
                final int ceilRadiusY = (int) Math.ceil(radiusYTemp);
                final int ceilRadiusZ = (int) Math.ceil(radiusZTemp);

                double nextXn = 0;
                forX:
                for (int x = 0; x <= ceilRadiusX; ++x) {
                    final double xn = nextXn;
                    nextXn = (x + 1) * invRadiusX;
                    double nextYn = 0;
                    forY:
                    for (int y = 0; y <= ceilRadiusY; ++y) {
                        final double yn = nextYn;
                        nextYn = (y + 1) * invRadiusY;
                        double nextZn = 0;
                        forZ:
                        for (int z = 0; z <= ceilRadiusZ; ++z) {
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

                            switch(ringOrientation) {
                                case 0 -> {
                                    double newY = yOffset - slope * Math.abs(z);
                                    if (isOnEdge(x, (int) newY, z, radiusXTemp, radiusYTemp, radiusZTemp)) {
                                        Pos[] positionsA = {
                                                pos.add(x, newY, z),
                                                pos.add(-x, newY, z),
                                                pos.add(x, -newY, -z),
                                                pos.add(-x, -newY, -z)
                                        };
                                        points.addAll(List.of(positionsA));
                                    }
                                }
                                case 1 -> {
                                    double newY = yOffset - slope * Math.abs(x);
                                    if (isOnEdge(x, (int) newY, z, radiusXTemp, radiusYTemp, radiusZTemp)) {
                                        Pos[] positionsB = {
                                                pos.add(-x, newY, z),
                                                pos.add(x, -newY, z),
                                                pos.add(-x, newY, -z),
                                                pos.add(x, -newY, -z)
                                        };
                                        points.addAll(List.of(positionsB));
                                    }
                                }
                                case 2 -> {
                                    double newY = yOffset - slope * Math.abs(z);
                                    if (isOnEdge(x, (int) newY, z, radiusXTemp, radiusYTemp, radiusZTemp)) {
                                        Pos[] positionsB = {
                                                pos.add(x, newY, -z),
                                                pos.add(-x, newY, -z),
                                                pos.add(x, -newY, z),
                                                pos.add(-x, -newY, z)
                                        };
                                        points.addAll(List.of(positionsB));
                                    }
                                }
                                case 3 -> {
                                    double newY = yOffset - slope * Math.abs(x);
                                    if (isOnEdge(x, (int) newY, z, radiusXTemp, radiusYTemp, radiusZTemp)) {
                                        Pos[] positionsA = {
                                                pos.add(-x, newY, z),
                                                pos.add(x, -newY, z),
                                                pos.add(-x, newY, -z),
                                                pos.add(x, -newY, -z)
                                        };
                                        points.addAll(List.of(positionsA));
                                    }
                                }
                            }

                        }
                    }
                }
        }

    return points;
    }
}