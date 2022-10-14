import processing.core.PImage;

import java.util.List;

public abstract class Transformer extends Active{

    private int health;

    public Transformer(String id, Point position, int actionPeriod, int animationPeriod, int health, List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, images);
        this.health = health;
    }

    public int getHealth(){return health;}
    public void setHealth(int health){this.health = health;}

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        if (this.health <= 0) {
            Entity entity = this.transformHelper(imageStore);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(entity);

//            //this might cause some issue
//            if (entity instanceof Animatable) {
//                ((Animatable) entity).scheduleActions(scheduler, world, imageStore);
//            }

            return true;
        }

        return this.transformSaplingHelper(world, scheduler, imageStore);
    }

    protected abstract Entity transformHelper(ImageStore imageStore);

    //probably could move to just be in sapling
    protected abstract boolean transformSaplingHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
