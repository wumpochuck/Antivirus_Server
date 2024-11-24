package ru.mtuci.antivirus.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "mac_address")
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<DeviceLicense> deviceLicenses;

    public Device(String name, String macAddress, User user, List<DeviceLicense> deviceLicenses) {
        this.name = name;
        this.macAddress = macAddress;
        this.user = user;
        this.deviceLicenses = deviceLicenses;
    }

    public Device() {
    }
}
