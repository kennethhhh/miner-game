import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull extends MinerPosition {

    public MinerNotFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this,world,imageStore),
                this.actionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this,0), this.animationPeriod());
    }


    public void executeActivityAction(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position(),
                Ore.class);
        //System.out.println(this.resourceLimit());

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this,world,imageStore),
                    this.actionPeriod());
        }
    }

    private boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position(), target.position()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.position());

            if (!this.position().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                //Optional<Entity> occupant = getOccupant(world, nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                //moveEntity(world, miner, nextPos);
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount() >= this.resourceLimit())
        {
            MinerFull miner = new MinerFull(this.id(),
                    this.position(), this.images(), this.resourceLimit(), this.resourceCount(), this.actionPeriod(), this.animationPeriod());
//            Sasuke sasuke = new Sasuke("sasuke",
//                    this.position(), imageStore.getImageList("sasuke"), 0, 0,
//                    this.actionPeriod(), this.animationPeriod());
//            MinerFull miner = new MinerFull(this.id, this.resourceLimit,
//                    this.position, this.actionPeriod, this.animationPeriod,
//                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

//    public static Entity createMinerNotFull(String id, int resourceLimit,
//                                            Point position, int actionPeriod, int animationPeriod,
//                                            List<PImage> images)
//    {
//        return new MinerNotFull( id, position, images,
//                resourceLimit, 0, actionPeriod, animationPeriod);
//    }
}
