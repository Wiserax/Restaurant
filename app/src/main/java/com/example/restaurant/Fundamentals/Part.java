package com.example.restaurant.Fundamentals;

public class Part {

    private int idPart;
    private int hashCode;
    private int idDish;
    private int idOrder;
    private int idWaiter;
    private int idCook;
    private int idTable;

    private int quality;
    private int quantity;
    private String status;
    private String comment;
    private String dishName;
    private int price;


    private String date;

    public Part() {
        hashCode = hashCode();
        status = "";
    }

    public Part(int idWaiter,
                int idCook,
                int idTable,
                int idDish,
                int quantity,
                String status,
                String date) {
        hashCode = hashCode();
        this.idWaiter = idWaiter;
        this.idCook = idCook;
        this.idTable = idTable;
        this.quantity = quantity;
        this.status = status;
        this.date = date;
        this.idDish = idDish;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getIdPart() {
        return idPart;
    }

    public void setIdPart(int idPart) {
        this.idPart = idPart;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getDishId() {
        return idDish;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdWaiter() {
        return idWaiter;
    }

    public void setIdWaiter(int idWaiter) {
        this.idWaiter = idWaiter;
    }

    public int getIdCook() {
        return idCook;
    }

    public void setIdCook(int idCook) {
        this.idCook = idCook;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
