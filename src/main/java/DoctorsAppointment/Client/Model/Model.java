package DoctorsAppointment.Client.Model;

import DoctorsAppointment.Server.ServerNetwork.RMIServer;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.ListenerSubject;
import DoctorsAppointment.Shared.Patient;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

public interface Model extends ListenerSubject
{
  boolean login(Patient patient, RMIServer client);
  void addPatient(Patient patient);
  void addAppointment(Appointment appointment);
  void cancelAppointment(Appointment appointment);

  List<Appointment> getAppointments();
  List<Patient> getPatients();
  void approveAppointment(Appointment appointment);
  void removeDay(Date date);
  void updateWorkingDays(Date date);
}
