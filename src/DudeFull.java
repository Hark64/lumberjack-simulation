import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeFull extends DudeTransformer{

    private static final int DUDE_NUM_PROPERTIES = 7;
    private static final int DUDE_ID = 1;
    private static final int DUDE_COL = 2;
    private static final int DUDE_ROW = 3;
    private static final int DUDE_LIMIT = 4;
    private static final int DUDE_ACTION_PERIOD = 5;
    private static final int DUDE_ANIMATION_PERIOD = 6;

    public DudeFull(String id,
                     Point position,
                     int actionPeriod,
                     int animationPeriod,
                     int resourceLimit,
                     List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, resourceLimit, images);

    }



    @Override
    protected void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world,
                fullTarget.get(), scheduler))
        {
            this.transform(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }



    protected Entity transformHelper(ImageStore imageStore){
        return new DudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImages());
    }

    @Override
    protected void moveHelper(WorldModel world, Entity target, EventScheduler scheduler){};


    protected boolean nextPosHelper(WorldModel world, Point newPos){
        return !(world.getOccupancyCell(newPos) instanceof Stump);}

    public static boolean parseDude(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            DudeNotFull entity = new DudeNotFull(properties[DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    imageStore.getImageList( DUDE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

}
