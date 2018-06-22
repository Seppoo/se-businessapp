package com.businessapp.fxgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.ItemDataIntf;
import com.businessapp.pojos.Item;
import com.businessapp.pojos.Item.ItemStatus;
import com.businessapp.pojos.LogEntry;

/**
 * FXML Controller class for Item.fxml
 *
 */
public class ItemFXMLController implements FXMLControllerIntf {
    private ItemDataIntf DS;

    /**
     * FXML skeleton defined as:
     * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
     * AnchorPane > HBox > Button			- buttons in footer area
     *
     * Defined CSS style classes:
     *   .tableview-Item-column-id
     *   .tableview-Item-column-name
     *   .tableview-Item-column-status
     *   .tableview-Item-column-contacts
     *   .tableview-Item-column-notes
     *   .tableview-Item-column-notes-button
     *   .tableview-Item-hbox
     */

    @FXML
    private AnchorPane fxItem_AnchorPane;

    @FXML
    private GridPane fxItem_GridPane;

    @FXML
    private TableView<Item> fxItem_TableView;

    @FXML
    private TableColumn<Item,String> fxItem_TableCol_ID;


    @FXML
    private HBox fxItem_HBox;	// Bottom area container for buttons, search box, etc.

    /*
     * TableView model.
     */
    private final ObservableList<Item> cellDataObservable = FXCollections.observableArrayList();

    private final String LABEL_ID		= "ID";
    private final String LABEL_NAME		= "Artikel-Name";
    private final String LABEL_QUANTITY	= "Anzahl";
    private final String LABEL_STATUS	= "Status";
    private final String LABEL_NOTES	= "Anmerk.";


    @Override
    public void inject( ControllerIntf dep ) {
        this.DS = (ItemDataIntf)dep;
    }

    @Override
    public void inject( Component parent ) {
    }

    @Override
    public void start() {
        // Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
        fxItem_HBox.prefWidthProperty().bind( ((AnchorPane) fxItem_AnchorPane).widthProperty().subtract( 12 ) );
        fxItem_HBox.prefHeightProperty().bind( ((AnchorPane) fxItem_AnchorPane).heightProperty() );

        fxItem_GridPane.prefWidthProperty().bind( ((AnchorPane) fxItem_AnchorPane).widthProperty().subtract( 16 ) );
        fxItem_GridPane.prefHeightProperty().bind( ((AnchorPane) fxItem_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 *
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
        fxItem_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
        //fxItem_GridPane.setStyle( "-fx-border-color: red;" );
        //fxItem_HBox.setStyle( "-fx-border-color: blue;" );

        fxItem_HBox.getStyleClass().add( "tableview-Item-hbox" );


		/*
		 * Construct TableView columns.
		 *
		 * TableView presents a row/column cell rendering of an ObservableList<Object>
		 * model. Each cell computes a "value" from the associated object property that
		 * defines how the object property is visualized in a TableView.
		 * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		 *
		 * TableView columns define how object properties are visualized and cell values
		 * are computed.
		 *
		 * In the simplest form, cell values are bound to object properties, which are
		 * public getter-names of the object class, and visualized in a cell as text.
		 *
		 * More complex renderings such as with graphical elements, e.g. buttons in cells,
		 * require overloading of the built-in behavior in:
		 *   - CellValueFactory - used for simple object property binding.
		 *   - CellFactory - overriding methods allows defining complex cell renderings.
		 *
		 * Constructing a TableView means defining
		 *   - a ObservableList<Object> model
		 *   - columns with name, css-style and Cell/ValueFactory.
		 *
		 * Variation 1: Initialize columns defined in FXML.
		 *  - Step 1: associate a .css class with column.
		 *  - Step 2: bind cell value to object property (must have public property getters,
		 *            getId(), getName()).
		 */
        fxItem_TableCol_ID.getStyleClass().add( "tableview-customer-column-id" );
        fxItem_TableCol_ID.setText( LABEL_ID );
        fxItem_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );



        TableColumn<Item,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
        tableCol_STATUS.getStyleClass().add( "tableview-Item-column-status" );
        tableCol_STATUS.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            // Render status as 3-letter shortcut of Item state enum.
            Item c = cellData.getValue();
            observable.set( c.getStatus().name().substring( 0, 3 ) );
            return observable;
        });

