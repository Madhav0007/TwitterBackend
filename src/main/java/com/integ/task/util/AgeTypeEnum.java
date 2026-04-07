package com.integ.task.util;

import lombok.Getter;

@Getter
public enum AgeTypeEnum {
    CHILD(1L, "Child"), ADULT(2L, "Adult");

    private final Long id;
    private final String name;

    AgeTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}