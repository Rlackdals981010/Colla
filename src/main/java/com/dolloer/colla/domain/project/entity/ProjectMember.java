package com.dolloer.colla.domain.project.entity;

import com.dolloer.colla.domain.auth.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status; // PENDING, ACCEPTED, DECLINED

    @ManyToOne(fetch = FetchType.LAZY)
    private Member invitedBy;

    private LocalDateTime invitedAt;

    public ProjectMember(Project project, Member member, Member invitedBy, ProjectRole role) {
        this.project = project;
        this.member = member;
        this.invitedBy = invitedBy;
        this.role = role;
        this.status = InvitationStatus.PENDING;
        this.invitedAt = LocalDateTime.now();
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }

    public void reject() {
        this.status = InvitationStatus.REJECTED;
    }

    public void changeRole(ProjectRole role) {this.role = role;}


}