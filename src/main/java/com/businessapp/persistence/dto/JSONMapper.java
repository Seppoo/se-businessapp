package com.businessapp.persistence.dto;

import java.util.HashMap;

import com.businessapp.pojos.Customer;
import com.businessapp.pojos.EntityIntf;


/**
 * Public JSON mapper between DTO (Data Transfer Object) and POJO (Plain old Java object).
 * 
 */
public class JSONMapper {
	private static final JSONMapper _mapper = new JSONMapper();	// singleton mapper

	private final HashMap<
		Class<? extends EntityIntf>,				// entity class
		Class<? extends JSONIntf>> classMapper;		// mapped DTO class


	/*
	 * Private constructor.
	 */
	private JSONMapper() {
		classMapper = new HashMap<Class<? extends EntityIntf>,Class<? extends JSONIntf>>();
		classMapper.put( Customer.class, CustomerJSON.class );
	}

	/**
	 * Public static method to map Entity class to DTO class, if mapping exists.
	 * @param cls entity class.
	 * @return DTO class if mapping exists, entity class otherwise.
	 */
	public static Class<?> map( Class<?> cls ) {
		Class<?>cls2 = _mapper.classMapper.get( cls );
		return cls2 != null? cls2 : cls;
	}

	/**
	 * Public static method to map between an Entity instance and an associated DTO instance,
	 * if mapping exists.
	 * @param e input instance
	 * @return DTO instance, if input was entity; entity instance if input was DTO instance.
	 * No change if no mapping exists.
	 */
	public static EntityIntf map( EntityIntf e ) {
		EntityIntf e2 = e instanceof JSONIntf?
				_mapper.mapOut( (JSONIntf)e ) :
				_mapper.mapIn( e );
		return e2;
	}


	/**
	 * Private methods.
	 * 
	 */
	private EntityIntf mapIn( EntityIntf e ) {
		if( e instanceof Customer ) {
			//System.out.println( " [map Customer -> CustomerJSON: " + e.getId() + "] " );
			e = new CustomerJSON( (Customer)e );

		}// else if( e instanceof CatalogItem ) {
			//e = new CatalogItemJSON( (CatalogItem)e );
		//}
		return e;
	}

	private EntityIntf mapOut( JSONIntf djo ) {
		EntityIntf e = null;

		if( djo instanceof CustomerJSON ) {
			//System.err.println( " [map CustomerJSON -> Customer: " + djo.getId() + "] " );
			e = ((CustomerJSON)djo).getCustomer();

		}// else if( djo instanceof CatalogItemJSON ) {
			//e = ((CatalogItemJSON)djo).getCatalogItem();
		//}
		return e;
	}

}
