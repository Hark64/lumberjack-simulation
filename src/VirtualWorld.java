import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    public static final int TIMER_ACTION_PERIOD = 100;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int WORLD_WIDTH_SCALE = 2;
    public static final int WORLD_HEIGHT_SCALE = 2;

    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public static final String IMAGE_LIST_FILE_NAME = "imagelist";
    public static final String DEFAULT_IMAGE_NAME = "background_default";
    public static final int DEFAULT_IMAGE_COLOR = 0x808080;

    public static String LOAD_FILE_NAME = "world.sav";

    public static final String FAST_FLAG = "-fast";
    public static final String FASTER_FLAG = "-faster";
    public static final String FASTEST_FLAG = "-fastest";
    public static final double FAST_SCALE = 0.5;
    public static final double FASTER_SCALE = 0.25;
    public static final double FASTEST_SCALE = 0.10;

    public static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;











    public static final int COLOR_MASK = 0xffffff;
    public static final int KEYED_IMAGE_MIN = 5;
    private static final int KEYED_RED_IDX = 2;
    private static final int KEYED_GREEN_IDX = 3;
    private static final int KEYED_BLUE_IDX = 4;

    public static final int PROPERTY_KEY = 0;

    public static final String SAPLING_KEY = "sapling";

    public static final String BGND_KEY = "background";

    public static final String OBSTACLE_KEY = "obstacle";

    public static final String DUDE_KEY = "dude";


    public static final String HOUSE_KEY = "house";


    public static final String FAIRY_KEY = "fairy";


    public static final String TREE_KEY = "tree";




    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();

            int x = 0;
            if(entity instanceof Tree){
                x = ((Tree)entity).getHealth();
            } else if (entity instanceof Sapling){
                x = ((Sapling)entity).getHealth();
            }

            System.out.println(entity.getId() + ": " + entity.getClass() + " : " + x);
        }

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(
                                                     DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            VirtualWorld.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            VirtualWorld.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if(entity instanceof Animatable) {
                ((Animatable) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    private static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }



    private static void loadImages(
            Scanner in, ImageStore imageStore, PApplet screen) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                processImageLine(imageStore.getImages(), in.nextLine(), screen);
            } catch (NumberFormatException e) {
                System.out.println(
                        String.format("Image format error on line %d",
                                lineNumber));
            }
            lineNumber++;
        }
    }

    private static void processImageLine(
            Map<String, List<PImage>> images, String line, PApplet screen) {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);

                if (attrs.length >= KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
                    setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }

    private static List<PImage> getImages(
            Map<String, List<PImage>> images, String key) {
        List<PImage> imgs = images.get(key);
        if (imgs == null) {
            imgs = new LinkedList<>();
            images.put(key, imgs);
        }
        return imgs;
    }

    /*
      Called with color for which alpha should be set and alpha value.
      setAlpha(img, color(255, 255, 255), 0));
    */
    private static void setAlpha(PImage img, int maskColor, int alpha) {
        int alphaValue = alpha << 24;
        int nonAlpha = maskColor & COLOR_MASK;
        img.format = PApplet.ARGB;
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            if ((img.pixels[i] & COLOR_MASK) == nonAlpha) {
                img.pixels[i] = alphaValue | nonAlpha;
            }
        }
        img.updatePixels();
    }

    private static void load(
            Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private static boolean processLine(
            String line, WorldModel world, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return Background.parseBackground(properties, world, imageStore);
                case DUDE_KEY:
                    return DudeFull.parseDude(properties, world, imageStore);
                case OBSTACLE_KEY:
                    return Obstacle.parseObstacle(properties, world, imageStore);
                case FAIRY_KEY:
                    return Fairy.parseFairy(properties, world, imageStore);
                case HOUSE_KEY:
                    return House.parseHouse(properties, world, imageStore);
                case TREE_KEY:
                    return Tree.parseTree(properties, world, imageStore);
                case SAPLING_KEY:
                    return Sapling.parseSapling(properties, world, imageStore);
            }
        }

        return false;
    }















//    public static Action createAnimationAction(Entity entity, int repeatCount) {
//        return new Action(ActionKind.ANIMATION, entity, null, null,
//                repeatCount);
//    }

//    public static Action createActivityAction(
//            Entity entity, WorldModel world, ImageStore imageStore)
//    {
//        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
//    }

//    public static Entity createHouse(
//            String id, Point position, List<PImage> images)
//    {
//        return new Entity(EntityKind.HOUSE, id, position, images, 0, 0, 0,
//                0, 0, 0);
//    }

//    public static Entity createObstacle(
//            String id, Point position, int animationPeriod, List<PImage> images)
//    {
//        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0,
//                animationPeriod, 0, 0);
//    }

//    public static Entity createTree(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            int health,
//            List<PImage> images)
//    {
//        return new Entity(EntityKind.TREE, id, position, images, 0, 0,
//                actionPeriod, animationPeriod, health, 0);
//    }

//    public static Entity createStump(
//            String id,
//            Point position,
//            List<PImage> images)
//    {
//        return new Entity(EntityKind.STUMP, id, position, images, 0, 0,
//                0, 0, 0, 0);
//    }

//    // health starts at 0 and builds up until ready to convert to Tree
//    public static Entity createSapling(
//            String id,
//            Point position,
//            List<PImage> images)
//    {
//        return new Entity(EntityKind.SAPLING, id, position, images, 0, 0,
//                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
//    }

//    public static Entity createFairy(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            List<PImage> images)
//    {
//        return new Entity(EntityKind.FAIRY, id, position, images, 0, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }
//
//    // need resource count, though it always starts at 0
//    public static Entity createDudeNotFull(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            int resourceLimit,
//            List<PImage> images)
//    {
//        return new Entity(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }

//    // don't technically need resource count ... full
//    public static Entity createDudeFull(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            int resourceLimit,
//            List<PImage> images) {
//        return new Entity(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }




    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
