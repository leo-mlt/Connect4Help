package org.template.rest.model;


public class ServiceRequest {
    private String address;
    private String details;
    private String category;
    private String startSlot;
    private String endSlot;
    private String expirationDate;
    //possibile inserimento foto

    public ServiceRequest(){}


    public ServiceRequest(String address, String details, String category, String startSlot, String endSlot,
                          String expirationDate) {
        this.address = address;
        this.details = details;
        this.category = category;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
        this.expirationDate = expirationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartSlot() {
        return startSlot;
    }

    public void setStartSlot(String startSlot) {
        this.startSlot = startSlot;
    }

    public String getEndSlot() {
        return endSlot;
    }

    public void setEndSlot(String endSlot) {
        this.endSlot = endSlot;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
