package ru.mtuci.antivirus.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "license_types")
public class LicenseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "default_duration")
    private int defaultDuration;

    @Column(name = "description")
    private String description;

    @OneToOne(mappedBy = "license_type")
    private License license;

    public LicenseType(String name, int defaultDuration, String description, License license) {
        this.name = name;
        this.defaultDuration = defaultDuration;
        this.description = description;
        this.license = license;
    }

    public LicenseType() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
