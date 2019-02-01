public class ActivityAction implements ActionKind {
    public ActionKind kind;
    public ActivityEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public ActivityAction(ActivityEntity entity, WorldModel world, ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler)
    {
        entity.executeActivityAction(this.world,this.imageStore,scheduler);
        //ORIGINAL
        //executeActivityAction(scheduler);
    }

//    public void executeActivityAction(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    //======ORIGINAL=======
//    public void executeActivityAction(EventScheduler scheduler)
//    {
//        switch  (this.entity.kind)
//        {
//            case MINER_FULL:
//                this.entity.executeMinerFullActivity(this.world,
//                        this.imageStore, scheduler);
//                break;
//
//            case MINER_NOT_FULL:
//                this.entity.executeMinerNotFullActivity(this.world,
//                        this.imageStore, scheduler);
//                break;
//
//            case ORE:
//                this.entity.executeOreActivity(this.world, this.imageStore,
//                        scheduler);
//                break;
//
//            case ORE_BLOB:
//                this.entity.executeOreBlobActivity(this.world,
//                        this.imageStore, scheduler);
//                break;
//
//            case QUAKE:
//                this.entity.executeQuakeActivity(this.world, this.imageStore,
//                        scheduler);
//                break;
//
//            case VEIN:
//                this.entity.executeVeinActivity(this.world, this.imageStore,
//                        scheduler);
//                break;
//
//            default:
//                throw new UnsupportedOperationException(
//                        String.format("executeActivityAction not supported for %s",
//                                this.entity.kind));
//        }
//    }
}
