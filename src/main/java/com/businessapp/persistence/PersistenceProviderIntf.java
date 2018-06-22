package com.businessapp.persistence;

import java.io.IOException;
import java.io.Serializable;

import com.businessapp.ControllerIntf;


/**
 * ****************************************************************************
 * Public interface of a Persistence Provider to save entity objects stored in
 * a Container to an external storage medium that is specified by 'path' and
 * loading them back into a Container at a later point.
 * 
 * Implements ControllerIntf with: start(), stop(), inject( ControllerIntf dep ),
 * inject( Component parent )
 *
 */
public interface PersistenceProviderIntf extends ControllerIntf {

	/**
	 * Save all entity objects of a container to an external storage medium that is
	 * specified by 'path'.
	 * 
	 * @param container holding objects of Type T to be saved.
	 * @param path under which all objects of the container are saved.
	 */
	public void save( GenericEntityContainer<?> container, String path );


	/**
	 * Load previously saved entity objects from a path of an external source and
	 * iteratively invoke the collector callback for each entity object to allow
	 * the calling code to process them, e.g. to insert them into a container.
	 *  
	 * @param path pointing to the stored entity object collection on an external storage medium.
	 * @param collector callback to process entity objects in context of the invoking method.
	 * @throws IOException for error cases
	 */
	public void loadInto( String path, CallbackIntf<Serializable,Boolean> collector ) throws IOException;

}
