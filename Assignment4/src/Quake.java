import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatingEntity{

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake( String id, Point position,
                   List<PImage> images, int resourceLimit, int resourceCount,
                   int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.actionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this,QUAKE_ANIMATION_REPEAT_COUNT),
                this.animationPeriod());
    }

    public void executeActivityAction(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

//    public static Entity createQuake(Point position, List<PImage> images)
//    {
//        return new Quake(QUAKE_ID, position, images,
//                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
//    }
}
