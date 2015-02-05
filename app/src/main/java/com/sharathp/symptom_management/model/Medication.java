package com.sharathp.symptom_management.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Medication {
    private long mId;

    @SerializedName("id")
    private String mServerId;

    @SerializedName("name")
    private String mName;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getServerId() {
        return mServerId;
    }

    public void setServerId(String serverId) {
        this.mServerId = serverId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
