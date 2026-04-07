package com.integ.task.common;

/**
 * User activity audit service
 */
public interface UserActivityAuditService {
	/**
	 * Sets created by attributes like created by user id and created time
	 * @param creatableEntity Entity object
	 */
	void processPrePersist(CreatableEntity creatableEntity);
	/**
	 * Sets last modified by attributes like last modified by user id and last modified time
	 * @param creatableEntity Entity object
	 */
	void processPreUpdate(CreatableEntity creatableEntity);
}
