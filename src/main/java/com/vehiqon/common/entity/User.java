package com.vehiqon.common.entity;

import com.vehiqon.common.enums.Role;
import com.vehiqon.features.carmgmt.entities.CarEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

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
    private String gender;
    private String address;
    private String primaryAccountNumber;
    private String email;
    private String password;
    private String phoneNumber;
    private String status;
    private Boolean isVerified;


//    @OneToMany(mappedBy = "user")
//    private Set<UserRole> userRole; // for user role
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private  Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<CarEntity> carEntities;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private Set<AuditLog> auditLog;

    @OneToMany(mappedBy = "user")
    private Set<UserSubscription> userPlan;

}
