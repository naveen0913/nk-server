package com.sample.sample.Repository;

import com.sample.sample.Model.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
    public interface DesignRepo extends JpaRepository<Design, Long> {
    }
