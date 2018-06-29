package com.businessapp.persistence;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.dto.JSONMapper;
import com.businessapp.pojos.EntityIntf;


/**
 * ****************************************************************************
 * Persistence Provider that uses JSON-Serialization for saving container objects
 * to a JSON file specified by 'path' and loading them back.
 * JSON-Serialization is another simple choice to persist Java objects.
 * 
 */
class JSONPersistenceProvider implements PersistenceProviderIntf {
	private static JSONPersistenceProvider singleton = null;
	private final String dataPath = PersistenceProviderFactory.dataPath;
	private boolean started = false;
	private final ObjectMapper mapper;

	/**
	 * Public factory method.
	 * @return instance of JSONPersistenceProvider.
	 */
	public static JSONPersistenceProvider getProvider() {
		if( singleton==null ) {
			singleton = new JSONPersistenceProvider();
		}
		return singleton;
	}

	/**
	 * Private constructor to enforce Singleton pattern.
	 */
	private JSONPersistenceProvider() {
		mapper = new ObjectMapper();
		// "dd.MM.yyyy HH:mm", "dd.MM.yyyy", "HH:mm:ss", "yyyy-MM-dd HH:mm a z"
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm a z" );
		mapper.setDateFormat( dateFormat );
		this.start();
	}

	/**
	 * Start persistence provider.
	 */
	@Override
	public void start() {
		if( ! started ) {
			File dir = new File( dataPath );
			if( ! dir.exists() ) {
				dir.mkdirs();	// create dataPath, if not present
			}
		}
	}

	/**
	 * Stop persistence provider.
	 */
	@Override
	public void stop() {
	}

	/** 
	 * Inject reference to another controller to interact with.
	 * @param dep reference to another controller.
	 */
	@Override
	public void inject( ControllerIntf dep ) {
	}

	/**
	 * Inject parent component of which a controller is part of.
	 * @param parent component.
	 */
	@Override
	public void inject( Component parent ) {
	}

	/**
	 * Save all entities of a container to a file that is specified by 'path'.
	 * 
	 * @param container holding objects of Type T to be saved.
	 * @param path under which objects of the container are saved.
	 */
	@Override
	public void save( GenericEntityContainer<?> container, String path ) {
		if( container != null ) {
			/*
			 * Writing JSONPersistenceContainer with:
			 *	@JsonGetter("id")
			 *	@JsonGetter("genericType")
			 *	@JsonGetter("entities")
			 */
			JSONWrapper wrapper = new JSONWrapper( container, e -> {
			e = JSONMapper.map( e );
				return e;
			});
			path = dataPath + path + ".json";
			File file = new File( path );
			try {
				// Serialize Java object info JSON file.
				mapper.writerWithDefaultPrettyPrinter().writeValue( file, wrapper );

			} catch( IOException ex ) {
				System.out.println( "IOException: " + ex.getMessage() );
			}
		}
	}

	/**
	 * Load previously saved entities from a file addressed by path and iteratively invoke the
	 * collector callback for each entity to allow the calling code to process them, e.g. to
	 * insert them into a container.
	 *  
	 * @param path pointing to the stored entity object collection on an external storage medium.
	 * @param collector callback to process entity objects in context of the invoking method.
	 * @throws IOException for error cases.
	 */
	@Override
	public void loadInto( String path, CallbackIntf<Serializable,Boolean> collector ) throws IOException {
		path = dataPath + path + ".json";
		File file = new File( path );
		/*
		 * Assuming to read JSONPersistenceContainer with:
		 *	@JsonGetter("id")
		 *	@JsonGetter("genericType")
		 *	@JsonGetter("entities")
		 */
		JsonNode n = mapper.readTree( file );
		try {
			String clsName = n.get( "genericType" ).asText();
			ArrayNode an = (ArrayNode)n.get( "entities" );

			Class<?> cls = Class.forName( clsName );
		cls = JSONMapper.map( cls );

			for( JsonNode en : an ) {
				Object e = mapper.treeToValue( en , cls );
				if( e instanceof EntityIntf ) {
				e = JSONMapper.map( (EntityIntf) e );
				}
				collector.call( (Serializable)e );
			}

		} catch( ClassNotFoundException e ) {
			System.err.println( "JSON deserialization: ClassNotFoundException: " + e.getMessage() );
			throw new IOException( e.getMessage() );
		}
	}

}
