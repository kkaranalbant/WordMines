//package model.cost;
//
//public class Cost {
//    private double price;
//    private double time;
//    private double distance;
//
//    public Cost(double price, double time, double distance) {
//        this.price = price;
//        this.time = time;
//        this.distance = distance;
//    }
//

//}


package model.cost;

public class Cost {
    private Double price;
    private Double time;
    private Double distance;

    public Cost(Double price, Double time, Double distance) {
        this.price = price;
        this.time = time;
        this.distance = distance;
    }

    public void add(Cost other) {
        this.price += other.price;
        this.time += other.time;
        this.distance += other.distance;
    }

    public double getPrice() {
        return price;
    }

    public double getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }

}