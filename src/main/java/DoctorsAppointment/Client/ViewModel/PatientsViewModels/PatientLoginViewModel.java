package DoctorsAppointment.Client.ViewModel.PatientsViewModels;

import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.Views.PatientViews.PatientLoginController;
import DoctorsAppointment.Shared.Patient;

import java.rmi.RemoteException;
import java.sql.*;

public class PatientLoginViewModel
{
  private long cpr;
  private String password;
  private Model model;
  private Connection conn;
  private PatientLoginController controller;


  public PatientLoginViewModel(Model model) throws RemoteException
  {
    this.model = model;
  }

  public Patient findPatient(long cprNumber, String password){
    try
    {
      conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
          "postgres", "heslo");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patient WHERE cpr = ? AND password = ?");
      stmt.setLong(1, cprNumber);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
      if(rs.next()){
        return new Patient(rs.getLong(1), rs.getInt(4), rs.getString(3), rs.getString(2), rs.getString(5));
      } else{
        return null;
      }
    } catch(SQLException e){
      e.printStackTrace();
      return null;
    }
  }

  public boolean login(long cpr, String password)
  {

    try
    {
      controller = new PatientLoginController();
      conn = DriverManager.getConnection(
          "jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
          "postgres", "heslo");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patient WHERE cpr = ? AND password = ?");
      stmt.setLong(1, cpr);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
        if (rs.next())
        {
          // CPR and password are correct, so return true to indicate successful login
          System.out.println("User with CPR " + cpr + " logged in.");
          return true;
        }
        else
        {
          // CPR or password is incorrect
          System.out.println("Login failed for user with CPR " + cpr);
          return false;
        }

    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }
}

