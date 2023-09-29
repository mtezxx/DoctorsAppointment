
import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorDashboardViewModel;
import DoctorsAppointment.Client.Views.DoctorViews.DoctorDashboardController;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.States.Approved;
import DoctorsAppointment.Shared.Appointments.States.Canceled;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.beans.PropertyChangeEvent;

import java.sql.Timestamp;
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

class DoctorDashboardViewModelTest
{

  @Mock
  private Model model;

  private DoctorDashboardViewModel viewModel;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    viewModel = new DoctorDashboardViewModel(model);
  }

  @Test
  void testGetSymptoms() {
    String symptoms = "Fever, headache";
    viewModel.getSymptoms().set(symptoms);

    assertEquals(symptoms, viewModel.getSymptoms().get());
  }

  @Test
  void testGetSelectedDate() {
    LocalDate date = LocalDate.now();
    viewModel.getSelectedDate().set(date);

    assertEquals(date, viewModel.getSelectedDate().get());
  }

  @Test
  void testGetSelectedTime() {
    LocalTime time = LocalTime.now();
    viewModel.getSelectedTime().set(time);
    assertEquals(time, viewModel.getSelectedTime().get());
  }

  @Test
  void testOnNewAppointment() throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    Appointment appointment = new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms", timestamp);
    viewModel.onNewAppointment(new PropertyChangeEvent(this, "NewAppointment", null, appointment));

    assertEquals(1, viewModel.getAppointments().size());
    assertEquals(appointment, viewModel.getAppointments().get(0));
  }


  @Test
  void testApproveAppointment() throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    Appointment appointment = new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms", timestamp);

    viewModel.approveAppointment(appointment);

    assertEquals(Approved.class, appointment.getState().getClass());
  }


  @Test
  void testCancelAppointment() throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    Appointment appointment = new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms", timestamp);


    viewModel.cancelAppointment(appointment);

    assertEquals(Canceled.class, appointment.getState().getClass());
  }

  @Test
  void testOnCancelAppointment() throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    Appointment appointment = new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms", timestamp);

    viewModel.getAppointments().add(appointment);

    viewModel.onCancelAppointment(new PropertyChangeEvent(this, "DoctorCancelAppointment", null, appointment));

    assertEquals(0, viewModel.getAppointments().size());
    verify(model, times(1)).cancelAppointment(appointment); // Verify that the model's cancelAppointment method is called once
  }

  @Test
  void testOnApproveAppointment() throws ParseException
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    Appointment appointment = new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms", timestamp);

    viewModel.getAppointments().add(appointment);

    viewModel.onApproveAppointment(new PropertyChangeEvent(this, "ApproveAppointment", null, appointment));

    assertEquals(0, viewModel.getAppointments().size());
    assertEquals(1, viewModel.getApprovedAppointments().size());
    assertEquals(appointment, viewModel.getApprovedAppointments().get(0));
    verify(model, times(1)).approveAppointment(appointment);
  }
}
