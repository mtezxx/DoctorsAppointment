package DoctorsAppointment.Client.Views.DoctorViews;

import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorLoginViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorLoginViewModel;
import DoctorsAppointment.Shared.Doctor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.rmi.RemoteException;

import javax.swing.text.View;

public class DoctorLoginController
{
  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backToHome;

  @FXML
  private Button loginButton;

  private ViewHandler viewHandler;

  @FXML
  private TextField cprInput;

  @FXML
  private TextField passwordInput;

  private DoctorLoginViewModel viewModel;
  private Doctor doctor;
  private Model model;

  public void init(DoctorLoginViewModel dlvm, ViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  @FXML
  public void backToHome() {
    viewHandler.start();
  }

  @FXML
  void loginButton() throws RemoteException {
    if(!validateInputs()){
      return;
    }
    DoctorLoginViewModel viewModel = new DoctorLoginViewModel(model);
    doctor = viewModel.findDoctor(getCPR(), getPasswordInput());
    if(viewModel.canLogin(getCPR(), getPasswordInput())){
      viewHandler.openDoctorDashboardView(doctor);
    }
    else{
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Login error.");
      alert.setHeaderText("Invalid username or password. Please try again.");
      alert.showAndWait();
    }
  }

  public long getCPR(){
    return Long.parseLong(cprInput.getText());
  }
  public String getPasswordInput(){
    return passwordInput.getText();
  }

  public boolean validateInputs(){
    boolean isValid = true;
    if(cprInput.getText().isEmpty()){
      showErrorPopup("CPR field cannot be empty.");
      isValid = false;
    } else if(cprInput.getText().length() != 10){
      showErrorPopup("CPR number must be 10 digits.");
      isValid = false;
    } else{
      try{
        Long.parseLong(cprInput.getText());
      } catch (NumberFormatException e){
        showErrorPopup("CPR number must be an integer.");
        isValid = false;
      }
    }
    if(passwordInput.getText().isEmpty()){
      showErrorPopup("Password field cannot be empty.");
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
