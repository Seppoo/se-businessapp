package com.businessapp.persistence;

import java.io.Serializable;


/**
 * Wrapper class to enclose an entity container.
 */
class JavaSerializationWrapper implements Serializable {

	/*
	 * Properties.
	 */
	private static final long serialVersionUID = 1L;

	private GenericEntityContainer<?> container;


	/**
	 * Public constructor.
	 * @param id container id.
	 * @param container container.
	 */
	public JavaSerializationWrapper( String id, GenericEntityContainer<?> container ) {
		this.container = container;
	}

	/**
	 * Return generic container.
	 * @return generic container.
	 */
	public GenericEntityContainer<?> getPersistenceContainerIntf() {
		return container;
	}

}
