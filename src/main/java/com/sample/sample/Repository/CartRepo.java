package com.sample.sample.Repository;

import com.sample.sample.Model.Cart;
import com.sample.sample.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserAndStatus(User user, Cart.Status status);
    Optional<Cart> findBySessionIdAndStatus(String sessionId, Cart.Status status);
    List<Cart> findByUser(User user);
    List<Cart> findBySessionId(String sessionId);
}
