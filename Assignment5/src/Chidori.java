import processing.core.PImage;

import java.util.List;

public class Chidori extends AnimatingEntity{
    public Chidori(String id, Point position,
                   List<PImage> images, int resourceLimit, int resourceCount,
                   int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.actionPeriod());
        scheduler.scheduleEvent(this,
                new AnimationAction(this,10),
                this.animationPeriod());
    }

    public void executeActivityAction(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
