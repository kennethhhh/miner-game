import processing.core.PImage;

public interface Entity {
    public PImage getCurrentImage();
    public Entity kind();
    public Point position();
    public void setPosition(Point point);
}

//    public PImage getCurrentImage(){ return this.images.get(this.imageIndex);}
//
//    public Point position(){return this.position;}
//
//    public Entity kind(){return this.kind;}
//
//    public void setPosition(Point point) { this.position = point; }
