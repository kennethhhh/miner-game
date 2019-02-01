import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class MinerPosition extends AnimatingEntity{
    private PathingStrategy strategy = new AStarPathingStrategy();

    public MinerPosition(String id, Point position,List<PImage> images, int resourceLimit, int resourceCount,
                         int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }
    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz,
//                this.position.y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x,
//                    this.position.y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
        Predicate<Point> canPassThrough = pass -> world.withinBounds(pass) && !world.isOccupied(pass);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> Functions.adjacent(p1,p2);

        List<Point> points = strategy.computePath(this.position(), destPos, canPassThrough,
                withinReach, PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.isEmpty())
            return this.position();

        return points.get(1);
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