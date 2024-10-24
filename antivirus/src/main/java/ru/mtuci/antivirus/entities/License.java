package ru.mtuci.antivirus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "licenses")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public License(int id, String value, User user) {
        this.id = id;
        this.value = value;
        this.user = user;
    }

    public License(String value, User user) {
        this.value = value;
        this.user = user;
    }

    public License() {
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
