package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Camisetas;
import model.services.CamisetasService;

public class CamisetasListController implements Initializable, DataChangeListener {

	private CamisetasService service;

	@FXML
	private TableView<Camisetas> tableViewCamisetas;

	@FXML
	private TableColumn<Camisetas, Integer> tableColumnId;

	@FXML
	private TableColumn<Camisetas, String> tableColumnName;

	@FXML
	private TableColumn<Camisetas, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<Camisetas, Double> tableColumnPreco;
	
	@FXML
	private TableColumn<Camisetas, Double> tableColumnPrecoCusto;
	
	@FXML
	private TableColumn<Camisetas, Double> tableColumnCustoTotal;

	@FXML
	private TableColumn<Camisetas, Double> tableColumnValorTotal;
	
	@FXML
	private TableColumn<Camisetas, Double> tableColumnLucroLiquido;
	
	@FXML
	private TableColumn<Camisetas, Camisetas> tableColumnEdit;

	@FXML
	private TableColumn<Camisetas, Camisetas> tableColumnRemove;
	
	@FXML
	private TableColumn<Camisetas, Double> tableColumnLucroTotal;
	
	@FXML
	private Button btNew;
	
	@FXML
	private Button btBack;

	private ObservableList<Camisetas> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Camisetas obj = new Camisetas();
		createDialogForm(obj, "/gui/CamisetasForm.fxml", parentStage);
	}
	
	@FXML
	public void onBtBack() {
		loadView("/gui/Home.fxml", x -> {});
	}

	public void setCamisetasService(CamisetasService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
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

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tableColumnPrecoCusto.setCellValueFactory(new PropertyValueFactory<>("precoCusto"));
		tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
		tableColumnCustoTotal.setCellValueFactory(new PropertyValueFactory<>("custoTotal"));
		tableColumnLucroLiquido.setCellValueFactory(new PropertyValueFactory<>("lucroLiquido"));
		//tableColumnLucroTotal.setCellValueFactory(new PropertyValueFactory<>("lucroTotal"));
		Utils.formatTableColumnDouble(tableColumnPreco, 2);
		Utils.formatTableColumnDouble(tableColumnPrecoCusto, 2);
		Utils.formatTableColumnDouble(tableColumnValorTotal, 2);
		Utils.formatTableColumnDouble(tableColumnCustoTotal, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCamisetas.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Camisetas> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCamisetas.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Camisetas obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			CamisetasFormController controller = loader.getController();
			controller.setCamisetas(obj);
			controller.setCamisetasService(new CamisetasService());
			controller.subscribeDataChangeListenner(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter tshirt data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Io Exception", "error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChange() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Camisetas, Camisetas>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Camisetas obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/CamisetasForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Camisetas, Camisetas>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Camisetas obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Camisetas obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
