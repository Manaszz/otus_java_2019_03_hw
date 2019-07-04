package ru.otus.dao;

import javax.persistence.*;

@Entity
@Table(name = "Addres")
public
class AddressDataSet {
    @Id
    @GeneratedValue
    @Column(name = "addr_id")
    private Long id;
    private String street;

    @OneToOne(optional=true, mappedBy="addres")
    private User owner;

    public AddressDataSet() {
    }

    public AddressDataSet(String street, User owner) {
        this.street = street;
       this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public User getOwner() {
        return owner;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street +
                '}';
    }
}