package DoctorsAppointment.Shared.Appointments;

import DoctorsAppointment.Shared.Appointments.States.Pending;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class Appointment implements Serializable
{
  private long cpr;
  private String name;
  private int phone_number;
  private String symptoms;
  private Timestamp date;
  private AppointmentState state;

  public Appointment(long cpr, String name, int phone_number, String symptoms, Timestamp date) {
    this.cpr = cpr;
    this.name = name;
    this.phone_number = phone_number;
    this.symptoms = symptoms;
    this.date = date;
    this.state = new Pending();
  }

  public Appointment(long cpr, String name, int phone_number, String symptoms, Timestamp date, AppointmentState appointmentState) {
    this.cpr = cpr;
    this.name = name;
    this.phone_number = phone_number;
    this.symptoms = symptoms;
    this.date = date;
    this.state = appointmentState;
  }

  public void setCpr(long cpr)
  {
    this.cpr = cpr;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setPhone_number(int phone_number)
  {
    this.phone_number = phone_number;
  }

  public void setSymptoms(String symptoms)
  {
    this.symptoms = symptoms;
  }

  public void setDate(Timestamp date)
  {
    this.date = date;
  }

  public String getName()
  {
    return name;
  }

  public long getCpr()
  {
    return cpr;
  }

  public int getPhone_number()
  {
    return phone_number;
  }

  public Timestamp getDate()
  {
    return date;
  }

  public String getSymptoms()
  {
    return symptoms;
  }

  public AppointmentState getState()
  {
    return state;
  }

  public void setState(AppointmentState state) {
    if (state != this.state) {
      this.state = state;
      this.state.PENDING(this);
    } else {

    }
  }
}
