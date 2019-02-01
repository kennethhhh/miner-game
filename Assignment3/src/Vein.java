import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein extends ActivityEntity {

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final String ORE_KEY = "ore";

    private static final Random rand = new Random();


    public Vein(String id, Point position,
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

    }


    public void executeActivityAction(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position());
        //Optional<Point> openPt = Functions.findOpenAround(world, this.position);

        if (openPt.isPresent())
        {
            Ore ore = new Ore(ORE_ID_PREFIX + this.id(),
                    openPt.get(),imageStore.getImageList(ORE_KEY), 0,0,ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),0
                    );
            //getImageList(imageStore, ORE_KEY));
            world.addEntity(ore);
            //addEntity(world, ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world,imageStore),
                this.actionPeriod());
    }

//    public static Entity createVein(String id, Point position, int actionPeriod,
//                                    List<PImage> images)
//    {
//        return new Vein( id, position, images, 0, 0,
//                actionPeriod, 0);
//    }
}
