package com.dolloer.colla.domain.sector.notice.entity;


import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.common.Timestamped;
import com.dolloer.colla.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project; // 소속된 프로젝트

    @ManyToOne(fetch = FetchType.LAZY)
    private Member uploader; // 업로더

    public Notice(String title, String description, Project project, Member uploader){
        this.title = title;
        this.description = description;
        this.project = project;
        this.uploader = uploader;
    }

    public void updateTitle(String title){
        this.title = title;
    }
    public void updateDescription(String description){
        this.description = description;
    }
}
