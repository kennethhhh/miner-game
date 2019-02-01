import processing.core.PImage;

import java.util.List;

public class Obstacle implements Entity {
    public Entity kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public Obstacle( String id, Point position,
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

    public static Entity createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle(id, position, images,
                0, 0, 0, 0);
    }
}
