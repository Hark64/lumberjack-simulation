import processing.core.PImage;

import java.util.List;

public abstract class Active extends Animatable {

    private int actionPeriod;

    public Active(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, position, animationPeriod, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod(){
        return this.actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
