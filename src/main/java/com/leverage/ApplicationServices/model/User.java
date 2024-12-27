package com.leverage.ApplicationServices.model;

import java.time.LocalDateTime;

import com.leverage.ApplicationServices.enums.Roles;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
//@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
	private Integer id;
	private String firstName;
	private String lastName;
	@Column(name = "mail", unique = true)
	private String mail;
	private String password;
	@Column(name = "mobile_number", unique = true)
	private String mobileNumber;

	@Column(nullable = false, name = "role")
	@Enumerated(EnumType.STRING)
	private Roles roles;

	@CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
