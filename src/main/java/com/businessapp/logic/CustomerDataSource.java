package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Customer;
import java.io.IOException;
import java.util.Collection;

public class CustomerDataSource implements CustomerDataIntf {

    private final GenericEntityContainer<Customer> customers;
    private PersistenceProviderIntf persistenceProvider = null;

    /**
     *      * Factory method that returns a CatalogItem data source.      
     *
     *
     * @return new instance of data source.      
     */
    public static CustomerDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
        CustomerDataIntf cds = new CustomerDataSource(name);
        cds.inject(persistenceProvider);
        return cds;
    }

    /**
     *      * Private constructor.      
     */
    private CustomerDataSource(String name) {
        this.customers = new GenericEntityContainer<>(name, Customer.class);
    }

    @Override

    public void start() {
        if (persistenceProvider != null) {
            try {
                /*
                * Attempt to load container from persistent storage.
                */
                persistenceProvider.loadInto(customers.getId(), entity -> {
                    this.customers.store((Customer) entity);
                    return true;
                });
            } catch (IOException e) {
                System.out.print(", ");
                System.err.print("No data: " + customers.getId());
            }
        }
    }

    @Override
    public Customer findCustomerById(String id) {
        return customers.findById(id);

    }

    @Override
    public Collection<Customer> findAllCustomers() {
        return customers.findAll();
    }

    @Override
    public Customer newCustomer(String firstName, String familyName) {
        Customer cust = new Customer(null, firstName, familyName);
        customers.store(cust);
        if (persistenceProvider != null) {
            persistenceProvider.save(customers, customers.getId());
        }
        return cust;
    }

    @Override
    public void updateCustomer(Customer c) {
        customers.update(c);
        if (persistenceProvider != null) {
            persistenceProvider.save(customers, customers.getId());
        }
    }

    @Override
    public void deleteCustomers(Collection<String> ids) {
        customers.delete(ids);
        if (persistenceProvider != null) {
            persistenceProvider.save(customers, customers.getId());
        }
    }

    @Override
    public void inject(Component parent) {
        //???
    }

    @Override
    public void inject(ControllerIntf dep) {
        if (dep instanceof PersistenceProviderIntf) {
            this.persistenceProvider = (PersistenceProviderIntf) dep;
        }
    }

    @Override
    public void stop() {
        }

}