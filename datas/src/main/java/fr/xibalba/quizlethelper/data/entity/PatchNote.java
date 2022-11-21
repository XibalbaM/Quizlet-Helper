package fr.xibalba.quizlethelper.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "patch_notes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PatchNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "content", nullable = false)
    String content;
}