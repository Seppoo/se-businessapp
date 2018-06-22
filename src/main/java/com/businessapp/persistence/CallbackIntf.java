package com.businessapp.persistence;


/**
 * Generic callback interface with two generic parameters, a single argument of type P
 * and a generic return type R.
 * Souce: JavaFX 2.0, http://book2s.com/java/src/package/javafx/util/callback.html
 * Use can be replaced with: import javafx.util.Callback;
 *
 * @param <P> The type of the argument provided to the <code>call</code> method.
 * @param <R> The type of the return type of the <code>call</code> method.
 * 
 */
@FunctionalInterface
public interface CallbackIntf<P, R> {

	/**
	 * Method called by invoked code.
	 * @param param The single argument upon which the returned value should be
	 *      determined.
	 * @return An object of type R that may be determined based on the provided
	 *      parameter value.
	 */
	public R call( P param );
	
}
