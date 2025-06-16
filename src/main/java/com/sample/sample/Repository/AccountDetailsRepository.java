package com.sample.sample.Repository;

import com.sample.sample.Model.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {

    @Query("SELECT a FROM AccountDetails a WHERE a.user.id = :userId")
    Optional<AccountDetails> findByUserId(@Param("userId") String userId);


}

