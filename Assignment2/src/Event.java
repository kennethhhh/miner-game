final class Event
{
   public ActionKind action;
   public long time;
   public Entity entity;

   public Event(ActionKind action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }
}
