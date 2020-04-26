package com.example.pratapgurung.testnavigation;

public class Orders {
    String orderId;
    String customerId;
    String serviceId;
    String address;
    String city;
    String agentId;
    String state;
    String zipcode;
    String serviceCost;
    String serviceHr;
    String requestDate;
    String completionDate;
    String desc;


    public Orders() {
        orderId = "";
        customerId = "";
        serviceId = "";
        address = "";
        city = "";
        agentId = "";
        state = "";
        zipcode = "";
        serviceCost = "";
        serviceHr = "";
        requestDate = "";
        completionDate = "";
        desc = "";
    }
    public Orders(String oId, String cId, String sId, String agId, String add, String c,
                  String st, String zip, String cost, String hr, String rDate, String cDate,
                  String description) {
        orderId = oId;
        customerId = cId;
        serviceId = sId;
        address = add;
        city = c;
        agentId = agId;
        state = st;
        zipcode = zip;
        serviceCost = cost;
        serviceHr = hr;
        requestDate = rDate;
        completionDate = cDate;
        desc = description;
    }

    public String getAddress() {
        return address;
    }

    public String getCustId() {
        return customerId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getDescription() {
        return desc;
    }
}
