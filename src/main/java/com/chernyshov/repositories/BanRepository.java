package com.chernyshov.repositories;

import com.chernyshov.entities.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanRepository extends JpaRepository<Ban, Long> {
}
