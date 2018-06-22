package com.businessapp.logic;

import java.util.Collection;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Item;

/**
 * Public interface to Item data.
 *
 */
public interface ItemDataIntf extends ControllerIntf {

    /**
     * Factory method that returns a Item data source.
     * @return new instance of Item data source.
     */
    public static ItemDataIntf getController() {
        return new ItemDataMockImpl();
    }

    /**
     * Public access methods to Item data.
     */
    Item findItemById( String id );

    public Collection<Item> findAllItems();

    public Item newItem(  String name, int quantity);

    public void updateItem( Item c );

    public void deleteItems( Collection<String> ids );

}
