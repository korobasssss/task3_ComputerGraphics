package cs.vsu.ruKorobeynikova_A_V.Forms;

import cs.vsu.ruKorobeynikova_A_V.BinomialCoefficient;
import cs.vsu.ruKorobeynikova_A_V.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class Bezier {

    List<Coordinates> pointList;
    List<Coordinates> addPoints = new ArrayList<>();

    public Bezier(List<Coordinates> pointList) {
        this.pointList = pointList;
    }

    public void calculation() {

        if (pointList.size() > 2) {
            for (double t = 0; t <= 1; t += 0.01) {
                Coordinates coordBXAndBY = new Coordinates(0, 0);
                calc(coordBXAndBY, pointList, t);
                addPoints.add(coordBXAndBY);
            }
        }
    }

    private void calc(Coordinates BXAndBY, List<Coordinates> points, double t) {
        BinomialCoefficient binomialCoefficient = new BinomialCoefficient(0);
        for (int i = 0; i < points.size(); i++) {
            binomialCoefficient.binomialCalc(points.size() - 1, i);
            double b = binomialCoefficient.getCoef();
            BXAndBY.setX(BXAndBY.getX() + b
                    * Math.pow(t, i)
                    * (Math.pow(1 - t, points.size() - (i + 1)))
                    * points.get(i).getX());
            BXAndBY.setY(BXAndBY.getY() + b
                    * Math.pow(t, i) * (Math.pow(1 - t, points.size() - (i + 1)))
                    * points.get(i).getY());
        }
    }

    public List<Coordinates> getAddPoints() {
        return addPoints;
    }
}

