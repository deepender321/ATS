package com.leverage.ApplicationServices.service.impl;

import org.springframework.stereotype.Service;
import com.leverage.ApplicationServices.model.Admin;
import com.leverage.ApplicationServices.repo.AdminRepo;
import com.leverage.ApplicationServices.service.AdminService;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;

    public AdminServiceImpl(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepo.save(admin);
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return adminRepo.save(admin);
    }

    @Override
    public void deleteAdminById(int adminId) {
        adminRepo.deleteById(adminId);
    }

    @Override
    public Admin getAdminById(int adminId) {
        return adminRepo.findById(adminId).orElse(null);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }
    @Override
    public Admin getAdminByUserId(Integer userId) {
        return adminRepo.findByUser_Id(userId);
    }

	
}
