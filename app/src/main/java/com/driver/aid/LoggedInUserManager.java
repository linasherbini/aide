package com.driver.aid;

import com.driver.aid.Model.Driver;
import com.driver.aid.Model.Employee;
import com.driver.aid.Model.Shop;
import com.google.firebase.auth.FirebaseAuth;

public class LoggedInUserManager {
    Employee employee;
    UserType userType;
    Driver driver;


    Shop shop;

    public LoggedInUserManager(UserType userType, Shop shop) {
        this.userType = userType;
        this.shop = shop;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Driver getDriver() {
        return driver;
    }
    public Shop getShop() {
        return shop;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    static LoggedInUserManager instance;

    public LoggedInUserManager(UserType userType, Driver driver) {
        this.userType = userType;
        this.driver = driver;
    }
    public LoggedInUserManager(UserType userType, Employee employee) {
        this.userType = userType;
        this.employee = employee;
    }

    public static void init(UserType userType, Driver driver) {
        instance = new LoggedInUserManager(userType, driver);
    }

    public static void init(UserType userType, Shop shop) {
        instance = new LoggedInUserManager(userType, shop);
    }
    public static void init(UserType userType, Employee employee) {
        instance = new LoggedInUserManager(userType, employee);
    }

    public static LoggedInUserManager getInstance() {
        return instance;
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public Employee getEmployee() {
        return employee;
    }
}
