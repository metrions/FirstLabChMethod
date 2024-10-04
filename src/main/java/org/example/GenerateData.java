package org.example;

import lombok.Getter;
import org.example.config.Point;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class GenerateData {
    @Value("${data.a}")
    double a;
    @Value("${data.b}")
    double b;
    @Value("${data.h}")
    double h;

    // Сетка от a до b с шагом h
    ArrayList<Point> points = new ArrayList<>();
    // диапозон от a до b
    private final ArrayList<Double> data;
    // f(x)
    private final ArrayList<Double> dataf = new ArrayList<>();
    // f'(x)
    private final ArrayList<Double> dataf1 = new ArrayList<>();
    // f''(x)
    private final ArrayList<Double> dataf2 = new ArrayList<>();

    public GenerateData(ArrayList<Double> data){
        this.data = data;
    }

    // Возврат списка [f(x), f'(x), f''(x)]
    public ArrayList<ArrayList<Double>> getF(){
        ArrayList<ArrayList<Double>> F = new ArrayList<>(data.size());
        IntStream.range(0, data.size()).forEach(x -> {
            F.add(new ArrayList<>(Arrays.asList(dataf.get(x), dataf1.get(x), dataf2.get(x))));
        });
        return F;
    }

    private double f(double x){
        return 1.0 / x + 0.1;
    }
    private double f1(double x){
        return -1.0 / Math.pow(x, 2);
    }
    private double f2(double x){
        return 2.0 / Math.pow(x, 3);
    }

    public List<Double> getData(){
        return data;
    }

    @PostConstruct
    public void init(){
        points = IntStream
                .range(0, (int) ((b - a) / h + 2))
                .mapToObj(x -> new Point(a + h * x, f(a + h * x)))
                .collect(Collectors.toCollection(ArrayList::new));
        data.forEach(
                x -> {
                    dataf.add(f(x));
                    dataf1.add(f1(x));
                    dataf2.add(f2(x));
                }
        );

    }

    // Вспомогательная генерация
    public List<Double> generateData(){
        ArrayList<Double> data = new ArrayList<>();
        for (int i=0; i<10; i++){
            double v = Math.random() % b;
            if (v < a){
                v += a;
            }
            data.add(v);
        }
        return data.stream().sorted().collect(Collectors.toList());
    }
}
