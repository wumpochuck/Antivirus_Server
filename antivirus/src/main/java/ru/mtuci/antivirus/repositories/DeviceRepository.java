package ru.mtuci.antivirus.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.antivirus.entities.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device getDeviceByMacAddress(String macAddress);
}
