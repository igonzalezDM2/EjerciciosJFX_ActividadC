package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Persona;

public class ActividadBController implements Initializable {

    @FXML
    private Button btnAgregarPersona;

    @FXML
    private TableColumn<Persona, String> columnaApellidos;

    @FXML
    private TableColumn<Persona, Integer> columnaEdad;

    @FXML
    private TableColumn<Persona, String> columnaNombre;

    @FXML
    private TableView<Persona> tablaPersonas;

    @FXML
    private TextField tfApellidos;

    @FXML
    private TextField tfEdad;

    @FXML
    private TextField tfNombre;
    

    @FXML
    void agregarPersona(ActionEvent event) {
    	Persona persona = recogerPersona();
    	if (validarPersona(persona)) {    		
    		insertarPersona(persona);
    	}
    }
    
    @FXML
    void eliminar(ActionEvent event) {
    	Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
    	if (personaSeleccionada != null) {
    		tablaPersonas.getItems().remove(personaSeleccionada);
    		tablaPersonas.refresh();
    		limpiarCampos();
    		Alert alert = new Alert(AlertType.INFORMATION, String.format("La persona con nombre %s ha sido eliminada correctamente", personaSeleccionada.getNombre()), ButtonType.OK);
    		alert.showAndWait();
    	} else {
    		Alert alert = new Alert(AlertType.ERROR, "No has seleccionado ninguna persona", ButtonType.OK);
    		alert.showAndWait();
    	}
    }

    @FXML
    void modificar(ActionEvent event) {
    	Persona personaIntroducida = recogerPersona();
    	if (validarPersona(personaIntroducida)) {
        	Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        	if (personaSeleccionada != null) {
        		personaSeleccionada.setNombre(personaIntroducida.getNombre());
        		personaSeleccionada.setApellidos(personaIntroducida.getApellidos());
        		personaSeleccionada.setEdad(personaIntroducida.getEdad());
        		tablaPersonas.refresh();
        		limpiarCampos();
        		Alert alert = new Alert(AlertType.INFORMATION, "Persona modificada correctamente", ButtonType.OK);
        		alert.showAndWait();
        	} else {
        		Alert alert = new Alert(AlertType.ERROR, "No has seleccionado ninguna persona", ButtonType.OK);
        		alert.showAndWait();
        	}
    	}
    	
    }
    
    
    private void insertarPersona(Persona persona) {
    	ObservableList<Persona> personas = tablaPersonas.getItems();
    	
    	if (!personas.contains(persona)) {
    		tablaPersonas.getItems().add(persona);
    		tablaPersonas.refresh();
    		Alert alert = new Alert(AlertType.INFORMATION, "Persona añadida correctamente", ButtonType.OK);
    		alert.showAndWait();
    		limpiarCampos();
    	} else {
    		Alert alert = new Alert(AlertType.WARNING, "La persona está repetida", ButtonType.OK);
    		alert.showAndWait();
    	}
    	
	}
    
    private Persona recogerPersona() {
    	String nombre = tfNombre.getText() != null ? tfNombre.getText().toString() : "";
    	String apellidos = tfApellidos.getText() != null ? tfApellidos.getText().toString() : "";
    	int edad = tfEdad.getText() != null ? parseInt(tfEdad.getText().toString()) : -1;
    	return new Persona(nombre, apellidos, edad);
    }

    
    private boolean validarPersona(Persona persona) {
    	StringBuilder sb = new StringBuilder();
    	
    	if (persona.getNombre() == null || persona.getNombre().isBlank()) {
    		sb.append("El campo Nombre es obligatorio\n");
    	}
    	if (persona.getApellidos() == null || persona.getApellidos().isBlank()) {
    		sb.append("El campo Apellidos es obligatorio\n");    		
    	}
    	if (persona.getEdad() < 0) {
    		sb.append("El campo Edad es obligatorio\n");
    	}
    	
    	if (!sb.isEmpty()) {
    		Alert alert = new Alert(AlertType.ERROR, sb.toString(), ButtonType.OK);
    		alert.showAndWait();
    		return false;
    	}
    	return true;
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		columnaApellidos.setCellValueFactory(param -> {
			Persona persona = param.getValue();
			if (persona != null && persona.getApellidos() != null) {					
				return new SimpleStringProperty(persona.getApellidos());
			}
			return new SimpleStringProperty("");
		});
		
		columnaNombre.setCellValueFactory(param -> {
			Persona persona = param.getValue();
			if (persona != null && persona.getNombre() != null) {					
				return new SimpleStringProperty(persona.getNombre());
			}
			return new SimpleStringProperty("");
		});
		
		columnaEdad.setCellValueFactory(param -> {
			Persona persona = param.getValue();
			if (persona.getEdad() >= 0) {
				return new SimpleIntegerProperty(persona.getEdad()).asObject();
			}
			return  new SimpleIntegerProperty().asObject();
		});
		
		tablaPersonas.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Persona>() {

			@Override
			public void onChanged(Change<? extends Persona> c) {
				Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
				if (persona != null ) {
					tfNombre.setText(persona.getNombre());
					tfApellidos.setText(persona.getApellidos());
					tfEdad.setText(persona.getEdad() >= 0 ? Integer.toString(persona.getEdad()) : "");
				}
			}
			
		});
		
	
	}
	
	private void limpiarCampos() {
		tfNombre.clear();
		tfApellidos.clear();
		tfEdad.clear();
	}

	
	private static int parseInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return -1;
		} catch (NullPointerException e1) {
			return -1;
		}
	}
}
