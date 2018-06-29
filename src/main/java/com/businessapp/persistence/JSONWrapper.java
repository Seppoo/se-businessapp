package com.businessapp.persistence;

import java.util.ArrayList;
import java.util.Collection;

import com.businessapp.pojos.EntityIntf;
import com.fasterxml.jackson.annotation.JsonGetter;


/**
 * JSON structure to wrap entity container using JSON-fields:
 *	- id: file name under which JSON-serialized container is stored
 *	- genericType: Java class of entities stored as JSON-serialized objects.
 *	- entities: array of JSON serialized objects from entity container.
 */
class JSONWrapper {

	/*
	 * Properies.
	 */
	private final String id;

	private Class<?> genericType;

	private Collection<EntityIntf> entities;

	/**
	 * Public constructor.
	 * @param id file name under which JSON-serialized container is stored.
	 * @param container associated entity container.
	 */
	public JSONWrapper( GenericEntityContainer<? extends EntityIntf> container, CallbackIntf<EntityIntf, EntityIntf> dtoMapper ) {
		this.id = container.getId();
		this.genericType = container.getGenericType();
		this.entities = new ArrayList<EntityIntf>();

		for( EntityIntf e : container.findAll() ) {
			this.entities.add( dtoMapper.call( e ) );
		}
	}

	/**
	 * Getter for id.
	 * @return id.
	 */
	@JsonGetter("id")
	public String getId() {
		return id;
	}

	/**
	 * Getter for genericType.
	 * @return Java class of entities stored as JSON-serialized objects.
	 */
	@JsonGetter("genericType")
	Class<?> getGenericType() {
		return genericType;
	}

	/**
	 * Getter for entities.
	 * @return array of JSON serialized objects from entity container.
	 */
	@JsonGetter("entities")
	Collection<?> getEntities() {
		return entities;
	}

}
