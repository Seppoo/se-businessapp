//package com.businessapp;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import com.businessapp.fxgui.ViewControllerIntf;
//
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//
//
///**
// * Local helper class to find and load .fxml documents in order to create FX-GUI components.
// */
//class FXSceneLoader {
//	private final App app;
//
//	/**
//	 * Local constructor.
//	 * @param app
//	 */
//	FXSceneLoader( App app ) {
//		this.app = app;
//	}
//
//	/**
//	 * Functional interface used by loadFXML( ..., callback ) method.
//	 */
//	@FunctionalInterface
//	interface FXSceneLoader_FIntf {
//		void call( ViewControllerIntf ctrl, Node fxmlRoot );
//	}
//
//	/**
//	 * Local method to load a FXML document, which creates the specified fx:controller instance.
//	 * <p>
//	 * @param controllerClass controller class for which .fxml and .css need to be located and loaded.
//	 * @param callback functional interface called during controller launch.
//	 * @return JavaFX node of the loaded .fxml scene component.
//	 */
//	Node loadFXML( Class<? extends ViewControllerIntf> controllerClass, FXSceneLoader_FIntf callback ) {
//		Node node = null;
//		String afs[] =	controllerClass.getProtectionDomain().getCodeSource().getLocation().getPath().split( ":" );
//		String absPkgPth = afs.length > 1? afs[1] : afs[0];
//		String absClsPth = absPkgPth + controllerClass.getPackage().getName().replace( '.', '/' );
//		String relClsPth = "/" + ( controllerClass.getPackage().getName().replace( '.', '/' ) );
//
//		String fxmlControllerName = controllerClass.getName();
//		String fxmlControllerRegex = ".*fx:controller=\"" + fxmlControllerName + "\".*";
//
//		File folder = new File( absClsPth );
//		ArrayList<File> fxmlFileList = new ArrayList<File>();
//		fxmlFileList.addAll( Arrays.asList( folder.listFiles( ( dirN, fName ) -> { return fName.toLowerCase().endsWith( ".fxml" ); }) ) );
//		FileReader fr = null;
//		BufferedReader br = null;
//		File fxmlFile = null;
//		String cssFileName = null;
//		/*
//		 * Probe .fxml files to match controller passed as controllerClass argument.
//		 */
//		for( File f : fxmlFileList ) {
//			if( f.isFile() && fxmlFile==null) {
//				try {
//					fr = new FileReader( f );
//					br = new BufferedReader( fr );
//					String line;
//					while( ( line = br.readLine() ) != null ) {
//						if( line.matches( fxmlControllerRegex ) ) {
//							fxmlFile = f;
//							String cssName = f.getName().replaceAll( ".fxml$", ".css" );
//							String cssRegex = ".*stylesheets=\"@" + cssName + "\".*";
//							if( ! line.matches( cssRegex ) ) {
//								cssFileName = f.getAbsolutePath().replaceAll( ".fxml$", ".css" ).replaceAll( "\\\\", "/" );
//							//} else {
//							// System.out.println( "found .css load instruction for " + cssFileName + " in " + f.getName() );
//							}
//						}
//					}
//
//				} catch( IOException e ) {
//					e.printStackTrace();
//
//				} finally {
//					try {
//						if( fr != null ) {
//							fr.close();
//						}
//						if( br != null ) {
//							br.close();
//						}
//					} catch( IOException e ) {
//						e.printStackTrace();
//
//					}
//				}
//			}
//		}
//
//		if( fxmlFile != null ) {
//			try {
//				FXMLLoader fxmlLoader = new FXMLLoader( fxmlFile.toURI().toURL() );
//				node = fxmlLoader.load();
//				String fxmlFN = "/" + controllerClass.getPackage().getName().replace( '.', '/' ) + "/" + fxmlFile.getName();
//				System.out.println( " -- Loaded: " + fxmlFN );
//				Object ctrl = fxmlLoader.getController();
//				if( ctrl != null && ctrl.getClass() == controllerClass ) {
//					//if( ctrl instanceof ViewControllerIntf && callback != null ) {
//						callback.call( (ViewControllerIntf)ctrl, node );
//					//}
//				}
//
//			} catch( MalformedURLException e ) {
//				e.printStackTrace();
//
//			} catch( IOException e ) {
//				e.printStackTrace();
//			}
//
//		} else {
//			System.err.println( FXSceneLoader.class.getName() + ".loadFXML(): no .fxml file for controller class: " + controllerClass.getName() );
//		}
//
//		if( cssFileName != null ) {
//			File cssFile = new File( cssFileName );
//			Scene scene = app.getRootScene();
//			if( scene != null && cssFile.isFile() ) {
//				String rn = cssFileName.substring( cssFileName.indexOf( relClsPth ) );	// resource name relative to java packages
//				scene.getStylesheets().add( this.getClass().getResource( rn ).toExternalForm() );
//				System.out.println( " -- Loaded: " + rn );
//			}
//		}
//
//		return node;
//	}
//
//}
