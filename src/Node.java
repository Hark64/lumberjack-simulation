public class Node {
    private final int x;
    private final int y;

    private final Point point;
    private final Point previous;

    private final int g;
    private final int h;
    private final int f;

    public Node(Point point, Point previous, int g, int h, int f){
        this.x = point.getX();
        this.y = point.getY();

        this.point  = point;
        this.previous = previous;

        this.g = g;
        this.h = h;
        this.f = f;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public int getG(){
        return g;
    }

    public int getH(){
        return h;
    }

    public int getF(){
        return f;
    }

    public Point getPrevious(){
        return this.previous;
    }

    public Point getPoint(){
        return this.point;
    }

}