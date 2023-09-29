package DoctorsAppointment.Client.Network;

import DoctorsAppointment.Server.ServerNetwork.ClientCallback;
import DoctorsAppointment.Server.ServerNetwork.RMIServer;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Patient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class RMIClient implements Client, ClientCallback
{

  private RMIServer server;

  private PropertyChangeSupport support;

  public RMIClient()
  {
    support=new PropertyChangeSupport(this);
  }

  @Override public void addPatient(Patient patient)
  {
    try {
      server.addPatient(patient);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
      throw new RuntimeException("Cannot connect to the server.");
    }
  }

  @Override public void startClient()
  {
    try {
      UnicastRemoteObject.exportObject(this, 0);
      Registry registry = LocateRegistry.getRegistry("localhost", 1099);
      server = (RMIServer) registry.lookup("Server");
      server.registerClient(this);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
    catch (NotBoundException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void addAppointment(Appointment appointment)
  {
    try
    {
      server.addAppointment(appointment);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
      throw new RuntimeException("Cannot connect to the server.");
    }
  }

  @Override public void cancelAppointment(Appointment appointment)
  {
    try{
      server.cancelAppointment(appointment);
    }
    catch(RemoteException e){
      e.printStackTrace();
      throw new RuntimeException("Couldn't delete appointment.");
    }
  }

  @Override public List<Appointment> getAppointments()
  {
    try{
      return server.getAppointments();
    } catch(RemoteException e){
      throw new RuntimeException("Couldn't load appointments.");
    }
  }

  @Override public List<Patient> getPatients()
  {
    try{
      return server.getPatients();
    } catch(RemoteException e){
      throw new RuntimeException("Couldn't load patients.");
    }
  }

  @Override public void approveAppointment(Appointment appointment)
  {
    try
    {
      server.approveAppointment(appointment);
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void updateStatus(Appointment appointment)
  {
    support.firePropertyChange("ApproveAppointment", null, appointment);
  }

  @Override public void removeDay(Date date)
  {
    try{
      server.removeDay(date);
      support.firePropertyChange("RemoveDay", null, date);

    } catch(RemoteException e){
      e.printStackTrace();
    }
  }

  @Override public void updateWorkingDays(Date date)
  {
    support.firePropertyChange("RemoveDay", null, date);
  }

  @Override public void addListener(String event,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(event, listener);
  }

  @Override public void removeListener(String event,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }

  @Override public void updatePatients(Patient patient) throws RemoteException
  {
    support.firePropertyChange("NewPatient", null, patient);
  }

  @Override public void updateAppointments(Appointment appointment)
      throws RemoteException
  {
    support.firePropertyChange("NewAppointment", null, appointment);
  }

  @Override public void updateReservations(Appointment appointment)
      throws RemoteException
  {
    support.firePropertyChange("CancelAppointment", null, appointment);
  }
}
