package com.sample.sample.Repository;



import com.sample.sample.Model.CartItem;
import com.sample.sample.Model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Query("SELECT a FROM UserAddress a WHERE a.accountDetails.id = :accountId")
    List<UserAddress> getAllUserAddress(@Param("accountId") Long accountId);

}
