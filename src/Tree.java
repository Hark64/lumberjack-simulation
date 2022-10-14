import processing.core.PImage;

import java.util.List;

public class Tree extends Transformer{

    private static final int TREE_NUM_PROPERTIES = 7;
    private static final int TREE_ID = 1;
    private static final int TREE_COL = 2;
    private static final int TREE_ROW = 3;
    private static final int TREE_ANIMATION_PERIOD = 4;
    private static final int TREE_ACTION_PERIOD = 5;
    private static final int TREE_HEALTH = 6;

    public Tree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, health, images);
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    @Override
    protected void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

    @Override
    protected Entity transformHelper(ImageStore imageStore){
        return new Stump(this.getId(),
                this.getPosition(),
                imageStore.getImageList( STUMP_KEY));
    }

    @Override
    protected boolean transformSaplingHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        return false;
    }

    public static boolean parseTree(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Tree entity = new Tree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList( TREE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }
}
