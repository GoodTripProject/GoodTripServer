package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users", schema = "public", catalog = "GoodTripDatabase")
@Setter
@Getter
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "handle")
    private String handle;

    @Column(name = "hashed_password")
    private String hashedPassword;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "salt")
    private String salt;

    @OneToMany(cascade=CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Trip> trips;

    public User(String username, String handle, String hashedPassword, String name, String surname, String salt) {
        this.username = username;
        this.handle = handle;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.surname = surname;
        this.salt = salt;
    }
}
