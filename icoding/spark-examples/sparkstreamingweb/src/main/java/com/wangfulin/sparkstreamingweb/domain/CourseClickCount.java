package com.wangfulin.sparkstreamingweb.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @projectName: sparkstreamingweb
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-05-24 13:16
 **/

public class CourseClickCount {
    private String name;
    private long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
