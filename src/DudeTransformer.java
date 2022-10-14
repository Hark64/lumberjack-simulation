import processing.core.PImage;

import java.util.List;

public abstract class DudeTransformer extends Moveable{

    private int resourceLimit;

    public DudeTransformer(String id, Point position, int actionPeriod, int animationPeriod, int resourceLimit, List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, images);
        this.resourceLimit=resourceLimit;
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Entity entity = this.transformHelper(imageStore);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity(entity);

        //this might cause some issues
        if(entity instanceof Animatable) {
            ((Animatable)entity).scheduleActions(scheduler, world, imageStore);
        }

        return false;
    }

    protected int getResourceLimit() {
        return resourceLimit;
    }

    protected abstract Entity transformHelper(ImageStore imageStore);
}
