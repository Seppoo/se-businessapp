package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Customer;

import java.util.Collection;
import java.util.HashMap;


/**
 * Implementation of Customer data.
 */
class CustomerDataMockImpl implements CustomerDataIntf {

    private final HashMap<String, Customer> _data;    // HashMap as data container
    private final CustomerDataIntf DS;                // Data Source/Data Store Intf
    private Component parent;                        // parent component

    /**
     * Constructor.
     */
    CustomerDataMockImpl() {
        this._data = new HashMap<String, Customer>();
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
        if (name.equals("Kunden.json")) {
            // Customer list 1
            Customer eric = DS.newCustomer("Eric", "Winter").addContact("eric@gmail.com");
            Customer anja = DS.newCustomer("Anja", "Schuhmann");
            DS.newCustomer("Moritz" , "Baumann").addContact("moritz@gmx.de");
            DS.newCustomer("Claudia", "Lindner").addContact("claudia@gmail.com");
            eric.addContact("e532@yahoo.com");
            anja.addContact("anja.schuhmann@benz.de");
            DS.newCustomer("Alfons", "Kipp").addContact("alfons284@gmail.com");
        } else if (name.equals("Studenten")) {
            // student list
            Customer eric = DS.newCustomer("Lennard", "Grimm").addContact("s67895@beuth-hochschule.de");
            Customer anja = DS.newCustomer("Tjark", "Behrmann").addContact("anja.schuhmann@benz.de");
            DS.newCustomer("Robert" , "Bergner").addContact("moritz@gmx.de");
            DS.newCustomer("Jannis" ,  "Ruescher").addContact("beanie64@gmail.com");
            DS.newCustomer("Yannick", "Lindenbeg").addContact("yan@gmail.com");
        } else {
            // Customer list 2
            DS.newCustomer("Matteo", "Schwarz").addContact("Grossweg 4/0, 79805 Aschaffenburg");
            DS.newCustomer("Paul", "Neumann").addContact("Engelbert-Noack-Gasse 3, 16665 Parsberg");
            DS.newCustomer("Tom", "Wolf").addContact("Starkplatz 8, 79663 Wolfratshausen");
            DS.newCustomer("Mila", "Sauer").addContact("Nicole-Weidner-Platz 4, 15616 Gelnhausen");
            DS.newCustomer("Clara", "Richter").addContact("Ehlersplatz 59, 59965 Einbeck");
            DS.newCustomer("Henri", "Vogt").addContact("Kirschallee 21, 82493 Helmstedt");
            DS.newCustomer("Emily", "Beck").addContact("Silvio-Brand-Gasse 4/6, 54260 Hagenow");
            DS.newCustomer("Tom", "Winter").addContact("Luzia-Geisler-Gasse 74, 33489 Soltau-Fallingbostel");
            DS.newCustomer("Emilia", "Hartmann").addContact("Sanderring 5/2, 28072 Donaueschingen");
            DS.newCustomer("Greta"," Roth").addContact("Bartschallee 99, 94748 Mallersdorf");
            DS.newCustomer("Mathilda", "Becker").addContact("Kretschmergasse 95, 92935 Moers");
            DS.newCustomer("Paula", "Keller").addContact("Heilallee 720, 66426 Melsungen");
            DS.newCustomer("Rafael", "Schneider").addContact("Mina-Heine-Ring 9/9, 12096 Rochlitz");
            DS.newCustomer("Mia", "Sommer").addContact("Lindemannplatz 6/6, 32868 Arnstadt");
            DS.newCustomer("Karl", "Lang").addContact("Kloseweg 227, 91147 Wolfach");
            DS.newCustomer("Helena", "Horn").addContact("Stollplatz 1, 17700 Ravensburg");
            DS.newCustomer("Ella", "Wagner").addContact("Heinz-Dieter-Krebs-Weg 32, 82151 Grevenbroich");
            DS.newCustomer("Niklas", "Frank").addContact("Heinzeplatz 4, 40605 Bernburg");
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public Customer findCustomerById(String id) {
        return _data.get(id);
    }

    @Override
    public Collection<Customer> findAllCustomers() {
        return _data.values();
    }

    @Override
    public Customer newCustomer(String name, String vorname) {
        Customer e = new Customer(null, name, vorname);
        _data.put(e.getId(), e);
        //save( "created: ", c );
        return e;
    }

    @Override
    public void updateCustomer(Customer e) {
        String msg = "updated: ";
        if (e != null) {
            Customer e2 = _data.get(e.getId());
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
    public void deleteCustomers(Collection<String> ids) {
        String showids = "";
        for (String id : ids) {
            _data.remove(id);
            showids += (showids.length() == 0 ? "" : ", ") + id;
        }
        if (ids.size() > 0) {
            //save( "deleted: " + idx, customers );
            System.err.println("deleted: " + showids);
        }
    }

}
