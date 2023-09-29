package DoctorsAppointment.Server.ServerNetwork;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.ListenerSubject;
import DoctorsAppointment.Shared.Patient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote
{
  void updatePatients(Patient patient) throws
      RemoteException;

  void updateAppointments(Appointment appointment) throws RemoteException;
  void updateReservations(Appointment appointment) throws  RemoteException;
  void approveAppointment(Appointment appointment) throws  RemoteException;
  void updateStatus(Appointment appointment) throws RemoteException;
}
