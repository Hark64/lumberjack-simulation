import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Moveable extends Active implements PathingStrategy{



    public Moveable(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, position, actionPeriod, animationPeriod, images);
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        /* Does not check withinReach.  Since only a single step is taken
         * on each call, the caller will need to check if the destination
         * has been reached.
         */
        return potentialNeighbors.apply(start)
                .filter(canPassThrough)
                .filter(pt ->
                        !pt.equals(start)
                                && !pt.equals(end)
                                && Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())
                                && Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY()))
                .limit(1)
                .collect(Collectors.toList());
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler){

        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.moveHelper(world, target, scheduler);
            return true;
        }
        else {
//            Point nextPos = this.nextPosition(world, target.getPosition());

            List<Point> path = this.nextPath(world, target.getPosition());
            for(Point nextPos: path){
                if (!this.getPosition().equals(nextPos)) {
                    Optional<Entity> occupant = world.getOccupant(nextPos);
                    if (occupant.isPresent()) {
                        scheduler.unscheduleAllEvents(occupant.get());
                    }

                    world.moveEntity(this, nextPos);
                }
                return false;
            }


            return false;
        }
    };

    //SINGLE STEP POSTION CHOOSER
    public Point nextPosition(WorldModel world, Point destPos){

//        PROJECT 3 NEXTPOSITION() CODE
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && this.nextPosHelper(world, newPos)) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && this.nextPosHelper(world, newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;

        //PROJECT 4: Single step strategy code
        PathingStrategy strategy = new SingleStepPathingStrategy();

        Predicate<Point> canPassThrough = p -> (!world.isOccupied(p) || !(this.nextPosHelper(world, p)));
        Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;
        BiPredicate<Point, Point> withinReach = (p1, p2) -> potentialNeighbors.apply(p1).anyMatch(p -> p.equals(p2));

        List<Point> path = strategy.computePath(this.getPosition(), destPos, canPassThrough, withinReach, potentialNeighbors );
        if(path.size()==0){
            return this.getPosition();
        }
        return path.get(0);
    };

    //A* path chooser
    public List<Point> nextPath(WorldModel world, Point destPos) {
        //PROJECT 4: A* strategy code
        PathingStrategy strategy = new AStarPathingStrategy();

        Predicate<Point> canPassThrough = p -> (!world.isOccupied(p) || !(this.nextPosHelper(world, p)));
        Function<Point, Stream<Point>> potentialNeighbors = PathingStrategy.CARDINAL_NEIGHBORS;
        BiPredicate<Point, Point> withinReach = (p1, p2) -> potentialNeighbors.apply(p1).anyMatch(p -> p.equals(p2));

        List<Point> path = strategy.computePath(this.getPosition(), destPos, canPassThrough, withinReach, potentialNeighbors);

        if (path.size() == 0) {
            path.add(this.getPosition());
        }
        return path;
    }

    protected abstract void moveHelper(WorldModel world, Entity target, EventScheduler scheduler);
    protected abstract boolean nextPosHelper(WorldModel world, Point newPos);
}
