import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientDashboardViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientLoginViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientRegistrationViewModel;
import DoctorsAppointment.Client.Views.PatientViews.PatientDashboardController;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Patient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PatientRegistrationViewModelTest
{
  private PatientRegistrationViewModel viewModel;
  private Model mockModel;
  private StringProperty cprNumberProperty;
  private StringProperty phoneNumberProperty;
  private StringProperty firstNameProperty;
  private StringProperty lastNameProperty;
  private StringProperty passwordProperty;

  @BeforeEach
  void setUp() {
    mockModel = mock(Model.class);
    viewModel = new PatientRegistrationViewModel(mockModel);
    cprNumberProperty = viewModel.cprNumberProperty();
    phoneNumberProperty = viewModel.phoneNumberProperty();
    firstNameProperty = viewModel.firstNameProperty();
    lastNameProperty = viewModel.lastNameProperty();
    passwordProperty = viewModel.passwordProperty();
  }

  @Test
  void registerPatient_WhenAllFieldsAreValid_AddsPatientToModel() {
    String cprNumber = "1597346858";
    String phoneNumber = "47586932";
    String firstName = "Tomas";
    String lastName = "Masiar";
    String password = "h";

    cprNumberProperty.set(cprNumber);
    phoneNumberProperty.set(phoneNumber);
    firstNameProperty.set(firstName);
    lastNameProperty.set(lastName);
    passwordProperty.set(password);

    viewModel.registerPatient();

    verify(mockModel).addPatient(argThat(patient -> patient.getCprNumber() == 1234567890L
        && patient.getPhoneNumber() == 12345678
        && patient.getFirstName().equals("Tomas")
        && patient.getLastName().equals("Masiar")
        && patient.getPassword().equals("h")));
  }

  @Test
  void registerPatient_WhenAnyFieldIsMissing_DoesNotAddPatientToModel() {
    String cprNumber = "";
    String phoneNumber = "12345678";
    String firstName = "";
    String lastName = "Doe";
    String password = "password";

    cprNumberProperty.set(cprNumber);
    phoneNumberProperty.set(phoneNumber);
    firstNameProperty.set(firstName);
    lastNameProperty.set(lastName);
    passwordProperty.set(password);

    viewModel.registerPatient();

    verify(mockModel, never()).addPatient(any());
  }
}