package com.cottongallery.backend.domain;

import com.cottongallery.backend.constants.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    @Column(nullable = false)
    private Integer point;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Address> addressList = new ArrayList<>();

    private Account(String name, String username, String password, String email, String phoneNumber, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.point = 0;
    }

    public static Account createAccount(String name, String username, String password, String email, String phoneNumber, Role role) {
        return new Account(name, username, password, email, phoneNumber, role);
    }

    public static Account createAccountWithId(Long id, String name, String username, String password, String email, String phoneNumber, Role role) {
        Account account = new Account(name, username, password, email, phoneNumber, role);
        account.id = id;
        return account;
    }

    public void addAddress(Address address) {
        this.addressList.add(address);
        address.setAccount(this);
    }
}
