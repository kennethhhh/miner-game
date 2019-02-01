import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

final class Entity
{
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public static final String ORE_ID_PREFIX = "ore -- ";
    public static final int ORE_CORRUPT_MIN = 20000;
    public static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";

    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;

    public static final String QUAKE_KEY = "quake";

    public static final Random rand = new Random();


    public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public Action createActivityAction(WorldModel world,
                                              ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
    }

    public Action createAnimationAction(int repeatCount)
    {
        return new Action(ActionKind.ANIMATION, this, null, null, repeatCount);
    }

    public int getAnimationPeriod()
    {
        switch (this.kind)
        {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage()
    {
        return this.images.get(this.imageIndex);
    }

    public void executeVeinActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);
        //Optional<Point> openPt = Functions.findOpenAround(world, this.position);

        if (openPt.isPresent())
        {
            Entity ore = Functions.createOre(ORE_ID_PREFIX + this.id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            //getImageList(imageStore, ORE_KEY));
            world.addEntity(ore);
            //addEntity(world, ore);
            scheduler.scheduleActions(ore, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    public void executeOreActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Functions.createOreBlob(this.id + BLOB_ID_SUFFIX,
                pos, this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));
        //getImageList(imageStore, BLOB_KEY));

        world.addEntity(blob);
        //addEntity(world, blob);
        scheduler.scheduleActions(blob, world, imageStore);
    }

    public void executeQuakeActivity(WorldModel world,
                                            ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
        //removeEntity(world, this);
    }

    public void executeOreBlobActivity(WorldModel world,
                                       ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> blobTarget = world.findNearest(this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

//        Optional<Entity> blobTarget = findNearest(world,
//                this.position, EntityKind.VEIN);
//        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent())
        {
            Point tgtPos = blobTarget.get().position;

            if (moveToOreBlob(world, blobTarget.get(), scheduler))
            {
                Entity quake = Functions.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));
                //getImageList(imageStore, QUAKE_KEY));

                world.addEntity(quake);
//                addEntity(world, quake);
                nextPeriod += this.actionPeriod;
                scheduler.scheduleActions(quake, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }


    public Point nextPositionOreBlob(WorldModel world,
                                            Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);
//        Optional<Entity> occupant = getOccupant(world, newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);
            //occupant = getOccupant(world, newPos);


            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public boolean moveToOreBlob(WorldModel world,
                                        Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position, target.position))
        {
            world.removeEntity(target);
            //removeEntity(world, target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPositionOreBlob(world, target.position);

            if (!this.position.equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
//                Optional<Entity> occupant = getOccupant(world, nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
                //moveEntity(world, this, nextPos);
            }
            return false;
        }
    }

    public void executeMinerFullActivity(WorldModel world,
                                                ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                EntityKind.BLACKSMITH);
//        Optional<Entity> fullTarget = findNearest(world, this.position,
//                EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position,
                EntityKind.ORE);
//        Optional<Entity> notFullTarget = findNearest(world, this.position,
//                EntityKind.ORE);

        if (!notFullTarget.isPresent() ||
                !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transformNotFull(WorldModel world,
                                           EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Entity miner = Functions.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod, this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            //removeEntity(world, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            //addEntity(world, miner);
            scheduler.scheduleActions(miner, world, imageStore);

            return true;
        }

        return false;
    }

    public void transformFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = Functions.createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod, this.animationPeriod,
                this.images);

        world.removeEntity(this);
        //removeEntity(world, this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        scheduler.scheduleActions(miner, world, imageStore);
    }

    public boolean moveToNotFull(WorldModel world,
                                        Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position, target.position))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            //removeEntity(world, target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.position);

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

    public boolean moveToFull(WorldModel world,
                                     Entity target, EventScheduler scheduler)
    {
        if (Functions.adjacent(this.position, target.position))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPositionMiner(world, target.position);

            if (!this.position.equals(nextPos))
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

    public Point nextPositionMiner(WorldModel world,
                                          Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        //if (horiz == 0 || isOccupied(world, newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            //if (vert == 0 || isOccupied(world, newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }


}
