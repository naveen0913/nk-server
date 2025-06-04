package com.sample.sample.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.sample.Model.Contact;

public interface ContactRepo extends JpaRepository<Contact, String> {


}