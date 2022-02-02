package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Camisetas;
import model.exceptions.ValidationException;
import model.services.CamisetasService;

public class CamisetasFormController implements Initializable {
	
	private Camisetas entity;

	private CamisetasService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
		
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtQuantity;
	
	@FXML
	private TextField txtPrice;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorQuantity;
	
	@FXML
	private Label labelErrorPrice;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setCamisetas (Camisetas entity) {
		this.entity = entity;
	}
	
	public void setCamisetasService (CamisetasService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListenner(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
		entity = getFormData();
		service.saveOrUpdate(entity);
		notifyDataChangeListeners();
		Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMenssages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChange();
		}		
	}

	private Camisetas getFormData() {
		Camisetas obj = new Camisetas();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Campo não pode ser vazio");
		}
		obj.setName(txtName.getText());
		
		if(txtQuantity.getText() == null || txtQuantity.getText().trim().equals("")) {
			exception.addError("quantidade", "Campo não pode ser vazio");
		}
		obj.setQuantidade(Utils.tryParseToInt(txtQuantity.getText()));
		
		if(txtPrice.getText() == null || txtPrice.getText().trim().equals("")) {
			exception.addError("preco", "Campo não pode ser vazio");
		}
		obj.setPreco(Utils.tryParseToDouble(txtPrice.getText()));
		
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
				
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 20);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	private void setErrorMenssages(Map<String, String> errors){
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorQuantity.setText((fields.contains("quantidade") ? errors.get("quantidade") : ""));
		labelErrorPrice.setText((fields.contains("preco") ? errors.get("preco") : ""));
	}
	
}
