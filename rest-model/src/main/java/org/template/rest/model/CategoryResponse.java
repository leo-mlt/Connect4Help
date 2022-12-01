package org.template.rest.model;

public class CategoryResponse {
    private String categoryName;
    private byte[] photo;


    public CategoryResponse(){

    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public byte[] getFoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
