public class Animation implements Action{

    //changed entity to animatable
    private Animatable entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;


    public Animation(Animatable entity, int repeatCount){
        this.entity = entity;
        this.world = null;
        this.imageStore = null;
        this.repeatCount = repeatCount;
    }

    @Override
    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent( this.entity, new Animation(this.entity, Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }


}
