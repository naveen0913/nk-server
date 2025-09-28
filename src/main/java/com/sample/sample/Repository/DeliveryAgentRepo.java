package com.sample.sample.Repository;

import com.sample.sample.Model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAgentRepo extends JpaRepository<DeliveryAgent,Long> {


}
