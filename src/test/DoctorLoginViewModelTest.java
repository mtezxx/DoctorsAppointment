
import static org.junit.jupiter.api.Assertions.*;
import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorDashboardViewModel;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorLoginViewModel;
import DoctorsAppointment.Client.Views.DoctorViews.DoctorDashboardController;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.States.Approved;
import DoctorsAppointment.Shared.Appointments.States.Canceled;
import DoctorsAppointment.Shared.Doctor;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class DoctorLoginViewModelTest
{
  private DoctorLoginViewModel viewModel;
  private Model model;
  private Connection connection;
  private PreparedStatement preparedStatement;
  private ResultSet resultSet;

  @BeforeEach
  void setUp() throws SQLException, RemoteException
  {
    model = mock(Model.class);
    viewModel = new DoctorLoginViewModel(model);
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);
  }

  @Test
  void canLogin_WhenValidCredentials_ReturnsTrue() throws SQLException {
    long cprNumber = 1234567890;
    String password = "password";

    when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);

    boolean result = viewModel.canLogin(cprNumber, password);

    assertTrue(result);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
  }


//Test failed only because Mockito couldn't resolve our database driver for unknown reasons.
  @Test
  void canLogin_WhenInvalidCredentials_ReturnsFalse()
      throws SQLException, ClassNotFoundException
  {
    long cprNumber = 1234567890;
    String password = "wrongPassword";

    Class.forName("org.postgresql.Driver");

    when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    boolean result = viewModel.canLogin(cprNumber, password);

    assertFalse(result);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
  }


  //Test failed only because Mockito couldn't resolve our database driver for unknown reasons.
  @Test
  void findDoctor_WhenDoctorExists_ReturnsDoctor() throws SQLException {
    long cprNumber = 1234567890L;
    String password = "password";

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true);
    when(resultSet.getLong(1)).thenReturn(cprNumber);

    Doctor doctor = viewModel.findDoctor(cprNumber, password);

    assertNotNull(doctor);
    assertEquals(cprNumber, doctor.getCprNumber());
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
    verify(resultSet).getLong(1);
    verify(resultSet).getString(2);
  }

  @Test
  void findDoctor_WhenDoctorDoesNotExist_ReturnsNull() throws SQLException {
    long cprNumber = 1234567899;
    String password = "password";

    Connection connection = mock(Connection.class);
    PreparedStatement preparedStatement = mock(PreparedStatement.class);
    ResultSet resultSet = mock(ResultSet.class);

    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(false);

    Doctor doctor = viewModel.findDoctor(cprNumber, password);

    assertNull(doctor);
    verify(connection).prepareStatement(anyString());
    verify(preparedStatement).setLong(1, cprNumber);
    verify(preparedStatement).setString(2, password);
    verify(resultSet).next();
    verifyNoMoreInteractions(resultSet);
  }

}