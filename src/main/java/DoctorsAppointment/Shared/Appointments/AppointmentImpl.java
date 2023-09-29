package DoctorsAppointment.Shared.Appointments;

import DoctorsAppointment.Server.ServerNetwork.AppointmentStorage;
import DoctorsAppointment.Shared.Appointments.States.Approved;
import DoctorsAppointment.Shared.Appointments.States.Pending;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

public class AppointmentImpl extends UnicastRemoteObject implements AppointmentDAO
{
  private final AppointmentStorage appointmentStorage;
  private Connection connection;

  public AppointmentImpl(AppointmentStorage appointmentStorage) throws RemoteException
  {
    this.appointmentStorage = appointmentStorage;
  }

  @Override public Appointment createAppointment(long cpr, String name,
      int phone_number, String symptoms, Timestamp date, AppointmentState state)
      throws SQLException, RemoteException
  {
    try
  {
    Appointment appointment = new Appointment(cpr,name,phone_number,symptoms,date,state);
    appointmentStorage.storeAppointment(appointment);
    return appointment;
  }catch (RemoteException e){
      throw new RemoteException(e.getMessage(),e);
    }
  }

  @Override public Appointment readAppointment(long cpr, String name,
      int phone_number, String symptoms, Timestamp date, AppointmentState state)
      throws SQLException
  {
    return null;
  }

  @Override public void updateAppointment(Appointment appointment)
      throws SQLException, RemoteException
  {
    Appointment appointment1 = appointmentStorage.getAppointment(appointment.getCpr(),appointment.getName(),appointment.getPhone_number(),appointment.getSymptoms(),appointment.getDate(),appointment.getState());
    appointment1.setCpr(appointment.getCpr());
    appointment1.setName(appointment.getName());
    appointment1.setPhone_number(appointment.getPhone_number());
    appointment1.setSymptoms(appointment.getSymptoms());
    appointment1.setDate(appointment.getDate());
    appointment1.setState(appointment.getState());
  }

  @Override public ArrayList<Appointment> readPatientsAppointment(long cpr)
      throws SQLException
  {
    return null;
  }

  @Override public void cancelAppointment(Appointment appointment)
  {
    try
    {
      appointmentStorage.deleteAppointment(appointment);
      System.out.println("cancel appointment v AppointmentImpl prebehlo");
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }
  }

  public Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
        "postgres", "heslo");
  }

  @Override public ArrayList<Appointment> getAllAppointments()
  {
    ArrayList<Appointment> allAppointments = new ArrayList<>();
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment WHERE cpr IS NOT NULL");
      ResultSet rs = stmt.executeQuery();
      if(rs.next()){
        Appointment appointment = new Appointment(rs.getLong(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getTimestamp(5), new Pending());
        allAppointments.add(appointment);
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    return allAppointments;
  }

  @Override public void approveAppointment(Appointment appointment)
  {
    appointment.setState(new Approved());
  }

  @Override public void updateStatus(Appointment appointment)
  {
    try
    {
      Appointment appointment1 = appointmentStorage.getAppointment(appointment.getCpr(), appointment.getName(), appointment.getPhone_number(), appointment.getSymptoms(), appointment.getDate(), appointment.getState());
      appointment1.setState(appointment.getState());
    }
    catch (RemoteException e)
    {
      throw new RuntimeException(e);
    }

  }

  @Override public void removeDay(Date date)
  {

  }
}
