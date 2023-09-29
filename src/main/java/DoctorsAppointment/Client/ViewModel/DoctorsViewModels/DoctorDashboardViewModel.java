package DoctorsAppointment.Client.ViewModel.DoctorsViewModels;

import DoctorsAppointment.Client.Model.Model;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.Views.DoctorViews.DoctorDashboardController;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Appointments.States.Approved;
import DoctorsAppointment.Shared.Appointments.States.Canceled;
import DoctorsAppointment.Shared.Appointments.States.Pending;
import DoctorsAppointment.Shared.Doctor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.beans.PropertyChangeEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class DoctorDashboardViewModel
{
  private DoctorDashboardController controller;
  private ObjectProperty<LocalDate> selectedDate;
  private ObjectProperty<LocalTime> selectedTime;
  private StringProperty symptoms;
  private ObservableList<Appointment> appointments;
  private ObservableList<Appointment> listOfApprovedAppointments;
  private Model model;
  private AppointmentModelManager amm;
  public DoctorDashboardViewModel(Model model)
  {
    this.model = model;
    appointments = FXCollections.observableArrayList();
    listOfApprovedAppointments = FXCollections.observableArrayList();
    selectedDate = new SimpleObjectProperty<>();
    selectedTime = new SimpleObjectProperty<>();
    symptoms = new SimpleStringProperty();

    model.addListener("NewAppointment", this::onNewAppointment);
    model.addListener("RemoveDay", this::onRemoveDay);
    model.addListener("ApproveAppointment", this::onApproveAppointment);
    model.addListener("DoctorCancelAppointment", this::onCancelAppointment);
  }

  public ObjectProperty<LocalDate> getSelectedDate(){
    return selectedDate;
  }

  public ObjectProperty<LocalTime> getSelectedTime(){
    return selectedTime;
  }

  public StringProperty getSymptoms(){
    return symptoms;
  }

  public ObservableList<Appointment> getAppointments()
  {
    return appointments;
  }
  public ObservableList<Appointment> getApprovedAppointments(){
    return listOfApprovedAppointments;
  }

  public void loadAppointments(){
    ArrayList<Appointment> listOfAppointments = null;
    AppointmentModelManager amm = new AppointmentModelManager();
    listOfAppointments = amm.getAllAppointments();
    appointments.clear();
    appointments.addAll(listOfAppointments);
  }

  public void loadApprovedAppointments(){
    ArrayList<Appointment> approvedAppointments = null;
    AppointmentModelManager amm = new AppointmentModelManager();
    approvedAppointments = amm.getApprovedAppointments();
    listOfApprovedAppointments.clear();
    listOfApprovedAppointments.addAll(approvedAppointments);
  }



  public void onNewAppointment(PropertyChangeEvent event){
    appointments.add((Appointment) event.getNewValue());
  }

  public void onRemoveDay(PropertyChangeEvent event){
    //LocalDate selectedDate = (LocalDate) event.getNewValue();
    //Date date = Date.valueOf(selectedDate);
    //model.removeDay(date);
  }

  public void removeEntireDay(){
    LocalDate selectedDate = controller.getSelectedDate();
    Date date = Date.valueOf(selectedDate);


    if (selectedDate != null) {
      DayOfWeek dayOfWeek = selectedDate.getDayOfWeek();
      if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
        if (controller.getTimeSlotsList().isEmpty()) {
          // Show information alert if there are no time slots for the selected day
          Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
          informationAlert.setTitle("Information");
          informationAlert.setHeaderText("No Time Slots");
          informationAlert.setContentText("There are no time slots for this day.");
          informationAlert.showAndWait();
        } else {
          // Show confirmation alert before removing the time slots
          Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
          confirmationAlert.setTitle("Confirmation");
          confirmationAlert.setHeaderText("Remove Time Slots");
          confirmationAlert.setContentText("Are you sure you want to remove all time slots for this day?");

          Optional<ButtonType> result = confirmationAlert.showAndWait();
          if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.getTimeSlotsList().clear();
            model.removeDay(date);
          }
        }
      }
    }
  }

  public void approveAppointment(Appointment appointment){
    //DoctorDashboardController controller = new DoctorDashboardController();
    //appointment = controller.getSelectedAppointment();
    appointment.setState(new Approved());
  }

  public void cancelAppointment(Appointment appointment){
    //DoctorDashboardController controller = new DoctorDashboardController();
    //appointment = controller.getSelectedAppointment();
    appointment.setState(new Canceled());
  }

  public void onCancelAppointment(PropertyChangeEvent event){
    model.cancelAppointment((Appointment) event.getNewValue());
    appointments.remove((Appointment) event.getNewValue());

  }

  public void onApproveAppointment(PropertyChangeEvent event){
    model.approveAppointment((Appointment) event.getNewValue());
    appointments.remove((Appointment) event.getNewValue());
    listOfApprovedAppointments.add((Appointment) event.getNewValue());
  }



  public void setController(DoctorDashboardController controller) {
    this.controller = controller;
  }
}
