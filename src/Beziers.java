import java.util.ArrayList;
import java.util.List;

public class Beziers {

    List<Point> pointList;
    List<Point> putPoints = new ArrayList<>();

    public List<Point> getPutPoints() {
        return putPoints;
    }

    public Beziers(List<Point> pointList) {
        this.pointList = pointList;
    }

    public void solution() {

        if (pointList.size() > 2) {
            for (double t = 0; t <= 1; t += 0.01) {
                Point p = new Point(0, 0);
                calculation(p, pointList, t);
                putPoints.add(p);
            }
        }
    }

    private void calculation(Point p, List<Point> points, double t) {
        BinomialCoefficient binomialCoefficient = new BinomialCoefficient(0);
        for (int i = 0; i < points.size(); i++) {
            binomialCoefficient.binomialSolution(points.size() - 1, i);
            double c = binomialCoefficient.getCoefficient();
            p.setX(p.getX() + c * Math.pow(t, i) * (Math.pow(1 - t, points.size() - (i + 1))) * points.get(i).getX());
            p.setY(p.getY() + c * Math.pow(t, i) * (Math.pow(1 - t, points.size() - (i + 1))) * points.get(i).getY());
            
        }
    }

}
