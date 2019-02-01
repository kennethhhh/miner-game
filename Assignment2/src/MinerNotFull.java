import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class MinerNotFull implements AnimatingEntity,MinerPosition {
    public Entity kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public MinerNotFull(String id, Point position,
                    List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod)
    {

        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public PImage getCurrentImage(){ return this.images.get(this.imageIndex);}

    public Point position(){return this.position;}

    public Entity kind(){return this.kind;}

    public void setPosition(Point point) { this.position = point; }

    public ActivityAction createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore);
    }

    public AnimationAction createAnimationAction(int repeatCount){return new AnimationAction(this,  repeatCount);}

    public int getAnimationPeriod() { return this.animationPeriod; }

    public void nextImage() { this.imageIndex = (this.imageIndex + 1) % this.images.size(); }

    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                this.createAnimationAction(0), this.getAnimationPeriod());
    }

    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new MinerNotFull( id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public void executeActivityAction(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position,
                Ore.class);


        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position, target.position()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.position());

            if (!this.position.equals(nextPos))
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

    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            MinerFull miner = new MinerFull(this.id,
                    this.position, this.images, this.resourceLimit, this.resourceCount, this.actionPeriod, this.animationPeriod);
//            MinerFull miner = new MinerFull(this.id, this.resourceLimit,
//                    this.position, this.actionPeriod, this.animationPeriod,
//                    this.images);

            world.removeEntity(this);
            //removeEntity(world, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            //addEntity(world, miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
