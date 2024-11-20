package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.model.Admin;
import java.util.List;

public interface AdminService {
	Admin createAdmin(Admin admin);

	Admin updateAdmin(Admin admin);

	void deleteAdminById(int adminId);

	Admin getAdminById(int adminId);

	List<Admin> getAllAdmins();

	Admin getAdminByUserId(Integer userId);

}
