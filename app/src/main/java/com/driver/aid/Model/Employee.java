// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.driver.aid.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Employee extends RealmObject {
    private String empFullName;
    private String empEmail;
    private String empPhone;
    private String empSkills;
    private String empDegree;

    private String status;
    private String empExp;
    private String empPassword;

    @Required
    private String userId;

    public Employee() {
    }

    public Employee(String empFullName, String empEmail, String empPhone, String empSkills, String empDegree, String empExp, String empPassword, String userId,String status) {
        this.empFullName = empFullName;
        this.empEmail = empEmail;
        this.empPhone = empPhone;
        this.empSkills = empSkills;
        this.empDegree = empDegree;
        this.empExp = empExp;
        this.empPassword = empPassword;
        this.status=status;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public String getEmpFullName() { return empFullName; }

    public void setEmpFullName(String empFullName) { this.empFullName = empFullName; }

    public String getEmpEmail() { return empEmail; }

    public void setEmpEmail(String empEmail) { this.empEmail = empEmail; }

    public String getEmpPhone() { return empPhone; }

    public void setEmpPhone(String empPhone) { this.empPhone = empPhone; }

    public String getEmpSkills() { return empSkills; }

    public void setEmpSkills(String empSkills) { this.empSkills = empSkills; }

    public String getEmpDegree() { return empDegree; }

    public void setEmpDegree(String empDegree) { this.empDegree = empDegree; }

    public String getEmpExp() { return empExp; }

    public void setEmpExp(String empExp) { this.empExp = empExp; }

    public String getEmpPassword() { return empPassword; }

    public void setEmpPassword(String empPassword) { this.empPassword = empPassword; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }


}
