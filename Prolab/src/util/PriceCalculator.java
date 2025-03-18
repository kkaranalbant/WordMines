package util;

public class PriceCalculator {
    private static final double TAXI_BASE_FARE = 10.0;
    private static final double TAXI_PER_KM = 4.0;
    private static final double STUDENT_DISCOUNT = 0.5;
    private static final double ELDERLY_DISCOUNT = 1.0;

    public static double calculateTaxiFare(double distance) {
        return TAXI_BASE_FARE + (distance * TAXI_PER_KM);
    }

    public static double applyDiscount(double originalPrice, boolean isStudent, boolean isOld) {
        if (isOld) return 0.0;
        if (isStudent) return originalPrice * (1 - STUDENT_DISCOUNT);
        return originalPrice;
    }
}