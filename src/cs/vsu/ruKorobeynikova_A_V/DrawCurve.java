package cs.vsu.ruKorobeynikova_A_V;

import java.awt.*;
import java.util.List;

public class DrawCurve {
    private Graphics2D g2;
    private double max;
    private double min;

    public DrawCurve(Graphics2D g2, double max, double min) {
        this.g2 = g2;
        this.max = max;
        this.min = min;
    }
    public void drawCurve(int x1, int y1, int x2, int y2, int step, int pointSize){
        double dx,dy,steps,x,y,k;
        double xc,yc;
        boolean breakFlag = false;
        dx=x2-x1;
        dy=y2-y1;
        steps = Math.max(Math.abs(dx), Math.abs(dy));
        xc=(dx/steps);
        yc=(dy/steps);
        x=x1;
        y=y1;
        for(k=1;k<=steps;k++, x+=xc, y+=yc)
        {
            if (x == x1 && y == y1 || k == steps) {
                plot((int) Math.round(x), (int) Math.round(y), pointSize);
            }
            if ((step == 1 || (k % step < step/2))) {
                plot((int) x, (int) y, 2);
                if (y > max + steps || y < min - steps) {
                    if (!breakFlag) {
                        breakFlag = true;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void plot(int x, int y, int pointSize) {
        int shift = pointSize/2;
        g2.fillOval(x - shift, y - shift, pointSize, pointSize);
    }
}
