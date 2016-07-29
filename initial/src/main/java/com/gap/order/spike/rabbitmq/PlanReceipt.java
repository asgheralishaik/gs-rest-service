package com.gap.order.spike.rabbitmq;

public class PlanReceipt {

    private String brandId;
    private String marketId;

    private String channleId;

    private String eventType;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getChannleId() {
        return channleId;
    }

    public void setChannleId(String channleId) {
        this.channleId = channleId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @Override
    public String toString() {
        return "PlanReceipt{" +
                "brandId='" + brandId + '\'' +
                ", marketId='" + marketId + '\'' +
                ", channleId='" + channleId + '\'' +
                ", eventType='" + eventType + '\'' +
                '}';
    }


}
