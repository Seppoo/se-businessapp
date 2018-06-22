package com.businessapp.persistence;

import java.io.IOException;
import java.io.Serializable;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;


class DefaultProvider implements PersistenceProviderIntf {
	private static DefaultProvider singleton = null;

	/**
	 * Public factory method.
	 * @return instance of NoopProvider
	 */
	public static DefaultProvider getProvider() {
		if( singleton==null ) {
			singleton = new DefaultProvider();
		}
		return singleton;
	}

	/**
	 * Private constructor to enforce Singleton pattern.
	 */
	private DefaultProvider() {
	}

	@Override
	public void save( GenericEntityContainer<?> obj, String path ) {
	}

	@Override
	public void loadInto( String path, CallbackIntf<Serializable, Boolean> collector ) throws IOException {
		throw new IOException( PersistenceProviderFactory.DEFAULT_PROVIDER_NAME );
	}

	@Override
	public void inject(ControllerIntf dep) {
	}

	@Override
	public void inject(Component parent) {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

}
