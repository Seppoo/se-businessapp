package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import java.io.IOException;
import com.businessapp.pojos.Item;

import java.io.IOException;
import java.util.Collection;

public class ItemDataSource implements ItemDataIntf {
    private final GenericEntityContainer<Item> offers;
    private PersistenceProviderIntf persistenceProvider = null;
    private Component parent;

    /**
     * Factory method that returns a CatalogItem data source.
     *
     @return new instance of data source.
     */
    public static ItemDataIntf getController(String name, PersistenceProviderIntf persistenceProvider) {
        ItemDataIntf art = new ItemDataSource(name);
        art.inject(persistenceProvider);
        return art;
    }

    /**
     *
     Private constructor.
     */
    private ItemDataSource(String name) {
        this.offers = new GenericEntityContainer<Item>(name, Item.class);
    }

    @Override
    public void inject(ControllerIntf dep) {
        if (dep instanceof PersistenceProviderIntf) {
            this.persistenceProvider = (PersistenceProviderIntf) dep;
        }
    }

    @Override
    public void inject( Component parent ) {
        this.parent = parent;
    }

    @Override
    public void start() {
        if (persistenceProvider != null) {
            try {
                /*
                 * Attempt to load container from persistent storage.
                 */
                persistenceProvider.loadInto(offers.getId(), entity-> {
                    this.offers.store((Item) entity);
                    return true;
                });
            }
            catch (IOException e) {
                System.out.print(", ");
                System.err.print("No data: " + offers.getId());
            }
        }
    }

    @Override
    public void stop() {}

    @Override
    public Item findItemById(String id) {
        return offers.findById(id);
    }

    @Override
    public Collection<Item> findAllItems() {
        return offers.findAll();
    }

    @Override
    public Item newItem( String name, int quantity) {
        Item item = new Item(null, name, quantity);
        offers.update(item);
        if (persistenceProvider != null) {
            persistenceProvider.save(offers, offers.getId());
        }
        return item;
    }

    @Override
    public void updateItem(Item c) {
        offers.update(c);
        if (persistenceProvider != null) {
            persistenceProvider.save(offers, offers.getId());
        }
    }

    @Override
    public void deleteItems(Collection<String> ids) {
        offers.delete(ids);
        if (persistenceProvider != null) {
            persistenceProvider.save(offers, offers.getId());
        }
    }
}