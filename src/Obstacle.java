import processing.core.PImage;

import java.util.List;

public class Obstacle extends Animatable{


    private static final int OBSTACLE_NUM_PROPERTIES = 5;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;
    private static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public Obstacle(String id, Point position, int animationPeriod, List<PImage> images){
        super(id, position, animationPeriod, images);
    }



    @Override
    protected void scheduleHelp(EventScheduler scheduler, WorldModel world, ImageStore imageStore){}

    public static boolean parseObstacle(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Obstacle entity = new Obstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(
                            OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }
}
