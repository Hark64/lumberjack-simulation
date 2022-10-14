import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//BROOOOOOOOOOOOOOOOOOOOO WHAT'S WRONG WITH MOVETO IM GONNA CRY
//AAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

public class Fairy extends Moveable implements PathingStrategy{

    private static final int FAIRY_NUM_PROPERTIES = 6;
    private static final int FAIRY_ID = 1;
    private static final int FAIRY_COL = 2;
    private static final int FAIRY_ROW = 3;
    private static final int FAIRY_ANIMATION_PERIOD = 4;
    private static final int FAIRY_ACTION_PERIOD = 5;

    public Fairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, images);
    }

    @Override
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                //this might need EntityKInd.STUMP
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                Sapling sapling = new Sapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList( SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    protected void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    public void moveHelper(WorldModel world, Entity target, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
    }

    @Override
    protected boolean nextPosHelper(WorldModel world, Point newPos){
        return true;
    }

    public static boolean parseFairy(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            Fairy entity = new Fairy(properties[FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList( FAIRY_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }
}
