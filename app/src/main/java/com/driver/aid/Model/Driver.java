// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.driver.aid.Model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;
import io.realm.annotations.PrimaryKey;

public class Driver extends RealmObject {
    @PrimaryKey
    @Required
    private String userId;
    @Required
    private String FullName;

    @Required
    private String Phone;
    @Required
    private String Email;
    @Required
    private String PlateNumber;
    @Required
    private String CarType;
    @Required
    private String NationalID;

    public Driver() {
    }

    public Driver(String userId, String fullName,  String phone, String email, String plateNumber, String carType, String nationalID) {
        this.userId = userId;
        FullName = fullName;
        Phone = phone;
        Email = email;
        PlateNumber = plateNumber;
        CarType = carType;
        NationalID = nationalID;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getFullName() { return FullName; }

    public void setFullName(String FullName) { this.FullName = FullName; }

    public String getPhone() { return Phone; }

    public void setPhone(String Phone) { this.Phone = Phone; }

    public String getEmail() { return Email; }

    public void setEmail(String Email) { this.Email = Email; }

    public String getPlateNumber() { return PlateNumber; }

    public void setPlateNumber(String PlateNumber) { this.PlateNumber = PlateNumber; }

    public String getCarType() { return CarType; }

    public void setCarType(String CarType) { this.CarType = CarType; }

    public String getNationalID() { return NationalID; }

    public void setNationalID(String NationalID) { this.NationalID = NationalID; }


}
