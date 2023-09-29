package DoctorsAppointment.Client.Views.DoctorViews;

import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorDashboardViewModel;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Doctor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.print.Doc;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class DoctorDashboardController {

  @FXML
  private ComboBox<LocalTime> timeSlots;

  @FXML
  private DatePicker datePicker;

  @FXML
  private Button removeDay;

  private ObservableList<LocalTime> timeSlotsList;
  private ObservableList<Appointment> appointments;
  private ObservableList<Appointment> approvedAppointments;

  @FXML
  private Button approveButton;

  @FXML
  private Button cancelButton;

  private DoctorDashboardViewModel viewModel;
  private ViewHandler viewHandler;
  private Doctor doctor;
  private AppointmentModelManager appointmentModelManager;
  @FXML private TableView<Appointment> listOfApprovedAppointments;
  @FXML private TableView<Appointment> showAppointments;
  @FXML private TableColumn<Appointment, Integer> numOfAppointment;
  @FXML private TableColumn<Appointment, Long> patientCPR;
  @FXML private TableColumn<Appointment, String> patientName;
  @FXML private TableColumn<Appointment, Integer> patientPhoneNum;
  @FXML private TableColumn<Appointment, String> patientSymptoms;
  @FXML private TableColumn<Appointment, Timestamp> appointmentDate;
  @FXML private TableColumn<Appointment, String> appointmentStatus;

  @FXML private TableColumn<Appointment, Integer> numOfAppointment1;
  @FXML private TableColumn<Appointment, Long> patientCPR1;
  @FXML private TableColumn<Appointment, String> patientName1;
  @FXML private TableColumn<Appointment, Integer> patientPhoneNum1;
  @FXML private TableColumn<Appointment, String> patientSymptoms1;
  @FXML private TableColumn<Appointment, Timestamp> appointmentDate1;
  @FXML private TableColumn<Appointment, String> appointmentStatus1;



  public void init(ViewHandler viewHandler, Doctor doctor, ViewModelFactory vmf) {
    this.viewModel = vmf.getDoctorDashboardVM();
    this.viewHandler = viewHandler;
    this.doctor = doctor;

    viewModel.setController(this);

    appointments = FXCollections.observableArrayList();
    approvedAppointments = FXCollections.observableArrayList();
    showAppointments.setItems(appointments);
    listOfApprovedAppointments.setItems(approvedAppointments);


    timeSlotsList = FXCollections.observableArrayList();
    timeSlots.setItems(timeSlotsList);


    datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
      LocalDate selectedDate = datePicker.getValue();
      System.out.println("Selected Date: " + selectedDate);
    });
    removeDay.setOnAction(this::removeDay);

    viewModel.getSelectedDate().bind(datePicker.valueProperty());
    viewModel.getSelectedTime().bind(timeSlots.valueProperty());

    try {
      appointmentModelManager = (AppointmentModelManager) AppointmentModelManager.getInstance();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    numOfAppointment.setCellValueFactory(cellData -> new SimpleIntegerProperty(appointments.indexOf(cellData.getValue())).add(1).asObject());
    patientName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    patientCPR.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getCpr()));
    patientPhoneNum.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPhone_number()));
    patientSymptoms.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSymptoms()));
    appointmentDate.setCellValueFactory(cellData -> new SimpleObjectProperty<Timestamp>(cellData.getValue().getDate()));
    appointmentStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().toString()));

    numOfAppointment1.setCellValueFactory(cellData -> new SimpleIntegerProperty(approvedAppointments.indexOf(cellData.getValue())).add(1).asObject());
    patientName1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
    patientCPR1.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getCpr()));
    patientPhoneNum1.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPhone_number()));
    patientSymptoms1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSymptoms()));
    appointmentDate1.setCellValueFactory(cellData -> new SimpleObjectProperty<Timestamp>(cellData.getValue().getDate()));
    appointmentStatus1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getState().toString()));

    viewModel.loadAppointments();
    viewModel.loadApprovedAppointments();
    appointments.setAll(viewModel.getAppointments());
    approvedAppointments.setAll(viewModel.getApprovedAppointments());
  }

  @FXML
  void timeSlots(ActionEvent event) {

  }

  @FXML
  void onApprove() {
    Appointment selectedAppointment = getSelectedAppointment();
    if(selectedAppointment != null){
      appointmentModelManager.approveAppointment(showAppointments.getSelectionModel().getSelectedItem());
      appointments.remove(selectedAppointment);
      showAppointments.getItems().remove(selectedAppointment);
      approvedAppointments.add(selectedAppointment);
    }

  }

  @FXML
  void onCancel() {
    Appointment selectedAppointment = getSelectedAppointment();
    if(selectedAppointment != null){
      appointmentModelManager.cancelAppointment(selectedAppointment);
      appointments.remove(selectedAppointment);
      showAppointments.getItems().remove(selectedAppointment);
    }
  }


  @FXML
  void datePicker(ActionEvent event) {
    LocalDate selectedDate = datePicker.getValue();

    timeSlotsList.clear();

    if (selectedDate != null) {
      DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();
      if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY)
      {
        timeSlotsList.addAll(
            LocalTime.parse("08:00"),
            LocalTime.parse("08:30"),
            LocalTime.parse("09:00"),
            LocalTime.parse("09:30"),
            LocalTime.parse("10:00"),
            LocalTime.parse("10:30"),
            LocalTime.parse("11:00"),
            LocalTime.parse("11:30"),
            LocalTime.parse("12:00"),
            LocalTime.parse("12:30"),
            LocalTime.parse("13:00"),
            LocalTime.parse("13:30"),
            LocalTime.parse("14:00"),
            LocalTime.parse("14:30"),
            LocalTime.parse("15:00"),
            LocalTime.parse("15:30"),
            LocalTime.parse("16:00")
        );
      }
    }
  }

  @FXML
  void removeDay(ActionEvent event) {
    viewModel.removeEntireDay();
  }

  public ObservableList<LocalTime> getTimeSlotsList(){
    return timeSlotsList;
  }

  public LocalDate getSelectedDate(){
    return datePicker.getValue();
  }

  public Appointment getSelectedAppointment(){
    return showAppointments.getSelectionModel().getSelectedItem();
  }
}

