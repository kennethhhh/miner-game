import processing.core.PImage;

import java.util.List;

public abstract class AnimatingEntity extends ActivityEntity {
    public AnimatingEntity(String id, Point position,
                          List<PImage> images, int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod){
        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod);
    }

    public void nextImage() { this.imageIndex = (this.imageIndex + 1) % this.images.size(); }
}

//    public AnimationAction createAnimationAction(int repeatCount){return new AnimationAction(this,  repeatCount);}
//
//public int getAnimationPeriod()
//{
//    switch (this.kind)
//    {
//        case MINER_FULL:
//        case MINER_NOT_FULL:
//        case ORE_BLOB:
//        case QUAKE:
//            return this.animationPeriod;
//        default:
//            throw new UnsupportedOperationException(
//                    String.format("getAnimationPeriod not supported for %s",
//                            this.kind));
//    }
//}
//
//    public void nextImage()
//    {
//        this.imageIndex = (this.imageIndex + 1) % this.images.size();
//    }
