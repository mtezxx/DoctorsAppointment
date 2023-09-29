import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientDashboardViewModel;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PatientDashboardViewModelTest {
  private PatientDashboardViewModel viewModel;
  private Model model;
  private PatientDashboardController controller;

  private AppointmentModelManager AMM;


  @BeforeEach
  void setUp() {
    model = mock(Model.class);
    viewModel = new PatientDashboardViewModel(model);
    controller = mock(PatientDashboardController.class);
    viewModel.setController(controller);
  }

  @Test
  void testSelectedDateProperty() {
    ObjectProperty<LocalDate> selectedDateProperty = viewModel.selectedDateProperty();
    LocalDate date = LocalDate.of(2023, 5, 30);

    selectedDateProperty.set(date);

    assertEquals(date, selectedDateProperty.get());
  }

  @Test
  void testSelectedTimeSlotProperty() {
    ObjectProperty<LocalTime> selectedTimeSlotProperty = viewModel.selectedTimeSlotProperty();
    LocalTime timeSlot = LocalTime.of(14, 30);

    selectedTimeSlotProperty.set(timeSlot);

    assertEquals(timeSlot, selectedTimeSlotProperty.get());
  }

  @Test
  void testSymptomsProperty() {
    StringProperty symptomsProperty = viewModel.symptomsProperty();
    String symptoms = "Headache";

    symptomsProperty.set(symptoms);

    assertEquals(symptoms, symptomsProperty.get());
  }

  @Test
  void testGetAppointments() throws ParseException
  {
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp = new Timestamp(currentDate.getTime());

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate1 = dateFormat.parse("2023-05-28 15:30:00");

    Timestamp timestamp1 = new Timestamp(currentDate.getTime());


    appointments.add(new Appointment(1234567890, "Matej Kucera", 12345678, "Symptoms",timestamp));
    appointments.add(new Appointment(1234567899, "Marek Stransky", 87654321, "Symptoms",timestamp1));
    viewModel.getAppointments().addAll(appointments);

    ObservableList<Appointment> result = viewModel.getAppointments();

    assertEquals(appointments, result);
  }

  @Test
  void displayAvailableTimeslots_WhenNoAppointments_ReturnsAllTimeslots() {
    LocalDate selectedDate = LocalDate.of(2023, 5, 16);
    List<LocalTime> allTimeslots = createAllTimeslots();

    viewModel.selectedDateProperty().set(selectedDate);

    ArrayList<LocalTime> availableTimeslots = viewModel.displayAvailableTimeslots();

    assertEquals(allTimeslots.size(), availableTimeslots.size());
    assertTrue(availableTimeslots.containsAll(allTimeslots));
  }

  @Test
  void displayAvailableTimeslots_WhenAppointmentsExist_RemovesReservedTimeslots() {
    List<LocalTime> allTimeslots = createAllTimeslots();
    List<LocalTime> reservedTimeslots = createReservedTimeslots();
    List<LocalTime> expectedAvailableTimeslots = new ArrayList<>(allTimeslots);
    expectedAvailableTimeslots.removeAll(reservedTimeslots);

    ArrayList<LocalTime> availableTimeslots = viewModel.displayAvailableTimeslots();
    availableTimeslots.removeAll(reservedTimeslots);

    assertEquals(expectedAvailableTimeslots.size(), availableTimeslots.size());
    assertTrue(availableTimeslots.containsAll(expectedAvailableTimeslots));
  }



  private List<LocalTime> createAllTimeslots() {
    List<LocalTime> allTimeslots = new ArrayList<>();
    allTimeslots.add(LocalTime.parse("08:00"));
    allTimeslots.add(LocalTime.parse("08:30"));
    allTimeslots.add(LocalTime.parse("09:00"));
    allTimeslots.add(LocalTime.parse("09:30"));
    allTimeslots.add(LocalTime.parse("10:00"));
    allTimeslots.add(LocalTime.parse("10:30"));
    allTimeslots.add(LocalTime.parse("11:00"));
    allTimeslots.add(LocalTime.parse("11:30"));
    allTimeslots.add(LocalTime.parse("12:00"));
    allTimeslots.add(LocalTime.parse("12:30"));
    allTimeslots.add(LocalTime.parse("13:00"));
    allTimeslots.add(LocalTime.parse("13:30"));
    allTimeslots.add(LocalTime.parse("14:00"));
    allTimeslots.add(LocalTime.parse("14:30"));
    allTimeslots.add(LocalTime.parse("15:00"));
    allTimeslots.add(LocalTime.parse("15:30"));
    allTimeslots.add(LocalTime.parse("16:00"));
    return allTimeslots;
  }

  private List<LocalTime> createReservedTimeslots() {
    List<LocalTime> reservedTimeslots = new ArrayList<>();
    reservedTimeslots.add(LocalTime.parse("08:30"));
    reservedTimeslots.add(LocalTime.parse("10:00"));
    reservedTimeslots.add(LocalTime.parse("11:30"));
    return reservedTimeslots;
  }




}