package ru.otus.core.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "age", nullable = false)
    private int age;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Collection<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name, int age, Account account, Address address) {
        this.name = name;
        this.age = age;
        this.account = account;
        this.address = address;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhones(Collection<Phone> phones) {
        phones.forEach(phone -> phone.setClient(Client.this));
        this.phones = phones;
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

    public Account getAccount() {
        return account;
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", account=" + account +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
