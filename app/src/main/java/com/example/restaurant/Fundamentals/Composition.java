package com.example.restaurant.Fundamentals;

public class Composition {

    private int idComp;
    private int hashCode;
    private int idProduct;
    private int idDish;

    public Composition() {
        hashCode = hashCode();
    }

    public Composition(int idProduct, int idDish) {
        hashCode = hashCode();
        this.idProduct = idProduct;
        this.idDish = idDish;
    }

    public int getIdComp() {
        return idComp;
    }

    public void setIdComp(int idComp) {
        this.idComp = idComp;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdDish() {
        return idDish;
    }

    public void setIdDish(int idDish) {
        this.idDish = idDish;
    }
}
