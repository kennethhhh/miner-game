import processing.core.PImage;

import java.util.List;

public abstract class MinerPosition extends AnimatingEntity{
    public MinerPosition(String id, Point position,List<PImage> images, int resourceLimit, int resourceCount,
                         int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }
    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
//    public Point nextPositionMiner(WorldModel world,
//                                   Point destPos)
//    {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz,
//                this.position.y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        //if (horiz == 0 || isOccupied(world, newPos))
//        {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x,
//                    this.position.y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            //if (vert == 0 || isOccupied(world, newPos))
//            {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
//    }