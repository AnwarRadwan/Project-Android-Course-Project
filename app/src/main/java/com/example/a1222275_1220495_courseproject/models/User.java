package com.example.a1222275_1220495_courseproject.models;

public class User {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String category;
    private String phone;
    private String image;

    public User() {

    }

    public User(long id,
                String email,
                String firstName,
                String lastName,
                String password,
                String gender,
                String category,
                String phone,
                String image) {

        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gender = gender;
        this.category = category;
        this.phone = phone;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {

        return "User{" +
                "\nid=" + id +
                "\nemail='" + email + '\'' +
                "\nfirstName='" + firstName + '\'' +
                "\nlastName='" + lastName + '\'' +
                "\npassword='" + password + '\'' +
                "\ngender='" + gender + '\'' +
                "\ncategory='" + category + '\'' +
                "\nphone='" + phone + '\'' +
                "\nimage='" + image + '\'' +
                "\n}";
    }
}