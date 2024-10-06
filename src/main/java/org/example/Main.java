package org.example;

import lombok.RequiredArgsConstructor;

import org.example.config.Point;
import org.example.service.CubicSpline;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@SpringBootApplication
public class Main {
    private final CubicSpline cubicSpline;
    // Для формирования сеток и для точек
    private final GenerateData generateData;

    public static void main(String[] args) {
        // Спринг для управления зависимостями
        ApplicationContext context = SpringApplication.run(Main.class, args);
        Main main = context.getBean(Main.class);

        // Set up spline
        main.cubicSpline.fillMatrix();
        // Какие данные должны быть list = [f(x), f'(x), f''(x)]
        var array = main.generateData.getF();
        for (var i : array){
            System.out.println(i.get(0) + " " + i.get(1) + " " + i.get(2));
        }

        System.out.println("\nВычисление через сплайн");
        var data = main.generateData.getData();
        var splineArray = new ArrayList<ArrayList<Double>>();
        for (var i : data){
            var temp = main.cubicSpline.result(new Point(i, 0.0));
            splineArray.add(temp);
            System.out.println(temp.get(0) + " " + temp.get(1) + " " + temp.get(2));
        }

        System.out.println("\nПогрешность");
        IntStream.range(0, array.size()).forEach(x ->{
            double result1 = Math.abs(splineArray.get(x).get(0) - array.get(x).get(0));
            double result2 = Math.abs(splineArray.get(x).get(1) - array.get(x).get(1));
            System.out.println(result1 + " " + result2);
        });

        double[] points = {0.25, 0.5, 1};
        System.out.println("\nТочки из варианта Через Сплайн");
        for (int i=0; i<3; i++){
            var temp = main.cubicSpline.result(new Point(points[i], 0.0));
            System.out.println(points[i] + " " +temp.get(1));
        }
        System.out.println("\nТочки из варианта Через формулы");
        for (int i=0; i<2; i++){
            var temp = (main.generateData.f(points[i+1]) - main.generateData.f(points[i])) / (points[i+1] - points[i]);
            System.out.println(temp);
        }
        var temp = (main.generateData.f(points[2]) - main.generateData.f(points[1])) / (points[2] - points[1]);
        System.out.println(temp);
    }
}