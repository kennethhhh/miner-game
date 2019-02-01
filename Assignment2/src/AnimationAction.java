public class AnimationAction implements ActionKind {
    public ActionKind kind;
    public AnimatingEntity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public AnimationAction(AnimatingEntity entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        executeAnimationAction(scheduler);
    }

    public void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