        /*
		 * Variation 2: Programmatically construct TableView columns.
		 */
        TableColumn<Item,String> tableCol_NAME = new TableColumn<>( LABEL_NAME );
        tableCol_NAME.getStyleClass().add( "tableview-Item-column-name" );
        tableCol_NAME.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Item c = cellData.getValue();
            observable.set( c.getName() );
            return observable;
        });

        TableColumn<Item,String> tableCol_QUANTITY = new TableColumn<>( LABEL_QUANTITY );
        tableCol_QUANTITY.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_QUANTITY.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Item c = cellData.getValue();
            observable.set( Integer.toString(c.getQuantity()) );
            return observable;
        });

        // TableColumn<Item,String> tableCol_NOTES = new TableColumn<>( "Notes" );
        TableColumn<Item,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
        tableCol_NOTES.getStyleClass().add( "tableview-customer-column-notes" );

        tableCol_NOTES.setCellFactory(

                // Complex rendering of Notes column as clickable button with number of notes indicator.
                new Callback<TableColumn<Item,String>, TableCell<Item, String>>() {

                    @Override
                    public TableCell<Item, String> call( TableColumn<Item, String> col ) {

                        col.setCellValueFactory( cellData -> {
                            Item c = cellData.getValue();
                            StringProperty observable = new SimpleStringProperty();
                            observable.set( c.getId() );
                            return observable;
                        });

                        TableCell<Item, String> tc = new TableCell<Item, String>() {
                            final Button btn = new Button();

                            @Override public void updateItem( final String item, final boolean empty ) {
                                super.updateItem( item, empty );
                                int rowIdx = getIndex();
                                ObservableList<Item> cust = fxItem_TableView.getItems();

                                if( rowIdx >= 0 && rowIdx < cust.size() ) {
                                    Item Item = cust.get( rowIdx );
                                    setGraphic( null );		// always clear, needed for refresh
                                    if( Item != null ) {
                                        btn.getStyleClass().add( "tableview-Item-column-notes-button" );
                                        List<LogEntry> nL = Item.getNotes();
                                        btn.setText( "notes: " + nL.size() );
                                        setGraphic( btn );	// set button as rendering of cell value

                                        //Event updateEvent = new ActionEvent();
                                        btn.setOnMouseClicked( event -> {
                                            String n = Item.getName();
                                            String label = ( n==null || n.length()==0 )? Item.getId() : n;

                                            PopupNotes popupNotes = new PopupNotes( label, nL );

                                            popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
                                                // Notification that List<Note> has been updated.
                                                // update button label [note: <count>]
                                                btn.setText( "notes: " + Item.getNotes().size() );
                                                // -> save node
                                                DS.updateItem( Item );
                                            });

                                            popupNotes.show();
                                        });
                                    }
                                } else {
                                    //System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
                                    setGraphic( null );		// reset button in other rows
                                }
                            }
                        };
                        return tc;
                    }
                });

        // Add programmatically generated columns to TableView. Columns appear in order.
        fxItem_TableView.getColumns().clear();
        fxItem_TableView.getColumns().addAll( Arrays.asList(
                fxItem_TableCol_ID,
                tableCol_STATUS,
                tableCol_NOTES,
                tableCol_NAME,
                tableCol_QUANTITY
        ));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
        fxItem_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
        fxItem_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
        fxItem_TableView.setRowFactory( tv -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked( event -> {
                if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
                    // Item rowData = row.getItem();
                    // fxItem_TableView.getSelectionModel().select( row.getIndex() );
                    //table.getSelectionModel().select( Math.min( i, size - 1 ) );
                    fxItem_Update();
                }
            });
            return row;
        });

		/*
		 * Load objects into TableView model.
		 */
        fxItem_TableView.getItems().clear();
        Collection<Item> col = DS.findAllItems();
        if( col != null ) {
            cellDataObservable.addAll( col );
        }
        fxItem_TableView.setItems( cellDataObservable );
    }

    @Override
    public void stop() {
    }


    @FXML
    void fxItem_Delete() {
        ObservableList<Item> selection = fxItem_TableView.getSelectionModel().getSelectedItems();
        List<Item> toDel = new ArrayList<Item>();
        List<String> ids = new ArrayList<String>();
        for( Item c : selection ) {
            toDel.add( c );
        }
        fxItem_TableView.getSelectionModel().clearSelection();
        for( Item c : toDel ) {
            ids.add( c.getId() );
            // should not alter cellDataObservable while iterating over selection
            cellDataObservable.remove( c );
        }
        DS.deleteItems( ids );
    }

    @FXML
    void fxItem_New() {
        Item Item = DS.newItem( null, 0 );
        openUpdateDialog( Item, true );
    }

    @FXML
    void fxItem_Update() {
        Item Item = fxItem_TableView.getSelectionModel().getSelectedItem();
        if( Item != null ) {
            openUpdateDialog( Item, false );
            //} else {
            //	System.err.println( "nothing selected." );
        }
    }

    @FXML
    void fxItem_Exit() {
        App.getInstance().stop();
    }


    /*
     * Private helper methods.
     */
    private final String SEP = ";";		// separates contacts in externalized String

    private void openUpdateDialog( Item c, boolean newItem ) {
        List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
        String n = c.getName();
        String label = ( n==null || n.length()==0 )? c.getId() : n;

        PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
                new StringTestUpdateProperty( LABEL_ID, c.getId(), false ),
                new StringTestUpdateProperty( LABEL_NAME, c.getName(), true ),
                new StringTestUpdateProperty( LABEL_STATUS, c.getStatus().name(), true ),
                new StringTestUpdateProperty( LABEL_QUANTITY, Integer.toString(c.getQuantity()) , true )
        ));

        // called when "OK" button in EntityEntryDialog is pressed
        dialog.addEventHandler( ActionEvent.ACTION, event -> {
            updateObject( c, altered, newItem );
        });

        dialog.show();
    }

    private void updateObject( Item Item, List<StringTestUpdateProperty> altered, boolean newItem ) {
        for( StringTestUpdateProperty dp : altered ) {
            String pName = dp.getName();
            String alteredValue = dp.getValue();
            //System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

            if( pName.equals( LABEL_NAME ) ) {
                Item.setName( alteredValue );
            }
            /*if( pName.equals( LABEL_QUANTITY ) ) {
                Item.setQuantity( alteredValue );
            }*/
            if( pName.equals( LABEL_STATUS ) ) {
                String av = alteredValue.toUpperCase();
                if( av.startsWith( "ACT" ) ) {
                    Item.setStatus( ItemStatus.ACTIVE );
                }
                if( av.startsWith( "BOR" ) ) {
                    Item.setStatus( ItemStatus.BORROWED );
                }
                if( av.startsWith( "RES" ) ) {
                    Item.setStatus( ItemStatus.RESERVED );
                }
            }
        }
        if( altered.size() > 0 ) {
            DS.updateItem( Item );	// update object in persistent store
            if( newItem ) {
                int last = cellDataObservable.size();
                cellDataObservable.add( last, Item );
            }
            // refresh TableView (trigger update
            fxItem_TableView.getColumns().get(0).setVisible(false);
            fxItem_TableView.getColumns().get(0).setVisible(true);

            altered.clear();	// prevent double save if multiple events fire
        }
    }

    private String contactsToString( List<String> con ) {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i < con.size(); i++ ) {
            sb.append( con.get( i ) );
            if( i < con.size() - 1 ) {
                sb.append( SEP ).append( " " );
            }
        }
        return sb.toString();
    }

}
