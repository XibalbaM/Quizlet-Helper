package fr.xibalba.quizlethelper.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "face1")
    String face1;

    @Column(name = "face2")
    String face2;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "cards_categories",
               joinColumns = @JoinColumn(name = "card_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<Category> categories;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", nullable = false)
    Card parent;
}