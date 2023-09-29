package DoctorsAppointment.Shared.Appointments;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public interface AppointmentDAO
{
  Appointment createAppointment(long cpr, String name, int phone_number, String symptoms, java.sql.Timestamp date, AppointmentState state)
      throws SQLException, RemoteException;

  Appointment readAppointment(long cpr, String name, int phone_number, String symptoms, java.sql.Timestamp date, AppointmentState state) throws SQLException;

  void updateAppointment(Appointment appointment)
      throws SQLException, RemoteException;

  ArrayList<Appointment> readPatientsAppointment(long cpr) throws SQLException;

  void cancelAppointment(Appointment appointment);
  ArrayList<Appointment> getAllAppointments();
  void approveAppointment(Appointment appointment);
  void updateStatus(Appointment appointment);
  void removeDay(Date date);
}
