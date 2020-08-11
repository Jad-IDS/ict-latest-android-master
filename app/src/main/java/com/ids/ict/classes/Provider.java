package com.ids.ict.classes;

public class Provider {
    private String id;
    private String name;
    private String code;
    private String IsForTransfer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsForTransfer() {
        return IsForTransfer;
    }

    public void setIsForTransfer(String isForTransfer) {
        IsForTransfer = isForTransfer;
    }

    public String toString() {
        return name;
    }

    public Provider(String g) {
        this.name = g;
    }

    public Provider() {
        this.id = "0";
        this.IsForTransfer = "true";

    }

}
