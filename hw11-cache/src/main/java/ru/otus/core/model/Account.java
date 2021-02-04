package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "no")
    private String no;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "rest", nullable = false)
    private double rest;
    @OneToOne(mappedBy = "account")
    private Client client;

    public Account() {
    }

    public Account(String no, String type, double rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRest() {
        return rest;
    }

    public void setRest(double rest) {
        this.rest = rest;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no='" + no + '\'' +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}

