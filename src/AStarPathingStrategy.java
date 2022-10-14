import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class AStarPathingStrategy
        implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new ArrayList<>();
        HashMap<Point, Node> tracker = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(10, new NodeComparator());
        List<Node> closedList = new ArrayList<>();

        Node current = new Node(start, null,0, Math.abs(end.getX() - start.getX())+Math.abs(end.getY() - start.getY()), Math.abs(end.getX() - start.getX())+Math.abs(end.getY() - start.getY()));

        tracker.put(start, current);
        queue.add(current);

        while ( queue.size()>0){
            current = queue.remove();
            if(withinReach.test(current.getPoint(),end)){

                while(current != null){
                    path.add(current.getPoint());
                    current = tracker.get(current.getPrevious());

                }

                path.remove(start);
                Collections.reverse(path);
                System.out.println("path: " +path.size()+ " tracker: " + tracker.size() + " closed: " + closedList.size() + " open: " + queue.size());
                return path;
            }

            List<Point> neighbors = potentialNeighbors.apply(current.getPoint()).filter(canPassThrough).filter(n -> !(closedList.contains(tracker.get(n)))).collect(Collectors.toList());


            for(Point neighbor: neighbors){
                int g = (Math.abs(neighbor.getX() - start.getX())+Math.abs(neighbor.getY() - start.getY()));
                int h = (Math.abs(neighbor.getX() - end.getX())+Math.abs(neighbor.getY() - end.getY()));
                int f = g + h;
                Node newOpen = new Node(neighbor, current.getPoint(), g, h, f);

                if (queue.contains(tracker.get(neighbor))){
                    if(tracker.get(neighbor).getG()>g){
                        queue.remove(tracker.get(neighbor));
                        queue.add(newOpen);
                        tracker.replace(neighbor, newOpen);
                    }
                } else {
                    tracker.put(neighbor, newOpen);
                    queue.add(newOpen);
                }
            }

            closedList.add(current);
        }



        return path;
    }

}