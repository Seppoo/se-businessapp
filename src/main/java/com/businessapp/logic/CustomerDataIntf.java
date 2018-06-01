package com.businessapp.logic;

import java.util.Collection;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Customer;

/**
 * Public interface to Customer data. 
 *
 */
public interface CustomerDataIntf extends ControllerIntf {

	/**
	 * Factory method that returns a Customer data source.
	 * @return new instance of Customer data source.
	 */
	public static CustomerDataIntf getController() {
		return new CustomerDataMockImpl();
	}

	/**
	 * Public access methods to Customer data.
	 */
	Customer findCustomerById( String id );

	public Collection<Customer> findAllCustomers();

	public Customer newCustomer( String vorname , String name);

	public void updateCustomer( Customer c );

	public void deleteCustomers( Collection<String> ids );

}
