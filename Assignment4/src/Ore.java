import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class Ore  extends ActivityEntity{

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private static final Random rand = new Random();


    public Ore(String id, Point position,
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
    }

    public void executeActivityAction(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(this.id() + BLOB_ID_SUFFIX,
                pos,imageStore.getImageList(BLOB_KEY),
                0,0,this.actionPeriod() / BLOB_PERIOD_SCALE,BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN));
        //getImageList(imageStore, BLOB_KEY));

        world.addEntity(blob);
        //addEntity(world, blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

//    public static Entity createOre(String id, Point position, int actionPeriod,
//                                   List<PImage> images)
//    {
//        return new Ore(id, position, images, 0, 0,
//                actionPeriod, 0);
//    }
}
