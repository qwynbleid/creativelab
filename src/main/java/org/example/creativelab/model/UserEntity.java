package org.example.creativelab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String username;  // Унікальне ім'я користувача (@handle)
    private String fullName;  // Повне ім'я

    @Lob
    private String bio;  // Опис профілю

    private String profilePicture;  // Посилання на аватар

    @ElementCollection
    private List<String> interests;  // Інтереси користувача

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts = new ArrayList<>();  // Зв'язок з постами
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();  // Зв'язок з коментарями
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_followers",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "follower_id")
//    )
//    private Set<UserEntity> followers = new HashSet<>();
//
//    @ManyToMany(mappedBy = "followers")
//    private Set<UserEntity> following = new HashSet<>();
}
