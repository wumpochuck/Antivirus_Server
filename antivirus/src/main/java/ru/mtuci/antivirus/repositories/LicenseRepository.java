package ru.mtuci.antivirus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.antivirus.entities.*;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    License getLicensesByCode(String code);
}
