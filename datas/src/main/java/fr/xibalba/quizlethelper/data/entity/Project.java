package fr.xibalba.quizlethelper.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "projects")@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"users", "categories", "cards"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Integer id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "code", nullable = false, unique = true)
    Integer code;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    List<User> users;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Card> cards;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Category> categories;


}