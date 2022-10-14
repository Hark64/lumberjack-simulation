import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity
{
    //below will be in interface

    public static final String SAPLING_KEY = "sapling";

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    public static final String OBSTACLE_KEY = "obstacle";

    public static final String DUDE_KEY = "dude";


    public static final String HOUSE_KEY = "house";


    public static final String FAIRY_KEY = "fairy";

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(
            String id,
            Point position,
            List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
    }


    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex);
    }

    public void nextImage(){
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    };

    protected String getId(){
        return id;
    }
    protected Point getPosition(){return position;}
    protected void setPosition(Point p){
        this.position = p;
    }
    protected List<PImage> getImages(){return this.images;}

}
