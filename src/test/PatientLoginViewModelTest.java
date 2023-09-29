import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientDashboardViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientLoginViewModel;
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

class PatientLoginViewModelTest
{
  private PatientLoginViewModel viewModel;
  private Model mockModel;
  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;

  @BeforeEach
  void setUp() throws RemoteException
  {
    mockModel = mock(Model.class);
    viewModel = new PatientLoginViewModel(mockModel);
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);
  }

  @Test
  void findPatient_WhenPatientExists_ReturnsPatient() throws SQLException {
    long cprNumber = 1597346858;
    String password = "h";
    String expectedName = "Tomas Masiar";

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong(1)).thenReturn(cprNumber);
    when(resultSet.getString(2)).thenReturn(expectedName);

    Patient patient = viewModel.findPatient(cprNumber, password);

    assertNotNull(patient);
    assertEquals(cprNumber, patient.getCprNumber());
    assertEquals(expectedName, patient.getName());
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
    verify(resultSet).getLong(1);
    verify(resultSet).getString(2);
    verify(resultSet).getString(3);
  }

  @Test
  void findPatient_WhenPatientDoesNotExist_ReturnsNull() throws SQLException {
    long cprNumber = 1234567890L;
    String password = "password";

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    Patient patient = viewModel.findPatient(cprNumber, password);

    assertNull(patient);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
    verifyNoMoreInteractions(resultSet);
  }

  @Test
  void login_WhenValidCredentials_ReturnsTrue() throws SQLException {
    long cprNumber = 1597346858;
    String password = "h";

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    boolean result = viewModel.login(cprNumber, password);

    assertTrue(result);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
  }

  @Test
  void login_WhenInvalidCredentials_ReturnsFalse() throws SQLException {
    long cprNumber = 1234567890L;
    String password = "password";

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    boolean result = viewModel.login(cprNumber, password);

    assertFalse(result);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
  }

  @Test
  void login_WhenSQLExceptionOccurs_ReturnsFalse() throws SQLException {
    long cprNumber = 1234567890L;
    String password = "password";

    when(connection.prepareStatement(anyString())).thenThrow(new SQLException());

    boolean result = viewModel.login(cprNumber, password);

    assertFalse(result);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verifyNoMoreInteractions(resultSet);
  }
}