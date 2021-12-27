package com.taipt.listexample.expandablelist;

import java.util.List;

public class CompanyModel {

    private String name;
    private List<DeviceModel> devices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceModel> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceModel> devices) {
        this.devices = devices;
    }
}
