public class Activity implements Action {


    //entity entity changed to active entity
    private Active entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Active entity, WorldModel world, ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        this.entity.executeActivity(this.world, this.imageStore, scheduler);
    }
}
