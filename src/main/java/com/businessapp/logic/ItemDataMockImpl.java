package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Item;

import java.util.Collection;
import java.util.HashMap;


/**
 * Implementation of Item data.
 */
class ItemDataMockImpl implements ItemDataIntf {

    private final HashMap<String, Item> _data;    // HashMap as data container
    private final ItemDataIntf DS;                // Data Source/Data Store Intf
    private Component parent;                        // parent component

    /**
     * Constructor.
     */
    ItemDataMockImpl() {
        this._data = new HashMap<String, Item>();
        this.DS = this;
    }

    /**
     * Dependency injection methods.
     */
    @Override
    public void inject(ControllerIntf dep) {
    }

    @Override
    public void inject(Component parent) {
        this.parent = parent;
    }

    /**
     * Start.
     */
    @Override
    public void start() {

        String name = parent.getName();
        if (name.equals("Katalog")) {
            // Item list 1
            DS.newItem("Rennrad Horst" , 5);
            DS.newItem("Trudelrädchen Greta", 5);
            DS.newItem("Kinderrad Lisa", 5);
            DS.newItem("Kinderrad Merle", 5);
            DS.newItem("Mountainbike Justus", 5);
            DS.newItem("Rennrad Firbe", 5);
            DS.newItem("Riesenrad Lena", 5);
            DS.newItem("Einrad Rewe", 5);
            DS.newItem("Dreirad Mars", 5);
            DS.newItem("Einrad Fiffi", 5);
            DS.newItem("Riesenrad Lerrrr", 5);
            DS.newItem("Fahrrad Hans", 5);
            DS.newItem("Rennrad Lars", 5);
            DS.newItem("Mountainbike Tom", 5);
            DS.newItem("Vierrad Firat", 5);

        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Item findItemById(String id) {
        return _data.get(id);
    }

    @Override
    public Collection<Item> findAllItems() {
        return _data.values();
    }

    @Override
    public Item newItem(String name, int quantity) {
        Item e = new Item(null, name, quantity);
        _data.put(e.getId(), e);
        //save( "created: ", c );
        return e;
    }

    @Override
    public void updateItem(Item e) {
        String msg = "updated: ";
        if (e != null) {
            Item e2 = _data.get(e.getId());
            if (e != e2) {
                if (e2 != null) {
                    _data.remove(e2.getId());
                }
                msg = "created: ";
                _data.put(e.getId(), e);
            }
            //save( msg, c );
            System.err.println(msg + e.getId());
        }
    }

    @Override
    public void deleteItems(Collection<String> ids) {
        String showids = "";
        for (String id : ids) {
            _data.remove(id);
            showids += (showids.length() == 0 ? "" : ", ") + id;
        }
        if (ids.size() > 0) {
            //save( "deleted: " + idx, Items );
            System.err.println("deleted: " + showids);
        }
    }

}
