import processing.core.PImage;

import java.util.List;

public abstract class Animatable extends Entity {

    private int animationPeriod;

    public Animatable(String id, Point position, int animationPeriod, List<PImage> images){
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){
        return this.animationPeriod;
    };

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        this.scheduleHelp(scheduler, world, imageStore);

        scheduler.scheduleEvent(this,
                new Animation(this, 0),
                this.getAnimationPeriod());
    };

    protected abstract void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
