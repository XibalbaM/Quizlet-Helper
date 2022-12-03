package fr.xibalba.quizlethelper.data.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Integer id;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "password", nullable = false)
    @ToString.Exclude
    String password;

    @Column(name = "is_admin", nullable = false)
    boolean isAdmin;

    @Column(name = "cards_created", nullable = false)
    int cardsCreated;

    @Column(name = "last_seen_changelog", nullable = false)
    int lastSeenChangelog;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @ToString.Exclude
    List<Project> projects;
}
