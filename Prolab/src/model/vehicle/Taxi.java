package model.vehicle;

public class Taxi extends  Vehicle{

    private Float openingFee ;

    private Float costPerKm ;


    public Float getOpeningFee() {
        return openingFee;
    }

    public void setOpeningFee(Float openingFee) {
        this.openingFee = openingFee;
    }

    public Float getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(Float costPerKm) {
        this.costPerKm = costPerKm;
    }

    public double calculateFare(double distance) {
        return openingFee + (costPerKm * distance);
    }
}
