module com.example.doctorsappointment {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires java.sql;
  requires org.postgresql.jdbc;
  requires java.rmi;

  opens DoctorsAppointment to javafx.fxml;
  exports DoctorsAppointment;
  exports DoctorsAppointment.Client.ViewModel.PatientsViewModels;
  exports DoctorsAppointment.Server.ServerNetwork to java.rmi;
  exports DoctorsAppointment.Client.Views.PatientViews;
  opens DoctorsAppointment.Client.Views.PatientViews to javafx.fxml;
  exports DoctorsAppointment.Client.Views.DoctorViews to javafx.fxml;
  opens DoctorsAppointment.Client.Views.DoctorViews to javafx.fxml;
  exports DoctorsAppointment.Client.Views.MainView to javafx.fxml;
  opens DoctorsAppointment.Client.Views.MainView to javafx.fxml;
  exports DoctorsAppointment.Client.Views.SharedViewClasses to javafx.fxml;
  opens DoctorsAppointment.Client.Views.SharedViewClasses to javafx.fxml;
  exports DoctorsAppointment.Client.ViewModel.DoctorsViewModels;
  exports DoctorsAppointment.Shared.Appointments;
  exports DoctorsAppointment.Shared.Appointments.States;
  exports DoctorsAppointment.Client.Model;

}

