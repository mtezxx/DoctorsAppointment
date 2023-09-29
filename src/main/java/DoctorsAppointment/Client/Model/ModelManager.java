package DoctorsAppointment.Client.Model;

import DoctorsAppointment.Client.Network.Client;
import DoctorsAppointment.Server.ServerNetwork.RMIServer;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.ListenerSubject;
import DoctorsAppointment.Shared.Patient;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ModelManager implements Model, ListenerSubject
{
  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  private Client client;


  public ModelManager(Client client)
  {
    this.client = client;
    client.startClient();
    client.addListener("NewAppointment", this::onNewAppointment);
    client.addListener("NewPatient", this::onNewPatient);
    client.addListener("CancelAppointment", this::onCancelAppointment);
    client.addListener("DoctorCancelAppointment", this::onDoctorCancelAppointment);
    client.addListener("ApproveAppointment", this::onApproveAppointment);
    client.addListener("RemoveDay", this::onRemoveDay);
  }

  private void onDoctorCancelAppointment(PropertyChangeEvent event){
    support.firePropertyChange(event);
  }

  private  void onApproveAppointment(PropertyChangeEvent event){
    support.firePropertyChange(event);
  }

  private void onCancelAppointment(PropertyChangeEvent event)
  {
    support.firePropertyChange(event);
  }

  private void onNewAppointment(PropertyChangeEvent event){
    support.firePropertyChange(event);
  }
  private void onNewPatient(PropertyChangeEvent event){
    support.firePropertyChange(event);
  }

  private void onRemoveDay(PropertyChangeEvent event){
    support.firePropertyChange(event);
  }

  @Override public boolean login(Patient patient, RMIServer client)
  {
    return true;
  }

  @Override public void addPatient(Patient patient)
  {
    client.addPatient(patient);
  }

  @Override public void addAppointment(Appointment appointment){
    client.addAppointment(appointment);
  }

  @Override public void cancelAppointment(Appointment appointment)
  {
    client.cancelAppointment(appointment);
  }

  @Override public List<Appointment> getAppointments()
  {
    return client.getAppointments();
  }

  @Override public List<Patient> getPatients()
  {
    return client.getPatients();
  }

  @Override public void approveAppointment(Appointment appointment)
  {
    client.approveAppointment(appointment);
  }

  @Override public void removeDay(Date date)
  {
    client.removeDay(date);
  }

  @Override public void updateWorkingDays(Date date)
  {

  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public void removeListener(String event,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(event, listener);
  }
}
