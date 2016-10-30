package com;

import java.io.Serializable;

public class Client implements Serializable{

    private static final long serialVersionUID = 1;

    private String objectId;

    private String name;
    public Client(final String objectId, final String name) {
        this.objectId = objectId;
        this.name = name;
    }

    public Client() {}

    public String getObjectId() {
        return objectId;
    }

    public Client setObjectId(final String objectId) {
        this.objectId = objectId;
        return this;
    }

    public Client setName(final String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
