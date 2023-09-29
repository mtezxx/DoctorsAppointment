package DoctorsAppointment.Client.Views.PatientViews;

import DoctorsAppointment.Client.Model.ClientFactory;
import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.Model.ModelFactory;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientLoginViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import DoctorsAppointment.Shared.Patient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class PatientLoginController
{

  @FXML
  private AnchorPane anchorPane;
  @FXML
  private Button backToHome;
  @FXML
  private Label registerLabel;
  @FXML
  private Button login;
  private PatientLoginViewModel viewModel;
  private Model model;
  private Patient patient;
  @FXML
  private TextField cprInput;
  @FXML
  private PasswordField passwordInput;

  private ViewHandler viewHandler;

  public void init(PatientLoginViewModel ptlvm, ViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }
  @FXML
  void registerClick()
  {
    viewHandler.openPatientRegisterView();
  }

  @FXML
  void backToHome() {
    viewHandler.start();
  }

  @FXML
  void login() throws RemoteException
  {
    PatientLoginViewModel viewModel = new PatientLoginViewModel(model);
    patient = viewModel.findPatient(getCPR(), getPasswordInput());
    if(viewModel.login(getCPR(), getPasswordInput())){
      viewHandler.openPatientDashboardView(patient);
    }
    else{
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Login Error");
      alert.setHeaderText("Invalid username or password. Please try again.");
      alert.showAndWait();
    }
  }


  public long getCPR(){
    String input = cprInput.getText();
    long cpr = 0;
    try{
      cpr = Long.parseLong(input);
    } catch(NumberFormatException e){
      System.out.println("Invalid input.");
    }
    return cpr;
  }

  public String getPasswordInput(){
    return passwordInput.getText();
  }

}

