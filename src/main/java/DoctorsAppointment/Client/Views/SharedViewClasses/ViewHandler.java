package DoctorsAppointment.Client.Views.SharedViewClasses;

import DoctorsAppointment.Client.Views.DoctorViews.DoctorDashboardController;
import DoctorsAppointment.Client.Views.DoctorViews.DoctorLoginController;
import DoctorsAppointment.Client.Views.MainView.WelcomeController;
import DoctorsAppointment.Client.Views.PatientViews.PatientDashboardController;
import DoctorsAppointment.Client.Views.PatientViews.PatientLoginController;
import DoctorsAppointment.Client.Views.PatientViews.PatientRegistrationController;
import DoctorsAppointment.Shared.Doctor;
import DoctorsAppointment.Shared.Patient;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ViewHandler
{
  private Stage stage;
  private ViewModelFactory vmf;

  public ViewHandler(ViewModelFactory vmf) {
    stage = new Stage();
    this.vmf = vmf;
  }

  public void start() {
    openWelcomeView();
  }

  public void openWelcomeView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/DoctorsAppointment/welcome.fxml"));
      Parent root = loader.load();
      WelcomeController ctrl = loader.getController();
      ctrl.init(vmf.getWelcomeVM(), this);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openPatientLoginView() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/DoctorsAppointment/patientLogin.fxml"));
      Parent root = loader.load();
      PatientLoginController ctrl = loader.getController();
      ctrl.init(vmf.getPatientLoginVM(), this);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openPatientRegisterView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(
          "/DoctorsAppointment/patientRegistration.fxml"));
      Parent root = loader.load();
      PatientRegistrationController ctrl = loader.getController();
      ctrl.init(this, vmf);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openDoctorLoginView()
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/DoctorsAppointment/doctorLogin.fxml"));
      Parent root = loader.load();
      DoctorLoginController ctrl = loader.getController();
      ctrl.init(vmf.getDoctorLoginVM(), this);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openPatientDashboardView(Patient patient)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/DoctorsAppointment/patientDashboard.fxml"));
      Parent root = loader.load();
      PatientDashboardController ctrl = loader.getController();
      ctrl.init(this, patient, vmf);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void openDoctorDashboardView(Doctor doctor)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/DoctorsAppointment/doctorDashboard.fxml"));
      Parent root = loader.load();
      DoctorDashboardController ctrl = loader.getController();
      ctrl.init(this, doctor, vmf);
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

