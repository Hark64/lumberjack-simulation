import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends DudeTransformer{

    private int resourceCount;

    public DudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);
        this.resourceCount=0;
    }


    @Override
    protected void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    protected Entity transformHelper(ImageStore imageStore){
        return new DudeFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImages());
    }

    @Override
    protected void moveHelper(WorldModel world, Entity target, EventScheduler scheduler){
        this.resourceCount += 1;
        int x = ((Transformer)target).getHealth()-1;
        ((Transformer)target).setHealth(x);
    }

    @Override
    protected boolean nextPosHelper(WorldModel world, Point newPos){
        return !(world.getOccupancyCell(newPos) instanceof Stump);}

}
