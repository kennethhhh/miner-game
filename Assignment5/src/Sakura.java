import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Sakura extends AnimatingEntity{
    private PathingStrategy strategy = new AStarPathingStrategy();

    private static final String QUAKE_KEY = "quake";
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Sakura(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new ActivityAction(this,world,imageStore),
                this.actionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this,0), this.animationPeriod());
    }

    public void executeActivityAction(WorldModel world,
                                                  ImageStore imageStore, EventScheduler scheduler){
        Optional<Entity> blobTarget = world.findNearest(this.position(), OreBlob.class);
        Optional<Entity> veinTarget = world.findNearest(this.position(), Vein.class);
        long nextPeriod = this.actionPeriod();

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position();

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Quake quake = new Quake(QUAKE_ID,tgtPos,
                        imageStore.getImageList(QUAKE_KEY),0,0,QUAKE_ACTION_PERIOD,QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += this.actionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        if (veinTarget.isPresent())
        {
            Point tgtPos = veinTarget.get().position();

            if (moveToOreBlob(world, veinTarget.get(), scheduler))
            {
                Quake quake = new Quake(QUAKE_ID,tgtPos,
                        imageStore.getImageList(QUAKE_KEY),0,0,QUAKE_ACTION_PERIOD,QUAKE_ANIMATION_PERIOD);

                world.addEntity(quake);
                nextPeriod += this.actionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this,world,imageStore),
                nextPeriod);

    }

    private Point nextPositionSakura(WorldModel world,
                                      Point destPos) {
                int horiz = Integer.signum(destPos.x - this.position().x);
        Point newPos = new Point(this.position().x + horiz,
                this.position().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().kind() instanceof Ore)))
        {
            int vert = Integer.signum(destPos.y - this.position().y);
            newPos = new Point(this.position().x, this.position().y + vert);
            occupant = world.getOccupant(newPos);


            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().kind() instanceof Ore)))
            {
                newPos = this.position();
            }
        }

        return newPos;

//        Predicate<Point> canPassThrough = pass -> world.withinBounds(pass) && !world.isOccupied(pass);
//        BiPredicate<Point, Point> withinReach = (p1, p2) -> Functions.adjacent(p1, p2);
//
//        List<Point> points = strategy.computePath(this.position(), destPos, canPassThrough,
//                withinReach, PathingStrategy.CARDINAL_NEIGHBORS);
//
//        if (points.isEmpty())
//            return this.position();
//
//        return points.get(1);
    }

    public boolean moveToOreBlob(WorldModel world,
                                 Entity target, EventScheduler scheduler){
        if (Functions.adjacent(this.position(), target.position()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionSakura(world, target.position());

            if (!this.position().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
