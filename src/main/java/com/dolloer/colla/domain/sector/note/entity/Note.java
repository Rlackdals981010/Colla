package com.dolloer.colla.domain.sector.note.entity;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.common.Timestamped;
import com.dolloer.colla.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Note extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member uploader; // 업로더

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project; // 소속된 프로젝트

    public Note(String title, String text, Member uploader, Project project) {
        this.title = title;
        this.text=text;
        this.uploader = uploader;
        this.project = project;
    }

}
