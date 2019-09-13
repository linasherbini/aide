package com.driver.aid.Model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Feedback extends RealmObject {
    @Required
    private String id;
    private String userId;
    private String userFullName;
    private String workshopId;
    private String workShopName;
    private String orderId;
    private String technicianName;
    private String feedbackContent;
    private String time;

    public Feedback() {
    }

    public Feedback(String id, String userId, String userFullName, String workshopId,String orderId, String workShopName, String technicianName, String feedbackContent, String time) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.workshopId = workshopId;
        this.orderId=orderId;
        this.workShopName = workShopName;
        this.technicianName = technicianName;
        this.feedbackContent = feedbackContent;
        this.time = time;
    }
}
