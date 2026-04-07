package com.integ.task.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public class CreatableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "created", nullable = false)
    protected Date created;

    @Column(name = "createdbyidf")
    protected Long createdByIDF;

    @Column(name = "lastmodified")
    protected Date lastModified;

    @Column(name = "lastmodifiedbyidf")
    protected Long lastModifiedByIDF;


}
