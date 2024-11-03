package ru.mtuci.antivirus.entities.Task5;

import jakarta.persistence.*;
import ru.mtuci.antivirus.entities.User;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}