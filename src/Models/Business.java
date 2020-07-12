package Models;

import java.util.ArrayList;

public class Business {
    private String businessId;
    private String fullAddress;
    private boolean open;
    ArrayList <BusinessCategories> categories = new ArrayList <BusinessCategories>();
    ArrayList <BusinessSubCategories> subCategories = new ArrayList<BusinessSubCategories>();
    ArrayList <Attribute> attr = new ArrayList<Attribute>();
    private String city;
    private int review_count;
    private String name;
    private float longitude;
    private String state;
    private float stars;
    private float latitude;

    public ArrayList<BusinessSubCategories> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<BusinessSubCategories> subCategories) {
        this.subCategories = subCategories;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ArrayList<BusinessCategories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<BusinessCategories> categories) {
        this.categories = categories;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Attribute> getAttr() {
        return attr;
    }

    public void setAttr(ArrayList<Attribute> attr) {
        this.attr = attr;
    }
}
