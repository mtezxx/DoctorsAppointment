package DoctorsAppointment.Server.ServerNetwork;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Server.Model.DataModelServer;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentDAO;
import DoctorsAppointment.Shared.Appointments.AppointmentModelManager;
import DoctorsAppointment.Shared.DatabaseAdapter;
import DoctorsAppointment.Shared.Patient;

import javax.xml.transform.Result;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIServerImplementation implements RMIServer
{

  private DataModelServer model;

  private Map<ClientCallback, PropertyChangeListener> listeners = new HashMap<>();

  private Connection conn;
  private RMIServer activeUser;
  public RMIServerImplementation(DataModelServer model) throws RemoteException{
    UnicastRemoteObject.exportObject( this,0);
    this.model = model;
  }

  public void startServer()
      throws RemoteException, AlreadyBoundException, SQLException
  {
    AppointmentDAO appointmentDAO = AppointmentModelManager.getInstance();
    AppointmentStorage appointmentStorage = new DatabaseAdapter(appointmentDAO);

    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("Server", this);
  }

  @Override public synchronized boolean login(Patient patient, RMIServer client) throws RemoteException
  {
    try{
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patient WHERE cpr = ? AND password = ?");
      stmt.setLong(1, patient.getCprNumber());
      stmt.setString(2, patient.getPassword());
      ResultSet rs = stmt.executeQuery();
      boolean result = rs.next();
      rs.close();
      stmt.close();

      if(result){
        activeUser = client;
      }
      return result;
    } catch (SQLException e){
      e.printStackTrace();
      return false;
    }



  }

  public synchronized void logOut(Patient patient) throws RemoteException {
    activeUser = null;
  }

  public RMIServer getActiveUser()
  {
    return activeUser;
  }


  @Override public synchronized void registerClient(ClientCallback client)
      throws RemoteException
  {
    PropertyChangeListener listener = new PropertyChangeListener()
    {
      @Override public void propertyChange(PropertyChangeEvent evt)
      {
        try{
          if(evt.getPropertyName().equals("NewAppointment")){
            client.updateAppointments((Appointment) evt.getNewValue());
          } else if(evt.getPropertyName().equals("NewPatient")){
            client.updatePatients((Patient) evt.getNewValue());
          } else if(evt.getPropertyName().equals("CancelAppointment")){
            client.updateReservations((Appointment) evt.getNewValue());
          }
        }
        catch (RemoteException e)
        {
          e.printStackTrace();
          System.out.println("Runtime exception.");
          throw new RuntimeException(e);
        }

      }
    };
    listeners.put(client, listener);
    model.addListener("NewAppointment", listener);
    model.addListener("NewPatient", listener);
    model.addListener("CancelAppointment", listener);
  }

  @Override public  List<Appointment> getAppointments() throws RemoteException
  {
    return model.getAppointments();
  }

  @Override public synchronized void cancelAppointment(Appointment appointment)
      throws RemoteException
  {
    model.cancelAppointment(appointment);
  }

  @Override public synchronized void addAppointment(Appointment appointment)
      throws RemoteException
  {
    model.addAppointment(appointment);
  }

  @Override public synchronized void updateAppointment(Appointment appointment)
      throws RemoteException
  {
    try{
      PreparedStatement stmt = conn.prepareStatement("UPDATE appointment SET status = ? WHERE cpr = ?, name = ?, phone_number = ?, symptoms = ?, date = ?");
      stmt.setString(1, appointment.getState().toString());
      stmt.setString(3, appointment.getName());
      stmt.setInt(4, appointment.getPhone_number());
      stmt.setLong(2, appointment.getCpr());
      stmt.setString(5, appointment.getSymptoms());
      stmt.setTimestamp(6, appointment.getDate());

      // Execute the update statement
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 1) {
        System.out.println("Appointment updated successfully.");
      } else {
        System.out.println("No rows affected. The appointment may not exist.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      // Handle the exception appropriately
    }
  }

  @Override
  public synchronized void updateReservation(Appointment appointment) throws RemoteException {
    try {
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM appointment WHERE cpr=? AND name=? AND phone_number=? AND symptoms=? AND date=?");
      stmt.setLong(1, appointment.getCpr());
      stmt.setString(2, appointment.getName());
      stmt.setInt(3, appointment.getPhone_number());
      stmt.setString(4, appointment.getSymptoms());
      stmt.setTimestamp(5, appointment.getDate());

      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        long cpr = rs.getLong("cpr");
        String name = rs.getString("name");
        int phoneNumber = rs.getInt("phone_number");
        String symptoms = rs.getString("symptoms");
        Timestamp date = rs.getTimestamp("date");

        PreparedStatement delete = conn.prepareStatement("DELETE FROM appointment WHERE cpr=? AND name=? AND phone_number=? AND symptoms=? AND date=?");
        delete.setLong(1, cpr);
        delete.setString(2, name);
        delete.setInt(3, phoneNumber);
        delete.setString(4, symptoms);
        delete.setTimestamp(5, date);
        delete.executeUpdate();
        delete.close();
      }
      stmt.close();

      model.cancelAppointment(appointment);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error while updating reservation.");
    }
  }

  @Override public synchronized void approveAppointment(Appointment appointment)
      throws RemoteException
  {
    model.approveAppointment(appointment);
  }

  @Override public synchronized void updateStatus(Appointment appointment)
      throws RemoteException
  {
    try{
      PreparedStatement stmt = conn.prepareStatement("UPDATE appointment SET status=? WHERE cpr=?, name=?, phone_number=?, symptoms=?, date=?");
      stmt.setString(1, appointment.getState().toString());
      stmt.setLong(2, appointment.getCpr());
      stmt.setString(3, appointment.getName());
      stmt.setInt(4, appointment.getPhone_number());
      stmt.setString(5, appointment.getSymptoms());
      stmt.setTimestamp(6, appointment.getDate());
      stmt.executeUpdate();
      stmt.close();
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  @Override public void removeDay(Date date) throws RemoteException
  {
    model.removeDay(date);
  }

  @Override public void updateWorkingDays(Date date) throws RemoteException
  {

  }

  @Override public synchronized void addPatient(Patient patient) throws RemoteException
  {
    model.addPatient(patient);
  }

  @Override public synchronized List<Patient> getPatients() throws RemoteException{
    return model.getPatients();
  }
}
