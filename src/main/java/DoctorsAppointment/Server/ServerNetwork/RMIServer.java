package DoctorsAppointment.Server.ServerNetwork;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Patient;

import java.rmi.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface RMIServer extends Remote
{
  boolean login(Patient patient, RMIServer client) throws RemoteException;

  void addPatient(Patient patient) throws RemoteException;
  List<Patient> getPatients() throws RemoteException;

  void registerClient(ClientCallback client) throws RemoteException;

  List<Appointment> getAppointments() throws RemoteException;

  void cancelAppointment(Appointment appointment) throws RemoteException;

  void addAppointment(Appointment appointment) throws RemoteException;
  void updateAppointment(Appointment appointment) throws RemoteException;
  void updateReservation(Appointment appointment) throws RemoteException;
  void approveAppointment(Appointment appointment) throws RemoteException;
  void updateStatus(Appointment appointment) throws RemoteException;
  void removeDay(Date date) throws RemoteException;
  void updateWorkingDays(Date date) throws  RemoteException;
}
