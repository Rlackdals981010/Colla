package com.dolloer.colla.domain.sector.schedule.entity;

import com.dolloer.colla.domain.auth.entity.Member;
import com.dolloer.colla.domain.project.entity.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime startAt;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private Long percent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project; // 소속된 프로젝트

    @ManyToOne(fetch = FetchType.LAZY)
    private Member manager; // 담당자

    public Schedule (
            String title,
            String description,
            LocalDateTime startAt,
            LocalDateTime endAt,
            Project project,
            Member manager
    ){
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt=endAt;
        this.percent = 0L;
        this.project = project;
        this.manager = manager;
    }
}
