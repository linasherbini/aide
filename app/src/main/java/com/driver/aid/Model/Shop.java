// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.driver.aid.Model;

import io.realm.RealmObject;
import io.realm.annotations.Required;
import io.realm.annotations.PrimaryKey;

public class Shop extends RealmObject {
    @PrimaryKey
    @Required
    private String userId;
    @Required
    private String name;
    @Required
    private String branch;
    @Required
    private String email;
    @Required
    private String phone;

    public Shop() {
    }

    public Shop(String userId, String name, String branch, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.branch = branch;
        this.email = email;
        this.phone = phone;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getBranch() { return branch; }

    public void setBranch(String branch) { this.branch = branch; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}
