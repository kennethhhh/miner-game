import processing.core.PImage;

import java.util.List;

public interface Entity {
    public String id();
    public Entity kind();
    public List<PImage> images();
    public int imageIndex();
    public Point position();
    public int resourceLimit();
    public int resourceCount();
    public int actionPeriod();
    public int animationPeriod();
    public void setPosition(Point point);
    public PImage getCurrentImage();
}

//    public PImage getCurrentImage(){ return this.images.get(this.imageIndex);}
//
//    public Point position(){return this.position;}
//
//    public Entity kind(){return this.kind;}
//
//    public void setPosition(Point point) { this.position = point; }
