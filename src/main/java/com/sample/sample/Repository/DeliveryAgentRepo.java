package com.sample.sample.Repository;

import com.sample.sample.Model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryAgentRepo extends JpaRepository<DeliveryAgent,Long> {

    Optional<DeliveryAgent> findByEmail(String email);

}
