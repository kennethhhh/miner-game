import processing.core.PImage;

import java.util.List;

public abstract class AbstractEntity implements Entity{
    public String id;
    public Entity kind;
    public List<PImage> images;
    public int imageIndex;
    public Point position;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;

    public AbstractEntity(String id, Point position,
                          List<PImage> images, int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public String id(){return this.id;}
    public Entity kind(){return this.kind;}
    public List<PImage> images(){return this.images;}
    public int imageIndex(){return imageIndex;}
    public Point position(){return this.position;}
    public int resourceLimit(){return this.resourceLimit;}
    public int resourceCount() {return this.resourceCount;}
    public int actionPeriod(){return this.actionPeriod;}
    public int animationPeriod(){return this.animationPeriod;}

    public PImage getCurrentImage(){ return this.images.get(this.imageIndex);}
    public void setPosition(Point point) {this.position = point;}

    //public void setResourceCount(int r){this.resourceCount=r;}
}

//    public PImage getCurrentImage(){ return this.images.get(this.imageIndex);}
//
//    public Point position(){return this.position;}
//
//    public Entity kind(){return this.kind;}
//
//    public void setPosition(Point point) { this.position = point; }
