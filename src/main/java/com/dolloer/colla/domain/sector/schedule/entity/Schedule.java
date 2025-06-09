package com.dolloer.colla.domain.sector.schedule.entity;

import com.dolloer.colla.domain.auth.entity.Member;
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
    private Member manager; // 담당자
}
