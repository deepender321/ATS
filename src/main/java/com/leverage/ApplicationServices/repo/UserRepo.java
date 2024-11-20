package com.leverage.ApplicationServices.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.leverage.ApplicationServices.enums.Roles;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.JpaRepository;
import com.leverage.ApplicationServices.model.User;

public interface UserRepo extends JpaRepository<User, Integer> {

//    String findByMail(String mail);

    boolean existsByMail(String mail);
    
    List<User> getUserByRoles(Roles roles);

	boolean existsByMobileNumber(String mobileNumber);

	List<User> findBycreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

   Optional<User> findByMail(String mail);
}
