import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerFull extends MinerPosition{


    public MinerFull( String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world,imageStore),
                this.actionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0),
                this.animationPeriod());
    }

    public void executeActivityAction(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    new ActivityAction(this, world,imageStore),
                    this.actionPeriod());
        }
    }

    private boolean moveToFull(WorldModel world,
                              Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position(), target.position()))
        {
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

                world.moveEntity(this, nextPos);
                //moveEntity(world, miner, nextPos);
            }
            return false;
        }
    }

    private void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore)
    {
        MinerNotFull miner = new MinerNotFull(this.id(),
                this.position() ,this.images(), this.resourceLimit(), 0,this.actionPeriod(), this.animationPeriod()
                );

        world.removeEntity(this);
        //removeEntity(world, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


//    public static Entity createMinerFull(String id, int resourceLimit,
//                                         Point position, int actionPeriod, int animationPeriod,
//                                         List<PImage> images)
//    {
//        return new MinerFull(id, position, images,
//                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
//    }
}
