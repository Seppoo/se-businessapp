package com.businessapp.pojos;

import java.util.ArrayList;
import java.util.List;

import com.businessapp.logic.IDGen;


/**
 * Customer is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 *
 */
public class Item implements EntityIntf  {
    private static final long serialVersionUID = 1L;

    private static IDGen IDG = new IDGen( "C.", IDGen.IDTYPE.AIRLINE, 6 );

    // Customer states.
    public enum ItemStatus { ACTIVE, BORROWED, RESERVED };


    /*
     * Properties.
     */

    private String id = null;

    private int quantity = 0;

    private String name = null;

    private List<LogEntry> notes = new ArrayList<LogEntry>();

    private ItemStatus status = ItemStatus.ACTIVE;


    /**
     * Private default constructor (required by JSON deserialization).
     */
    @SuppressWarnings("unused")
    private Item() { }

    /**
     * Public constructor.
     * @param id if customer id is null, an id is generated for the new customer object.
     * @param name customer.
     */
    public Item( String id,   String name, int quantity ) {
        this.id = id==null? IDG.nextId() : id;
        this.name = name;
        this.quantity = quantity;
        this.notes.add( new LogEntry( "Item record created." ) );
    }


    /**
     * Public getter/setter methods.
     */
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity(){ return quantity; }

    public List<String> getNotesAsStringList() {
        List<String>res = new ArrayList<String>();
        for( LogEntry n : notes ) {
            res.add( n.toString() );
        }
        return res;
    }

    public List<LogEntry> getNotes() {
        return notes;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public Item setName( String name ) {
        this.name = name;
        return this;
    }

    public Item setQuantity( int quantity ) {
        this.quantity = quantity;
        return this;
    }


    public Item setStatus( ItemStatus status ) {
        this.status = status;
        return this;
    }

}
