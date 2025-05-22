package com.dolloer.colla.domain.sector.link.entity;


import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.common.Timestamped;
import com.dolloer.colla.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Link extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String linkTitle;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project; // 소속된 프로젝트

    @ManyToOne(fetch = FetchType.LAZY)
    private Member uploader; // 업로더

    public Link(String linkTitle, String url,String description, Project project, Member uploader){
        this.linkTitle = linkTitle;
        this.url = url;
        this.description = description;
        this.project = project;
        this.uploader = uploader;
    }

}
