package fr.xibalba.quizlethelper.data.entity;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"ownedProjects", "projects", "password"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    Integer id;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "is_admin", nullable = false)
    boolean isAdmin;

    @Column(name = "cards_created", nullable = false)
    int cardsCreated;

    @Column(name = "last_seen_changelog", nullable = false)
    int lastSeenChangelog;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    List<Project> ownedProjects;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    List<Project> projects;
}
