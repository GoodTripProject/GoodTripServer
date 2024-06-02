package com.goodtrip.goodtripserver.database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", schema = "public", catalog = "GoodTripDatabase")
@Data
@NoArgsConstructor
public class User implements Serializable, UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "handle")
    private String handle;

    @Column(name = "hashed_password")
    @JsonIgnore
    private String hashedPassword;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "salt")
    @JsonIgnore
    private String salt;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
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

    public Integer getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return hashedPassword;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
