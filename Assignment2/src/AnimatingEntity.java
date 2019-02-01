public interface AnimatingEntity extends ActivityEntity {
    public int getAnimationPeriod();
    public void nextImage();
    public AnimationAction createAnimationAction(int repeatCount);
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
