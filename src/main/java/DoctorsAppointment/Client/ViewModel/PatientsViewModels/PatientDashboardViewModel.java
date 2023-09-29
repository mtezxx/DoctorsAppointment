package DoctorsAppointment.Client.ViewModel.PatientsViewModels;


import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.Views.PatientViews.PatientDashboardController;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Appointments.States.Pending;
import DoctorsAppointment.Shared.Patient;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.beans.PropertyChangeEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class PatientDashboardViewModel
{

  private PatientDashboardController controller;
  private ObjectProperty<LocalDate> selectedDateProperty;
  private ObjectProperty<LocalTime> selectedTimeSlotProperty;
  private StringProperty symptoms;

  private ObservableList<Appointment> appointments;
  private Model model;
  private Patient loggedInPatient;
  private AppointmentModelManager AMM;
  private ArrayList<Date> listOfDaysOff;

  public PatientDashboardViewModel(Model model)
  {
    this.model = model;
    appointments = FXCollections.observableArrayList();
    selectedDateProperty = new SimpleObjectProperty<>();
    selectedTimeSlotProperty = new SimpleObjectProperty<>();
    symptoms = new SimpleStringProperty();

    model.addListener("NewAppointment", this::onNewAppointment);
    model.addListener("CancelAppointment", this::onCancelAppointment);
  }



  public ObjectProperty<LocalDate> selectedDateProperty() {
    return selectedDateProperty;
  }

  public void setSelectedDateProperty(ObjectProperty<LocalDate> selectedDateProperty){
    this.selectedDateProperty = selectedDateProperty;
  }

  public ObjectProperty<LocalTime> selectedTimeSlotProperty() {
    return selectedTimeSlotProperty;
  }

  public StringProperty symptomsProperty() {
    return symptoms;
  }

  public ObservableList<Appointment> getAppointments(){
    return appointments;
  }

  public void loadAppointments()
  {
    ArrayList<Appointment> listOfAppointments = new ArrayList<>();
    try
    {
      AppointmentModelManager AMM = new AppointmentModelManager();
      listOfAppointments = AMM.readPatientsAppointment(loggedInPatient.getCprNumber());
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
    appointments.clear();
    appointments.addAll(listOfAppointments);
  }

  public ArrayList<Date> loadAllDaysOff(){
    ArrayList<Date> daysOff = new ArrayList<>();
    AppointmentModelManager amm = new AppointmentModelManager();
    daysOff = amm.getDaysOff();
    return daysOff;
    }


  public void onNewAppointment(PropertyChangeEvent event){
    appointments.add((Appointment) event.getNewValue());
  }

  public void setLoggedInPatient(Patient patient) {
    this.loggedInPatient = patient;
  }

  public void createAppointment() {
    AppointmentModelManager amm = new AppointmentModelManager();
    long cprNumber = loggedInPatient.getCprNumber();
    String name = loggedInPatient.getFirstName() + " " + loggedInPatient.getLastName();
    int phoneNumber = loggedInPatient.getPhoneNumber();
    LocalDate selectedDay = controller.getSelectedDay();
    LocalTime selectedTimeslot = controller.getSelectedTimeslot();

    if (isDateInPast(selectedDay)) {
      System.out.println("Cannot reserve appointments in the past.");
      showErrorPopup("Cannot reserve appointments in the past.");
      return;
    }

    if (!isAppointmentAlreadyCreated(selectedDay) && !isAppointmentAlreadyScheduled(selectedDay, selectedTimeslot)) {
      Timestamp sqlTimestamp = convertToLocalDateTime(selectedDay, selectedTimeslot);
      Appointment appointment = new Appointment(cprNumber, name, phoneNumber, symptoms.get(), sqlTimestamp, new Pending());
      model.addAppointment(appointment);
      controller.addToAppointmentsList(appointment);
      try{
        amm.createAppointment(cprNumber, name, phoneNumber, symptoms.get(), sqlTimestamp, new Pending());
      } catch(SQLException e){
        e.printStackTrace();
      }
    } else {
      showErrorPopup("You are trying to exceed the limit of allowed reservations per day / this reservation is already taken.");
      System.out.println("You are trying to exceed the limit of allowed reservations per day / this reservation is already taken.");
    }
  }


  public Timestamp convertToLocalDateTime(LocalDate date, LocalTime time) {
    LocalDateTime localDateTime = LocalDateTime.of(date, time);
    return Timestamp.valueOf(localDateTime);
  }

  public void cancelAppointment(){
    model.cancelAppointment(controller.getSelectedAppointment());
    appointments.remove(controller.getSelectedAppointment());
  }

  public void onCancelAppointment(PropertyChangeEvent event){
    AMM.cancelAppointment((Appointment) event.getNewValue());
    appointments.remove((Appointment)event.getNewValue());
    model.cancelAppointment((Appointment) event.getNewValue());
  }

  public boolean isAppointmentAlreadyCreated(LocalDate selectedDate) {
    for (Appointment appointment : appointments) {
      Timestamp appointmentTimestamp = appointment.getDate();
      LocalDate appointmentDate = appointmentTimestamp.toLocalDateTime().toLocalDate();

      if (appointmentDate.isEqual(selectedDate)) {
        return true;
      }
    }

    return false;
  }

  public void setController(PatientDashboardController controller){
    this.controller = controller;
  }

  public boolean isAppointmentAlreadyScheduled(LocalDate selectedDate, LocalTime selectedTimeSlot) {
    AppointmentModelManager AMM = new AppointmentModelManager();
    ArrayList<Appointment> allAppointments = AMM.getEverySingleAppointment();

    LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTimeSlot);

    for (Appointment appointment : allAppointments) {
      Timestamp appointmentTimestamp = appointment.getDate();
      LocalDateTime appointmentDateTime = appointmentTimestamp.toLocalDateTime();

      if (appointmentDateTime.toLocalDate().equals(selectedDate) &&
          appointmentDateTime.toLocalTime().equals(selectedTimeSlot)) {
        return true;
      }
    }

    return false;
  }

  public boolean isDateInPast(LocalDate selectedDate) {
    LocalDate currentDate = LocalDate.now();
    return selectedDate.isBefore(currentDate);
  }

  private void showErrorPopup(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public ArrayList<LocalTime> loadTimeslots(){
    ArrayList<LocalTime> timeslots = new ArrayList<>();
    AppointmentModelManager amm = new AppointmentModelManager();
    timeslots = amm.getTimeslots();
    return timeslots;
  }

  public ArrayList<LocalTime> displayAvailableTimeslots() {
    ArrayList<LocalTime> allTimeslots = new ArrayList<>();
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


    ArrayList<LocalTime> availableTimeslots = new ArrayList<>(allTimeslots);
    availableTimeslots.removeIf(timeslot -> isTimeslotReserved(controller.getSelectedDay(), timeslot));
    return availableTimeslots;
  }

  public boolean isTimeslotReserved(LocalDate date, LocalTime timeslot) {
    for (Appointment appointment : appointments) {
      LocalDateTime appointmentDateTime = appointment.getDate().toLocalDateTime();
      LocalDate appointmentDate = appointmentDateTime.toLocalDate();
      LocalTime appointmentTime = appointmentDateTime.toLocalTime();

      if (appointmentDate.equals(date) && appointmentTime.equals(timeslot)) {
        return true;
      }
    }
    return false;
  }

}
