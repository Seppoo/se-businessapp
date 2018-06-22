package com.businessapp.persistence;


/**
 * Factory for persistence providers.
 * 
 */
public class PersistenceProviderFactory {
	public static String DEFAULT_PROVIDER_NAME = DefaultProvider.class.getName();

	static final String dataPath = "data/";

	private static PersistenceProviderIntf _provider = null;


	/**
	 * Return persistence provider instance.
	 * @param selector to specify type of persistence provider.
	 * 		- match "(.*)seri(.*)" - JavaSerializationPersistenceProvider
	 * 		- match "(.*)json(.*)" - JSONPersistenceProvider
	 * 		- match "(.*)jdbc(.*)" - JDBCPersistenceProvider
	 * @return persistence provider instance.
	 */
	public static PersistenceProviderIntf getPersistenceProvider( String selector ) {

		if( _provider == null ) {
			if( selector.toLowerCase().matches( "(.*)seri(.*)" ) ) {
				_provider = JavaSerializationPersistenceProvider.getProvider();

			} else {
				if( selector.toLowerCase().matches( "(.*)json(.*)" ) ) {
// JSON					_provider = JSONPersistenceProvider.getProvider();

				} else {
					if( selector.toLowerCase().matches( "(.*)jdbc(.*)" ) ) {
// JDBC					_provider = JDBCPersistenceProvider.getProvider();

					} else {
						_provider = DefaultProvider.getProvider();
					}			}
			}
		}
		return _provider;
	}

}
