package com.businessapp.persistence;

import java.util.Collection;
import java.util.HashMap;

import com.businessapp.pojos.EntityIntf;


/**
 * Generic implementation of an Entity Container. Entities are instances of classes
 * that implement EntityIntf. Using a generic implementation avoids individual
 * container classes associated with Entity classes.
 * 
 * @param <T> Entity type that is associated with the container.
 */
public class GenericEntityContainer<T extends EntityIntf> implements EntityIntf {
	private static final long serialVersionUID = 1L;

	/*
	 * Properties.
	 */
	private final String id;

	private Class<T> genericType;

	private final HashMap<String,T>dataObjectMap;


	/**
	 * Public constructor.
	 * @param id Bag's identifier.
	 * @param genericType concrete type of T.
	 */
	public GenericEntityContainer( String id, Class<T>genericType ) {
		this.id = id;
		this.genericType = genericType;
		this.dataObjectMap = new HashMap<String,T>();
	}

	/**
	 * Return container id.
	 * @return container id.
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Return generic entity type.
	 * @return entity type. 
	 */
	public Class<?> getGenericType() {
		return genericType;
	}

	/**
	 * Return entity of matching id or null.
	 * @param id of entity
	 * @return entity of matching id or null.
	 */
	public T findById( String id ) {
		return dataObjectMap.get( id );
	}

	/**
	 * Return all entities stored in container.
	 * @return all entities of the container.
	 */
	public Collection<T> findAll() {
		return dataObjectMap.values();
	}

	/**
	 * Store entity in the container.
	 * @param e entity instance to store in container.
	 */
	public void store( T e ) {
		dataObjectMap.put( e.getId(), e );
	}

	/**
	 * Update entity in container.
	 * Cases:
	 * 	- entity with same id exists in container: if e has other reference
	 * 		than stored entity, reference e replaces stored one.
	 *  - entity id not on container: add e to the container.
	 * @param e entity to be updated in container.
	 */
	public void update( T e ) {
		String msg = "updated: ";
		if( e != null ) {
			String id = e.getId();
			T e2 = dataObjectMap.get( id );
			if( e != e2 ) {
				if( e2 != null ) {
					dataObjectMap.remove( e2.getId() );
				}
				msg = "created: ";
				dataObjectMap.put( id, e );
			}
			System.err.println( msg + e.getId() );			
		}
	}

	/**
	 * Delete collection of entities from container.
	 * @param ids collection of id's of entities to delete.
	 */
	public void delete( Collection<String> ids ) {
		String showids = "";
		for( String id : ids ) {
			dataObjectMap.remove( id );
			showids += ( showids.length()==0? "" : ", " ) + id;
		}
		if( ids.size() > 0 ) {
			System.err.println( "deleted: " + showids );
		}
	}

}
