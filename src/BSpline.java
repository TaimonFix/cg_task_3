import java.util.ArrayList;
import java.util.List;

public class BSpline {

    private final int[] matrix1 = {-1, 3, -3, 1};
    private final int[] matrix2 = {3, -6, 0, 4};
    private final int[] matrix3 = {-3, 3, 3, 1};
    private final int[] matrix4 = {1, 0, 0, 0};

    List<Point> pointList;
    List<Point> putPoints = new ArrayList<>();

    public BSpline(List<Point> pointList) {
        this.pointList = pointList;
    }

    public List<Point> getPutPoints() {
        return putPoints;
    }

    private double multiplyMatrix(int[] matrix, double t) {
        return Math.pow(t, 3) * matrix[0] + Math.pow(t, 2) * matrix[1] + t * matrix[2] + matrix[3];
    }
    public void solution() {

        for (int i = 1; i < pointList.size() - 2; i++) {
            for (double j = 0; j < 1; j += 0.01) {
                double res1 = multiplyMatrix(matrix1, j);
                double res2 = multiplyMatrix(matrix2, j);
                double res3 = multiplyMatrix(matrix3, j);
                double res4 = multiplyMatrix(matrix4, j);

                double x0 = pointList.get(i - 1).getX() * res1
                          + pointList.get(i).getX() * res2
                          + pointList.get(i + 1).getX() * res3
                          + pointList.get(i + 2).getX() * res4;

                double y0 = pointList.get(i - 1).getY() * res1
                          + pointList.get(i).getY() * res2
                          + pointList.get(i + 1).getY() * res3
                          + pointList.get(i + 2).getY() * res4;

                putPoints.add(new Point(1/6.0 * x0, 1/6.0 * y0));
            }
        }
    }

}
