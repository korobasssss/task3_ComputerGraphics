package cs.vsu.ruKorobeynikova_A_V.Forms;

import cs.vsu.ruKorobeynikova_A_V.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class BSpline {

    private final int[] matrix1 = {-1, 3, -3, 1};
    private final int[] matrix2 = {3, -6, 0, 4};
    private final int[] matrix3 = {-3, 3, 3, 1};
    private final int[] matrix4 = {1, 0, 0, 0};

    List<Coordinates> pointList;
    List<Coordinates> addPoints = new ArrayList<>();

    public BSpline(List<Coordinates> pointList) {
        this.pointList = pointList;
    }

    public void calculation() {
        for (int i = 1; i < pointList.size() - 2; i++) {
            for (double t = 0; t <= 1; t += 0.01) {
                double mult1 = matrixMult(t, matrix1);
                double mult2 = matrixMult(t, matrix2);
                double mult3 = matrixMult(t, matrix3);
                double mult4 = matrixMult(t, matrix4);

                double x0 = pointList.get(i - 1).getX() * mult1 + pointList.get(i).getX() * mult2
                        + pointList.get(i + 1).getX() * mult3 + pointList.get(i + 2).getX() * mult4;
                double y0 = pointList.get(i - 1).getY() * mult1 + pointList.get(i).getY() * mult2
                        + pointList.get(i + 1).getY() * mult3 + pointList.get(i + 2).getY() * mult4;

                addPoints.add(new Coordinates(1/6.0 * x0, 1/6.0 * y0));
            }
        }
    }

    private double matrixMult(double t, int[] matrix){
        return Math.pow(t, 3) * matrix[0] + Math.pow(t, 2) * matrix[1] + t * matrix[2] + matrix[3];
    }

    public List<Coordinates> getAddPoints() {
        return addPoints;
    }
}
