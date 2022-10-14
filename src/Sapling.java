import processing.core.PImage;

import java.util.List;

public class Sapling extends Transformer{

    private static final String SAPLING_KEY = "sapling";
    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_NUM_PROPERTIES = 4;
    private static final int SAPLING_ID = 1;
    private static final int SAPLING_COL = 2;
    private static final int SAPLING_ROW = 3;
    private static final int SAPLING_HEALTH = 4;

    private int healthLimit;

    public Sapling(
            String id,
            Point position,
            List<PImage> images){
        super(id, position, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, images);
        this.healthLimit=SAPLING_HEALTH_LIMIT;
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        this.setHealth(this.getHealth()+1);
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
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
    protected Entity transformHelper(ImageStore imageStore) {
        return new Stump(this.getId(),
                this.getPosition(),
                imageStore.getImageList( STUMP_KEY));
    }

    @Override
    protected boolean transformSaplingHelper(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        if (this.getHealth() >= this.healthLimit)
        {
            Tree tree = new Tree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList( TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public static boolean parseSapling(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            Sapling entity = new Sapling(id, pt, imageStore.getImageList( SAPLING_KEY));
            entity.setHealth(health);
            world.tryAddEntity(entity);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }
}


