package DoctorsAppointment.Shared;

import DoctorsAppointment.Server.ServerNetwork.AppointmentStorage;
import DoctorsAppointment.Shared.Appointments.Appointment;
import DoctorsAppointment.Shared.Appointments.AppointmentDAO;
import DoctorsAppointment.Shared.Appointments.AppointmentState;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DatabaseAdapter implements AppointmentStorage
{
  private final AppointmentDAO appointmentDAO;

  public DatabaseAdapter(AppointmentDAO appointmentDAO)
  {
    this.appointmentDAO = appointmentDAO;
  }



  @Override public Appointment getAppointment(long cpr, String name,
      int phone_number, String symptoms, Timestamp date, AppointmentState state)
      throws RemoteException
  {
    try
    {
      return appointmentDAO.readAppointment(cpr,name,phone_number,symptoms,date,state);
    }catch (SQLException e){
      throw new RemoteException(e.getMessage(),e);
    }
  }

  @Override public void storeAppointment(Appointment appointment)
      throws RemoteException
  {
    try
    {
      if (appointmentDAO.readAppointment(appointment.getCpr(), appointment.getName(), appointment.getPhone_number(), appointment.getSymptoms(), appointment.getDate(),appointment.getState())==null)
      {
        appointmentDAO.createAppointment(appointment.getCpr(), appointment.getName(), appointment.getPhone_number(), appointment.getSymptoms(), appointment.getDate(),appointment.getState());
      }
      else
      {
        appointmentDAO.updateAppointment(appointment);
      }
    }catch (SQLException e){
      throw new RemoteException(e.getMessage(), e);
    }
  }

  @Override public void deleteAppointment(Appointment appointment)
      throws RemoteException
  {
    try{
      appointmentDAO.cancelAppointment(appointment);
      appointmentDAO.updateAppointment(appointment);
    } catch(SQLException e){
      throw new RemoteException(e.getMessage(), e);
    }
  }
}
