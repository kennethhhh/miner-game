import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends AbstractEntity{
    public ActivityEntity(String id, Point position,
                          List<PImage> images, int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }
    //public ActivityAction createActivityAction(WorldModel world,ImageStore imageStore);
    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    protected abstract void executeActivityAction(WorldModel world,
                                                  ImageStore imageStore, EventScheduler scheduler);

}

//    public void scheduleActions(EventScheduler scheduler,
//                                       WorldModel world, ImageStore imageStore)
//    {
//        switch (entity.kind)
//        {
//            case MINER_FULL:
//                this.scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                this.scheduleEvent(entity, entity.createAnimationAction(0),
//                        entity.getAnimationPeriod());
//                break;
//
//            case MINER_NOT_FULL:
//                this.scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                this.scheduleEvent(entity,
//                        entity.createAnimationAction(0), entity.getAnimationPeriod());
//                break;
//
//            case ORE:
//                scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                break;
//
//            case ORE_BLOB:
//                scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                scheduleEvent(entity,
//                        entity.createAnimationAction(0), entity.getAnimationPeriod());
//                break;
//
//            case QUAKE:
//                scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                scheduleEvent(entity,
//                        entity.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
//                        entity.getAnimationPeriod());
//                break;
//
//            case VEIN:
//                scheduleEvent(entity,
//                        entity.createActivityAction(world, imageStore),
//                        entity.actionPeriod);
//                break;
//
//            default:
//        }
//    }
