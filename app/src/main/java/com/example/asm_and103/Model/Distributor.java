package com.example.asm_and103.Model;

import com.google.gson.annotations.SerializedName;

public class Distributor {
    @SerializedName("_id")
    private String _id;
    private String name;

    public Distributor() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
