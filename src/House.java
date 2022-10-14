import processing.core.PImage;

import java.util.List;

public class House extends Entity{

    private static final int HOUSE_NUM_PROPERTIES = 4;
    private static final int HOUSE_ID = 1;
    private static final int HOUSE_COL = 2;
    private static final int HOUSE_ROW = 3;

    public House(String id, Point position, List<PImage> images){
        super(id, position, images);
    }

    public static boolean parseHouse(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            House entity = new House(properties[HOUSE_ID], pt,
                    imageStore.getImageList(
                            HOUSE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }

}
