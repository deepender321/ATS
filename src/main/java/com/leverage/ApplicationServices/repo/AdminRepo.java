package com.leverage.ApplicationServices.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.leverage.ApplicationServices.model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    // Corrected method to query Admin by userId
    Admin findByUser_Id(Integer userId);
}
