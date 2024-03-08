package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
@Setter
@Getter
@NoArgsConstructor
public class User implements UserDetails {
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

    @Column(name = "hashed_token")
    private String hashedToken;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "salt")
    private String salt;

    public User(String username, String handle, String hashedPassword, String hashedToken, String name, String surname, String salt) {
        this.username = username;
        this.handle = handle;
        this.hashedPassword = hashedPassword;
        this.hashedToken = hashedToken;
        this.name = name;
        this.surname = surname;
        this.salt = salt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    //почему-то без этого не работает
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
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
