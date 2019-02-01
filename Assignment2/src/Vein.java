import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Vein implements ActivityEntity {
    public Entity kind;
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

    public static final Random rand = new Random();


    public Vein(String id, Point position,
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

    public static Entity createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Vein( id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public void executeActivityAction(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);
        //Optional<Point> openPt = Functions.findOpenAround(world, this.position);

        if (openPt.isPresent())
        {
            Ore ore = new Ore(ORE_ID_PREFIX + this.id,
                    openPt.get(),imageStore.getImageList(ORE_KEY), 0,0,ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),0
                    );
            //getImageList(imageStore, ORE_KEY));
            world.addEntity(ore);
            //addEntity(world, ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }
}
