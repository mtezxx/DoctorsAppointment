package DoctorsAppointment.Client.Views.PatientViews;

import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientDashboardViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Appointments.States.Pending;
import DoctorsAppointment.Shared.Patient;
import DoctorsAppointment.Shared.PatientModelManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.beans.PropertyChangeEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;

public class PatientDashboardController
{
  @FXML
  private DatePicker calendar;

  @FXML
  private Button reserveBtn;

  @FXML
  private ComboBox<LocalTime> timeSlots;

  @FXML
  private TextField symptomsTextField;

  @FXML
  private TableView<Appointment> showAppointments;

  @FXML
  private TableColumn<Appointment, Integer> appointmentNumber;
  @FXML
  private TableColumn<Appointment, Timestamp> appointmentDateColumn;

  @FXML
  private TableColumn<Appointment, String> appointmentStatusColumn;
  @FXML
  private Button cancelButton;
  private ViewHandler viewHandler;
  private PatientDashboardViewModel viewModel;

  private AppointmentModelManager appointmentModelManager;

  private Patient patient;

  private ObservableList<LocalTime> timeSlotsList;
  private ObservableList<Appointment> appointmentsList;
  private ObservableList<Date> listOfDaysOff;

  public void init(ViewHandler viewHandler, Patient patient, ViewModelFactory vmf) {
    this.viewModel = vmf.getPatientDashboardVM();
    this.viewHandler = viewHandler;
    this.patient = patient;
    viewModel.setController(this);

    listOfDaysOff = FXCollections.observableArrayList();

    calendar.valueProperty().addListener((observable, oldValue, newValue) -> {
          LocalDate selectedDate = calendar.getValue();
          System.out.println("Selected Date: " + selectedDate);
        });


    viewModel.selectedDateProperty().bind(calendar.valueProperty());

    viewModel.selectedTimeSlotProperty().bind(timeSlots.valueProperty());


    timeSlotsList = FXCollections.observableArrayList();


    timeSlots.setItems(timeSlotsList);

    appointmentsList = FXCollections.observableArrayList();
    showAppointments.setItems(appointmentsList);

    viewModel.setLoggedInPatient(patient);


    symptomsTextField.textProperty().bindBidirectional(viewModel.symptomsProperty());

    try {
      appointmentModelManager = (AppointmentModelManager) AppointmentModelManager.getInstance();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    appointmentDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
    appointmentStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().toString()));
    appointmentNumber.setCellValueFactory(cellData -> new SimpleIntegerProperty(appointmentsList.indexOf(cellData.getValue())).add(1).asObject());

    viewModel.loadAppointments();
    viewModel.loadTimeslots();
    viewModel.loadAllDaysOff();
    timeSlotsList.setAll(viewModel.displayAvailableTimeslots());
    listOfDaysOff.setAll(viewModel.loadAllDaysOff());
    appointmentsList.setAll(viewModel.getAppointments());
  }

  @FXML
  void handleCalendar(ActionEvent event) {
    LocalDate selectedDate = calendar.getValue();
    Date date = Date.valueOf(selectedDate);

    timeSlotsList.clear();

    if (selectedDate != null) {
      DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();
      if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)
      {
        boolean flag = true;
        for(int i = 0; i<listOfDaysOff.size(); i++)
        {
          if (date.equals(listOfDaysOff.get(i)))
          {
            flag = false;
            break;
          }
        }
        if(flag){
          timeSlotsList.addAll(viewModel.displayAvailableTimeslots());
        }

      }
    }
  }

  @FXML
  void handleTimeSlots(ActionEvent event) {

  }

  @FXML
  void handleReserveBtn(ActionEvent event) {
    long cprNumber = patient.getCprNumber();
    int phoneNumber = patient.getPhoneNumber();
    String name = "" + patient.getFirstName() + " " + patient.getLastName();
    LocalDate selectedDate = calendar.getValue();
    LocalTime selectedTime = timeSlots.getValue();
    String symptoms = symptomsTextField.getText();

    LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);
    java.sql.Timestamp sqlTimestamp = java.sql.Timestamp.valueOf(selectedDateTime);
    Appointment appointment = new Appointment(cprNumber, name, phoneNumber, symptoms, sqlTimestamp, new Pending());

    viewModel.createAppointment();
    symptomsTextField.setText("");
  }




  @FXML
  void cancelReservation() {
    Appointment selectedAppointment = getSelectedAppointment();
    if (selectedAppointment != null) {
      appointmentModelManager.cancelAppointment(selectedAppointment);
      appointmentsList.remove(selectedAppointment);
      showAppointments.getItems().remove(selectedAppointment);
    }
  }

  public Appointment getSelectedAppointment(){
    return showAppointments.getSelectionModel().getSelectedItem();
  }

  public LocalDate getSelectedDay(){
    return calendar.getValue();
  }

  public LocalTime getSelectedTimeslot(){
    return timeSlots.getValue();
  }

  public void addToAppointmentsList(Appointment appointment){
    appointmentsList.add(appointment);
  }
}
