package com.sample.sample.Repository;

import com.sample.sample.Model.UserOrderedItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderedItemRepository extends JpaRepository<UserOrderedItems,Long> {

    List<UserOrderedItems> findByOrderId(Long orderId);
}
