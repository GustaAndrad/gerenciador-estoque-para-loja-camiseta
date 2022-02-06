package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.CamisetasService;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemCamisetas;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	private MenuItem menuItemHome;
	
	@FXML
	private Button camisetasButton;
	
	@FXML
	private Button aboutButton;

	@FXML
	public void onMenuItemCamisetasAction() {
		loadView("/gui/CamisetasList.fxml", (CamisetasListController controller) -> {
			controller.setCamisetasService(new CamisetasService());
			controller.updateTableView();
		});		
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {});		
	}
	
	@FXML
	public void onMenuItemHome() {
		loadView("/gui/Home.fxml", x -> {});
	}
	
	@FXML
	public void onBtCamisetas() {
		onMenuItemCamisetasAction();		
	}
	
	@FXML
	public void onBtAbout() {
		onMenuItemAboutAction();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		VBox newVBox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVBox.getChildren().get(0);
		mainVBox.getChildren().clear();
		mainVBox.getChildren().add(mainMenu);
		mainVBox.getChildren().addAll(newVBox.getChildren());
		
		T controller = loader.getController();
		initializingAction.accept(controller);
		
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
