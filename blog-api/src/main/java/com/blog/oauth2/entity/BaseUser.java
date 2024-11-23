package com.blog.oauth2.entity;

import com.blog.post.entity.Feed;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Table(name = "base_user")
@SuperBuilder
public class BaseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "name")
    protected  String name; // non-identifier

    @Column(name = "email")
    protected  String email;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

//    public BaseUser(String identifier, String name, String email, String role) {
//        this.identifier = identifier;
//        this.name = name;
//        this.email = email;
//        this.role = role;
//    }
}
