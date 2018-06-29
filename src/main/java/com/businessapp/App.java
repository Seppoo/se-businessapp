package com.businessapp;

import java.io.IOException;
import java.util.Arrays;

import com.businessapp.fxgui.AppGUIBuilder;
import com.businessapp.logic.*;

import com.businessapp.persistence.PersistenceProviderFactory;
import com.businessapp.persistence.PersistenceProviderIntf;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Main class launched by JavaFX runtime.
 * 
 * The UI uses a TabPanel to frame a variable number of components as Tab's.
 * Each component has a name, a UI/FXML Controller handling the user interface
 * interactions and a separate controller that implements logic independently
 * of the UI.
 *
 */
public class App extends Application {
	public static final String NAME			= "SE Business App";
	public static final String DATA_PATH	= "data/";
	public static final String FXML_PATH	= "fxgui/";
	private static App _app = null;

	PersistenceProviderIntf persistenceProvider = PersistenceProviderFactory.getPersistenceProvider("Kunden.json");

	/*
	 * List of App components in order of appearance on the main GUI/TabPanel.
	 */
	private ComponentBuilder compBuilder = new ComponentBuilder(
			Arrays.asList( new Component[] {
					//				Name,			FXML UI-Controller,		Logic-Controller
					new Component(	"Main",			"App.fxml",				null ),
					new Component(	"Calculator",	"Calculator.fxml",		CalculatorLogicIntf.getController() ),
					//new Component( "Calc_2",		"Calculator.fxml",		CalculatorLogicIntf.getController() ),
					new Component(	"Kunden",		"Customer.fxml",		CustomerDataSource.getController("Kunden", persistenceProvider) ),
					new Component(	"Kundenliste_2","Customer.fxml",		CustomerDataSource.getController("Kundenliste_2", persistenceProvider) ),
					new Component(	"Studenten","Customer.fxml",		CustomerDataIntf.getController() ),
			new Component( "Artikel","Article.fxml",			ItemDataSource.getController("Item", persistenceProvider) ),
			}));

	public static App getInstance() {
		return _app;
	}

	public static void main( String[] args) {
		launch( args );
	}

	@Override
	public void start( Stage stage ) throws IOException {
		int logLevelStart = 1;	// 0: no log, 1: logic controller, 2: fxml controller, 3: both
		App._app = this;
		AppGUIBuilder appGUIBuilder = AppGUIBuilder.getInstance();

		System.out.print( " **** Building Scene: " );

		appGUIBuilder.buildAppUI( stage, compBuilder.getComponents(), ( first, comp, fxmlController ) -> {
			comp.inject( fxmlController );
			System.out.print( ( first? "" : ", " ) + comp.getName() );
		});
		System.out.println( " - OK." );

		compBuilder.start( "    + Starting Controllers: ", logLevelStart, comp -> {
			comp.start();
		});

		appGUIBuilder.getTabPane().getSelectionModel().select( 2 );		// select n-th Tab at start
		stage.setTitle( App.NAME );
		stage.show();		// show JavaFX GUI
	}

	@Override
	public void stop() {
		int logLevelStop = 1;	// 0: no log, 1: logic controller, 2: fxml controller, 3: both
		System.out.print( " **** Shutting down ..." );
		compBuilder.stop( "\n    + Stopping Controllers: ", logLevelStop, comp -> {
			comp.stop();
		});
		System.out.println( " - OK." );
		System.exit( 0 );
	}

}
