package DoctorsAppointment.Shared.Appointments.States;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentState;

public class Approved implements AppointmentState
{
  @Override public void CANCELED(Appointment appointment)
  {

  }

  @Override public void APPROVED(Appointment appointment)
  {

  }

  @Override public void PENDING(Appointment appointment)
  {

  }

  @Override public String toString()
  {
    return "APPROVED";
  }
}
