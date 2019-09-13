package com.driver.aid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Order extends RealmObject implements Parcelable {



    public String getDate() {
        return date;
    }

    public static final String STATUS_PENDING="PENDING";
    public static final String STATUS_ACCEPTED="ACCEPTED";
    public static final String STATUS_FINISHED="FINISHED";
    @PrimaryKey
    @Required
    private String orderId;
    @Required
    private String driverName;

    @Required
    private String driverID;

    private String workshopId;
    private String workshopName;
    @Required
    private String carType;
    @Required
    private String serviceType;
    @Required
    private String Issue;
    @Required
    private String driverPhone;
    private String date;
    private Double lat;
    private Double lan;
    private String status;
    private String price;
    private String technicianName;
    public String getTechnicianNumber() {
        return technicianNumber;
    }

    public void setTechnicianNumber(String technicianNumber) {
        this.technicianNumber = technicianNumber;
    }

    public Order(String orderId, String technicianName, String technicianNumber, String shopId, String shopName, String calcualteCost) {
        this.orderId=orderId;
        this.technicianName=technicianName;
        this.technicianNumber=technicianNumber;
        this.workshopId=shopId;
        this.workshopName=shopName;
        this.price=calcualteCost;
    }

    private String technicianNumber;
    public Order() {
    }

    public Order(String orderId, String driverName,String driverID, String workshopId, String workshopName, String carType, String serviceType, String issue, String driverPhone, Double lat, Double lan, String status, String price, String technicianName,String date) {
        this.orderId = orderId;
        this.driverID=driverID;
        this.driverName = driverName;
        this.workshopId = workshopId;
        this.workshopName = workshopName;
        this.carType = carType;
        this.serviceType = serviceType;
        Issue = issue;
        this.driverPhone = driverPhone;
        this.lat = lat;
        this.lan = lan;
        this.status = status;
        this.price = price;
        this.technicianName = technicianName;
        this.date=date;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getIssue() {
        return Issue;
    }

    public void setIssue(String issue) {
        Issue = issue;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLan() {
        return lan;
    }

    public void setLan(Double lan) {
        this.lan = lan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(driverName);
        dest.writeString(driverID);
        dest.writeString(workshopId);
        dest.writeString(workshopName);
        dest.writeString(carType);
        dest.writeString(serviceType);
        dest.writeString(Issue);
        dest.writeString(driverPhone);
        dest.writeString(date);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lan == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lan);
        }
        dest.writeString(status);
        dest.writeString(price);
        dest.writeString(technicianName);
        dest.writeString(technicianNumber);
    }
    protected Order(Parcel in) {
        orderId = in.readString();
        driverName = in.readString();
        driverID = in.readString();
        workshopId = in.readString();
        workshopName = in.readString();
        carType = in.readString();
        serviceType = in.readString();
        Issue = in.readString();
        driverPhone = in.readString();
        date = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lan = null;
        } else {
            lan = in.readDouble();
        }
        status = in.readString();
        price = in.readString();
        technicianName = in.readString();
        technicianNumber = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String calcualteCost() {
        if("Car Pickup service".equals(serviceType)){
            return "500 SAR";
        }else if("Car repair Help".equals(serviceType)){
            return "1500 SAR";
        }else {

            return "Unknown Cost";
        }
    }
}
