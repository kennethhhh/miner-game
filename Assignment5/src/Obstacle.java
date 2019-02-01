import processing.core.PImage;

import java.util.List;

public class Obstacle extends AbstractEntity {

    public Obstacle( String id, Point position,
                       List<PImage> images, int resourceLimit, int resourceCount,
                       int actionPeriod, int animationPeriod)
    {
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

//
//    public static Entity createObstacle(String id, Point position,
//                                        List<PImage> images)
//    {
//        return new Obstacle(id, position, images,
//                0, 0, 0, 0);
//    }
}
