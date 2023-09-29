package DoctorsAppointment.Client.Network;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.ListenerSubject;
import DoctorsAppointment.Shared.Patient;

import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface Client extends ListenerSubject
{
  void addPatient(Patient patient);

  void startClient();
  void addAppointment(Appointment appointment);
  void cancelAppointment(Appointment appointment);
  List<Appointment> getAppointments();
  List<Patient> getPatients();
  void approveAppointment(Appointment appointment);
  void updateStatus(Appointment appointment);
  void removeDay(Date date);
  void updateWorkingDays(Date date);
}
