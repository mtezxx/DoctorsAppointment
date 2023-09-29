package DoctorsAppointment.Server.ServerNetwork;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentState;

import java.rmi.RemoteException;

public interface AppointmentStorage
{

  Appointment getAppointment(long cpr, String name, int phone_number, String symptoms, java.sql.Timestamp date, AppointmentState state) throws RemoteException;

  void storeAppointment(Appointment appointment) throws RemoteException;
  void deleteAppointment(Appointment appointment) throws RemoteException;

}
