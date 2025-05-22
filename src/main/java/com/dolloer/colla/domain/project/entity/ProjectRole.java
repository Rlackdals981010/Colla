package com.dolloer.colla.domain.project.entity;

public enum ProjectRole {
    MEMBER(1),
    ADMIN(2),
    OWNER(3);

    private final int level;
    ProjectRole(int level) { this.level = level; }

    public boolean hasHigherPermissionThan(ProjectRole other) {
        return this.level > other.level;
    }
}