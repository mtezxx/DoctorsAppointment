package DoctorsAppointment.Client.Views.PatientViews;

import DoctorsAppointment.Client.Model.ClientFactory;
import DoctorsAppointment.Client.Model.ModelFactory;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientRegistrationViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import DoctorsAppointment.Shared.Patient;
import DoctorsAppointment.Shared.PatientModelManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import DoctorsAppointment.Client.Model.Model;
import java.sql.SQLException;

public class PatientRegistrationController
{
  @FXML
  private Button backToHome;

  @FXML
  private TextField cprTextField;

  @FXML
  private TextField firstNameTextField;

  @FXML
  private TextField lastNameTextField;

  @FXML
  private Label login;

  @FXML
  private PasswordField passwordTextField;

  @FXML
  private Button registerPatientButton;

  @FXML
  private TextField telTextField;

  private PatientModelManager patientModelManager;

  private ViewHandler viewHandler;
  private PatientRegistrationViewModel patientRVM;
  private Model model;
  private Patient patient;

  public void init(ViewHandler viewHandler, ViewModelFactory vmf){
    this.patientRVM = vmf.getPatientRVM();
    this.viewHandler = viewHandler;
    cprTextField.textProperty().bindBidirectional(patientRVM.cprNumberProperty());
    telTextField.textProperty().bindBidirectional(patientRVM.phoneNumberProperty());
    firstNameTextField.textProperty().bindBidirectional(patientRVM.firstNameProperty());
    lastNameTextField.textProperty().bindBidirectional(patientRVM.lastNameProperty());
    passwordTextField.textProperty().bindBidirectional(patientRVM.passwordProperty());

    try {
      patientModelManager = (PatientModelManager) PatientModelManager.getInstance();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void registerPatient() {
    if (!validateInputs()) {
      return;
    }

    long cprText = Long.parseLong(cprTextField.getText());
    int phoneText = Integer.parseInt(telTextField.getText());


    String firstName = firstNameTextField.getText();
    String lastName = lastNameTextField.getText();
    String password = passwordTextField.getText();

    try {
      patient=patientModelManager.createPatient(cprText, phoneText, firstName, lastName, password);
      patientRVM.registerPatient();
      viewHandler.openPatientDashboardView(patient);
    } catch (SQLException e) {
      showErrorPopup("An error occurred while registering the patient");
      e.printStackTrace();
    }
  }



  @FXML
  void backToLogin() {
    viewHandler.openPatientLoginView();
  }

  @FXML
  void backToHome() {
    viewHandler.start();
  }


  private boolean validateInputs() {
    boolean isValid = true;
    if (firstNameTextField.getText().isEmpty()) {
      showErrorPopup("First name cannot be empty.");
      isValid = false;
    }
    if (lastNameTextField.getText().isEmpty()) {
      showErrorPopup("Last name cannot be empty.");
      isValid = false;
    }
    if (cprTextField.getText().isEmpty()) {
      showErrorPopup("CPR number cannot be empty.");
      isValid = false;
    } else if (cprTextField.getText().length() != 10) {
      showErrorPopup("CPR number must be 10 digits long.");
      isValid = false;
    } else {
      try {
        Long.parseLong(cprTextField.getText());
      } catch (NumberFormatException e) {
        showErrorPopup("CPR number must be a valid number.");
        isValid = false;
      }
    }
    if (telTextField.getText().isEmpty()) {
      showErrorPopup("Phone number cannot be empty.");
      isValid = false;
    } else if (telTextField.getText().length() != 8) {
      showErrorPopup("Phone number must be 8 digits long.");
      isValid = false;
    } else {
      try {
        Long.parseLong(telTextField.getText());
      } catch (NumberFormatException e) {
        showErrorPopup("Phone number must be a valid number.");
        isValid = false;
      }
    }
    if (passwordTextField.getText().isEmpty()) {
      showErrorPopup("Password cannot be empty.");
      isValid = false;
    }
    return isValid;
  }


  private void showErrorPopup(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}


