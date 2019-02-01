import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore  implements ActivityEntity{
    public Entity kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public static final String BLOB_KEY = "blob";
    public static final String BLOB_ID_SUFFIX = " -- blob";
    public static final int BLOB_PERIOD_SCALE = 4;
    public static final int BLOB_ANIMATION_MIN = 50;
    public static final int BLOB_ANIMATION_MAX = 150;

    public static final Random rand = new Random();


    public Ore(String id, Point position,
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

    public void scheduleActions(EventScheduler scheduler,
                                       WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    public static Entity createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Ore(id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public void executeActivityAction(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(this.id + BLOB_ID_SUFFIX,
                pos,imageStore.getImageList(BLOB_KEY),
                0,0,this.actionPeriod / BLOB_PERIOD_SCALE,BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN));
        //getImageList(imageStore, BLOB_KEY));

        world.addEntity(blob);
        //addEntity(world, blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

}
