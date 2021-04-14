package com.mikkaeru.biometry.repositoty;

import com.mikkaeru.biometry.model.Biometry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometryRepository extends CrudRepository<Biometry, Long> {
}
