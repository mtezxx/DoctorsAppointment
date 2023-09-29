package DoctorsAppointment.Server.Model;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.Patient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataModelServerImpl implements DataModelServer
{
  private PropertyChangeSupport support;
  private List<Patient> patients;
  private List<Appointment> appointments;
  private AppointmentModelManager AMM;

  private Model model;
  private Connection conn;
  public DataModelServerImpl() {
    support = new PropertyChangeSupport(this);
    patients = new ArrayList<>();
    appointments = new ArrayList<>();
    AMM = new AppointmentModelManager();
  }

  @Override public synchronized void addPatient(Patient patient)
  {
    patients.add(patient);
    support.firePropertyChange("NewPatient", null, patient);
  }

  @Override public synchronized List<Appointment> getAppointments()
  {
    return new ArrayList<>(appointments);
  }

  @Override public synchronized void addAppointment(Appointment appointment)
  {
    appointments.add(appointment);
    support.firePropertyChange("NewAppointment", null, appointment);
  }

  @Override public synchronized void cancelAppointment(Appointment appointment){

    AMM.cancelAppointment(appointment);
    appointments.remove(appointment);
    support.firePropertyChange("CancelAppointment", null, appointment);
  }

  @Override public synchronized void approveAppointment(Appointment appointment)
  {
    AMM.approveAppointment(appointment);
  }

  @Override public synchronized void updateStatus(Appointment appointment)
  {
    support.firePropertyChange("ApproveAppointment", null, appointment);
  }

  @Override public synchronized void removeDay(Date date)
  {
    AMM.removeDay(date);
    support.firePropertyChange("RemoveDay", null , date);
  }

  @Override public void updateWorkingDays(Date date)
  {

  }

  @Override public synchronized List<Patient> getPatients()
  {
    return new ArrayList<>(patients);
  }

  @Override public synchronized void addListener(String event, PropertyChangeListener listener) {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public synchronized void removeListener(String event, PropertyChangeListener listener) {
    support.removePropertyChangeListener(event, listener);
  }
}
