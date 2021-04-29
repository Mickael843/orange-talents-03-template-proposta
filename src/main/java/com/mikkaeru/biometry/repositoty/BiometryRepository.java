package com.mikkaeru.biometry.repositoty;

import com.mikkaeru.biometry.model.Biometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometryRepository extends JpaRepository<Biometry, Long> {
}
