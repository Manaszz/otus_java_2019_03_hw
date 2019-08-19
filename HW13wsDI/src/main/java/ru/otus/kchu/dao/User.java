package ru.otus.kchu.dao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    private String role;
    private String password;

    private int age;

    @OneToOne (cascade=CascadeType.ALL)
    @JoinColumn (name="addr_id")
    private AddressDataSet addres;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Phone> phones = new ArrayList<>();

    public AddressDataSet getAddres() {
        return addres;
    }

    public void setAddres(AddressDataSet addres) {
        this.addres = addres;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
//                ", addres=" + addres +
                ", phones=" + phones +
                '}';
    }

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.role= "user";
        this.password= "pass";
    }
    public User(String name, String pass, String role) {
        this.name = name;
        this.password = pass;
        this.role= role;
    }

    public User(){
    }

    public void setRole(String role) {        this.role = role;    }

    public String getRole() {        return role;    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setPhones(List<Phone> listPhone) {
        phones = listPhone;
    }
}
