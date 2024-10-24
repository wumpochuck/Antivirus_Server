package ru.mtuci.antivirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.antivirus.entities.License;

@Repository
public interface LicenseRepository extends JpaRepository<License,Integer> {
}
