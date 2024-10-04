package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.GenerateData;
import org.example.config.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class CubicSpline {
    private final GenerateData data;
    double[] a, b, c, d, f;
    double eps = 1e-10;

    // set up
    public void fillMatrix(){
        int size = data.getPoints().size();
        var points = data.getPoints();
        a = new double[size-1];
        b = new double[size-1];
        c = new double[size-1];
        d = new double[size-1];
        f = new double[size-2];

        IntStream.range(0, size-2).parallel()
                .forEach(i ->
                {
                    double current = points.get(i+1).getX() - points.get(i).getX();
                    double next = points.get(i+2).getX() - points.get(i+1).getX();
                    b[i] = 2.0 * (current + next);
                    a[i+1] =  current;
                    d[i] = next;
                    f[i] = 3.0 * ((points.get(i+2).getY() - points.get(i+1).getY()) / next -
                            (points.get(i+1).getY() - points.get(i).getY()) / current);
                });
        IntStream.range(1, size-2).forEach(
                i -> {
                    b[i] -= a[i] / b[i-1] * d[i-1];
                    f[i] -= a[i] / b[i-1] * f[i-1];
                }
        );
        c[size-2] = f[size-3] / b[size-3];
        for (int i= size-3; i>0; i--){
            c[i] = (f[i-1] - c[i+1] * d[i-1]) / b[i-1];
        }
        c[0] = 0.0;
        IntStream.range(0, size-2).parallel().forEach(
                i -> {
                    double current = points.get(i+1).getX() - points.get(i).getX();
                    a[i] = points.get(i).getY();
                    b[i] = (points.get(i+1).getY() - points.get(i).getY()) / current -
                            (c[i+1] + 2.0 * c[i]) * current / 3.0;
                    d[i] = (c[i+1] - c[i]) / (current * 3.0);
                }
        );
        double current = points.get(size-1).getX() - points.get(size-2).getX();
        a[size-2] = points.get(size-2).getY();
        b[size-2] = (points.get(size-1).getY() - points.get(size-2).getY()) / current -
                2.0 * c[size-2] * current / 3.0;
        d[size-2] = (-1.0) * c[size - 2] / current / 3.0;
    }

    // Вычисление с помощью сплайна
    public ArrayList<Double> result(Point point) {
        var res = new ArrayList<Double>(3);
        var points = data.getPoints();
        for (int i=0; i< points.size()-2; i++)
            {
                if (point.getX() > points.get(i).getX() &&
                    point.getX() < points.get(i+1).getX() ||
                    Math.abs(point.getX() - points.get(i).getX()) < eps ||
                    Math.abs(point.getX() - points.get(i+1).getX()) < eps) {
                        double diff = (point.getX() - points.get(i).getX());
                        // f(x)
                        res.add(0, a[i] + b[i] * diff + c[i] * Math.pow(diff, 2) + d[i] * Math.pow(diff, 3));
                        // f'(x)
                        res.add(1, b[i] + 2.0 * c[i] * diff + 3.0 * d[i] * Math.pow(diff, 2));
                        // f''(x)
                        res.add(2, 2.0 * c[i] + 6.0 * d[i] * diff);
                        return res;
                }
            }
        return null;
    }

}
