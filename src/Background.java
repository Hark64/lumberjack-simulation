import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;

    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;


    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public PImage getCurrentImage() {
        if (this instanceof Background) {
            return ((Background) this).getImages().get(
                    ((Background) this).getImageIndex());
        } else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            this));
        }
    }

    public static boolean parseBackground(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground( pt,
                    new Background(id, imageStore.getImageList( id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public List<PImage> getImages(){
        return images;
    }

    public int getImageIndex(){
        return imageIndex;
    }



}
