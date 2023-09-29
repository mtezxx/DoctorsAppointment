package DoctorsAppointment.Client.Views.MainView;

import DoctorsAppointment.Client.Model.ClientFactory;
import DoctorsAppointment.Client.Model.ModelFactory;
import DoctorsAppointment.Client.ViewModel.General.WelcomeViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.text.View;

public class WelcomeController {
  @FXML
  private Button patient;

  @FXML
  private Button doctor;

  private ViewHandler viewHandler;

  public void init(WelcomeViewModel wcm, ViewHandler viewHandler) {
    this.viewHandler = viewHandler;
  }

  public void openPatientLoginScene() {
    viewHandler.openPatientLoginView();
  }

  public void openDoctorLoginScene()
  {
    viewHandler.openDoctorLoginView();
  }
}

