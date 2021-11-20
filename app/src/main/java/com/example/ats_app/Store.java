package com.example.ats_app;

import com.naver.maps.geometry.LatLng;

import java.util.List;

public class Store {
    private String address;
    private String detailAddress;
    private String businessName;
    private String phone;
    private String storeName;
    private String id;
    private List<String> positionIndex;
    private Long totalSeat;
    private String type;
    private String introduce;
    private LatLng p;

    public LatLng getP() {
        return p;
    }

    public void setP(LatLng p) {
        this.p = p;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getPositionIndex() { return this.positionIndex; }

    public void setPositionIndex(List<String> positionIndex) {
        this.positionIndex = positionIndex;
    }

    public Long getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Long totalSeat) {
        this.totalSeat = totalSeat;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setStoreAddress(String address) {
        this.address = address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
