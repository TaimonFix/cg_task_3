import java.awt.*;

public class Curve {
    private Graphics2D g2;
    private double max;
    private double min;

    public Curve(Graphics2D g2, double max, double min) {
        this.g2 = g2;
        this.max = max;
        this.min = min;
    }

    public void drawCurve(int x0, int y0, int x1, int y1, int range, int pointSize) {
    double dx, dy, ranges, xr, yr, x, y, n;
        boolean flag = false;
    dx = x1 - x0;
    dy = y1 - y0;
    ranges = Math.max(Math.abs(dx), Math.abs(dy));
    xr = ranges / dx;
    yr = ranges / dy;
    x = x1;
    y = y1;

        for(n = 1; n <= ranges; n++, x+=xr, y+=yr) {

            if (x == x1 && y == y1 || n == ranges) {
                paint((int) Math.round(x), (int) Math.round(y), pointSize);
            }

            if ((range == 1 || (n % range < range/2))) {
                paint((int) x, (int) y, 2);
                if (y > max + ranges || y < min - ranges) {
                    if (!flag) {
                        flag = true;
                    } else {
                        break;
                    }
                }
            }
        }


    }

    private void paint(int x, int y, int pointSize) {
        int half = pointSize / 2;
        g2.fillOval(x - half, y - half, pointSize, pointSize);
    }
}
