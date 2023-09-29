package DoctorsAppointment.Shared.Appointments;

import DoctorsAppointment.Shared.Appointments.Appointment;

public interface AppointmentState
{
  void CANCELED(Appointment appointment);
  void APPROVED(Appointment appointment);
  void PENDING(Appointment appointment);

  String toString();
}
