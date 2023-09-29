package DoctorsAppointment.Server.Model;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.ListenerSubject;
import DoctorsAppointment.Shared.Patient;

import java.sql.Date;
import java.util.List;

public interface DataModelServer extends ListenerSubject
{
  void addPatient(Patient patient);

  List<Appointment> getAppointments();

  void addAppointment(Appointment appointment);
  List<Patient> getPatients();
  void cancelAppointment(Appointment appointment);
  void approveAppointment(Appointment appointment);
  void updateStatus(Appointment appointment);
  void removeDay(Date date);
  void updateWorkingDays(Date date);
}
