package com.sample.sample.Repository;

import com.sample.sample.Model.Products;
import com.sample.sample.Model.User;
import com.sample.sample.Model.Wishlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist,Long> {
    List<Wishlist> findByUser(User user);
    Optional<Wishlist> findByUserAndProduct(User user, Products product);

    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.user = :user")
    void deleteByUser(@Param("user") User user);


}
