package model.stop;

public class Transfer {

    private String transferStopId;

    private Float transferTime;

    private Float transferPrice;

    public String getTransferStopId() {
        return transferStopId;
    }

    public void setTransferStopId(String transferStopId) {
        this.transferStopId = transferStopId;
    }

    public Float getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Float transferTime) {
        this.transferTime = transferTime;
    }

    public Float getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(Float transferPrice) {
        this.transferPrice = transferPrice;
    }
}
