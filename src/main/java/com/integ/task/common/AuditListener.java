package com.integ.task.common;


import com.integ.task.util.UserLoginUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;

import java.util.Date;

public class AuditListener {

    @PrePersist
    private void prePersistOperation(Object entity) {

        if (entity instanceof CreatableEntity) {
            ((CreatableEntity) entity).setCreated(new Date());
            ((CreatableEntity) entity).setCreatedByIDF(UserLoginUtil.getUserLoginIDP());
        }
    }

    @PreUpdate
    private void preUpdateOperation(Object entity) {
        if (entity instanceof CreatableEntity) {
            ((CreatableEntity) entity).setLastModified(new Date());
            ((CreatableEntity) entity).setLastModifiedByIDF(UserLoginUtil.getUserLoginIDP());
        }
    }

    @PreRemove
    private void preRemoveOperation(Object entity) {
        if (entity instanceof CreatableEntity) {

        }
    }

}