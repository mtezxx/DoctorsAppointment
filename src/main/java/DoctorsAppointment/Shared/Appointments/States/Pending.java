package DoctorsAppointment.Shared.Appointments.States;

import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentState;

import java.io.Serializable;

public class Pending implements AppointmentState, Serializable
{

  @Override public void CANCELED(Appointment appointment)
  {
    appointment.setState(new Canceled());
    System.out.println("Canceled");
  }

  @Override public void APPROVED(Appointment appointment)
  {
    appointment.setState(new Approved());
    System.out.println("Approved");
  }

  @Override public void PENDING(Appointment appointment)
  {

  }

  @Override public String toString()
  {
    return "PENDING";
  }
}
