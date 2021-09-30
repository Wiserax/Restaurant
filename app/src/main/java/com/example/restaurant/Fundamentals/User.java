package com.example.restaurant.Fundamentals;
public class User {
    private int idUser;
    //Шеф - 1
    //Заказчик - 2
    //Taster - 3
    //Brewer - 4
    private String role;

    private String name;
    private String phone;
    private String email;
    private String passportNumber;
    private String birthDate;
    private String login;
    private String password;
    private int hashCode;
    private int salary;

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public User() {
    }

    public User(String role, String name, String phone, String email,
                int salary, String birthDate, String login, String password) {
        hashCode = hashCode();
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.birthDate = birthDate;
        this.login = login;
        this.password = password;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String  getBirthDate() {
        return birthDate;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
