package com.decagon.fitnessoapp.model.user;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_name", unique = true)
    private String userName;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany
    private List<Address> address;

    @NotNull
    private Role role;

    @Column(name = "verify_email")
    private boolean verifyEmail;

    @NotNull
    private String gender;

    @NotNull
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String image;

}
