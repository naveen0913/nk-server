package com.sample.sample.Repository;


import com.sample.sample.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // cart repo
    @Query("SELECT w FROM CartItem w WHERE w.user.id = :userId")

    List<CartItem> getAllItemsByUser(String userId);











}