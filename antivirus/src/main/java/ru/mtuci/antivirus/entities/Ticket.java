package ru.mtuci.antivirus.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Ticket {

    private Date currentDate;
    private int lifetime;
    private Date activationDate;
    private Date expirationDate;
    private Long userId;
    private Long deviceId;
    private Boolean isBlocked;
    private String signature;

    public Ticket(Date currentDate, int lifetime, Date activationDate, Date expirationDate, Long userId, Long deviceId, Boolean isBlocked, String signature) {
        this.currentDate = currentDate;
        this.lifetime = lifetime;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.userId = userId;
        this.deviceId = deviceId;
        this.isBlocked = isBlocked;
        this.signature = signature;
    }

    public Ticket() {
    }

    public Ticket(Date currentDate, int lifetime, Date activationDate, Date expirationDate, Long userId, Long deviceId, Boolean isBlocked) {
        this.currentDate = currentDate;
        this.lifetime = lifetime;
        this.activationDate = activationDate;
        this.expirationDate = expirationDate;
        this.userId = userId;
        this.deviceId = deviceId;
        this.isBlocked = isBlocked;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "currentDate=" + currentDate +
                ", lifetime=" + lifetime +
                ", activationDate=" + activationDate +
                ", expirationDate=" + expirationDate +
                ", userId=" + userId +
                ", deviceId=" + deviceId +
                ", isBlocked=" + isBlocked +
                ", signature='" + signature + '\'' +
                '}';
    }

    public String getBodyForSigning(){
        return String.format("Ticket:" +
                        "Current date: %s" +
                        "Lifetime: %d" +
                        "Activation date: %s" +
                        "Expiration date: %s" +
                        "User ID: %d" +
                        "Device ID: %d" +
                        "Is blocked: %b" +
                        "My mega secret string for signing XD",
                this.getCurrentDate(),
                this.getLifetime(),
                this.getActivationDate(),
                this.getExpirationDate(),
                this.getUserId(),
                this.getDeviceId(),
                this.getIsBlocked());
    }
}