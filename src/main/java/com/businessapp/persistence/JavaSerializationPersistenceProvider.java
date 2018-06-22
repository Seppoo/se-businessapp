package com.businessapp.persistence;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;


/**
 * ****************************************************************************
 * Persistence Provider that uses Java Serialization for saving serialized
 * container objects to a file specified by 'path' and loading them back.
 * Java Serialization is the simplest form to persist Java objects.
 * 
 */
class JavaSerializationPersistenceProvider implements PersistenceProviderIntf {
	private static JavaSerializationPersistenceProvider singleton = null;
	private final String dataPath = PersistenceProviderFactory.dataPath;
	private boolean started = false;

	/**
	 * Public factory method.
	 * @return instance of JavaSerializationProvider
	 */
	public static JavaSerializationPersistenceProvider getProvider() {
		if( singleton==null ) {
			singleton = new JavaSerializationPersistenceProvider();
		}
		return singleton;
	}

	/**
	 * Private constructor to enforce Singleton pattern.
	 */
	private JavaSerializationPersistenceProvider() {
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
			started = true;
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
	public void inject(ControllerIntf dep) {
	}

	/**
	 * Inject parent component of which a controller is part of.
	 * @param parent component.
	 */
	@Override
	public void inject(Component parent) {
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
			JavaSerializationWrapper wrapper = new JavaSerializationWrapper( container.getId(), container );
			path = dataPath + path;
			final Path storage = new File( path ).toPath();
			ObjectOutputStream out = null;
			try {
				out = new ObjectOutputStream( Files.newOutputStream( storage ) );
			    out.writeObject( wrapper );
	
			} catch( IOException ex ) {
				ex.printStackTrace();

			} finally {
				if( out != null ) {
					try {
						out.close();
					} catch( IOException ex2 ) {
						ex2.printStackTrace();
					}
				}
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
		path = dataPath + path;
		final Path storage = new File( path ).toPath();
		final ObjectInputStream in = new ObjectInputStream( Files.newInputStream( storage ) );
		try {
			//Serializable obj = (Serializable)in.readObject();
			JavaSerializationWrapper wrapper = (JavaSerializationWrapper)in.readObject();
			GenericEntityContainer<?> px = wrapper.getPersistenceContainerIntf();
			for( Serializable obj : px.findAll() ) {
				collector.call( obj );
			}

		} catch( ClassNotFoundException ex ) {
			ex.printStackTrace();

		} finally {
			in.close();
		}
	}

}
