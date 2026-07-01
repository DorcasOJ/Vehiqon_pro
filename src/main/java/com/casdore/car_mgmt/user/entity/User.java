package com.casdore.car_mgmt.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name="users")
public class User extends BaseEntity{
    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String address;
    private String primaryAccountNumber;
    private String email;
    private String password;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String status;
    private Boolean isVerified;


    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRole; // for user role

    @OneToMany(mappedBy = "user")
    private Set<Car> cars;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private Set<AuditLog> auditLog;

    @OneToMany(mappedBy = "user")
    private Set<UserSubscription> userPlan;

}
