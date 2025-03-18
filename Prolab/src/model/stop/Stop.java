package model.stop;

import java.util.List;

public class Stop {

    private String id;

    private String name;

    private Type type;

    private Double lat;

    private Double lon;

    private boolean isLastStop;

    private List<StopToStop> stopToStops;

    private Transfer transfer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public boolean isLastStop() {
        return isLastStop;
    }

    public void setLastStop(boolean lastStop) {
        isLastStop = lastStop;
    }

    public List<StopToStop> getStopToStops() {
        return stopToStops;
    }

    public void setStopToStops(List<StopToStop> stopToStops) {
        this.stopToStops = stopToStops;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }


}
