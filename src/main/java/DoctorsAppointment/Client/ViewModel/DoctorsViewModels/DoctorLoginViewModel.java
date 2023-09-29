package DoctorsAppointment.Client.ViewModel.DoctorsViewModels;


import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Client.Views.DoctorViews.DoctorLoginController;
import DoctorsAppointment.Shared.Doctor;

import java.rmi.RemoteException;
import java.sql.*;

public class DoctorLoginViewModel
{
  private int cprNumber;
  private String password;
  private Model model;
  private Connection conn;
  private DoctorLoginController controller;

  public DoctorLoginViewModel(Model model) throws RemoteException
  {
    this.model = model;
  }

  public boolean canLogin(long cprNumber, String password){
    try{
      controller = new DoctorLoginController();
      conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
          "postgres", "heslo");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctor WHERE cpr = ? AND password = ?");
      stmt.setLong(1, cprNumber);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
      if(rs.next()){
        System.out.println("Doctor with CPR " + cprNumber + " logged in.");
        return true;
      }
      else{
        System.out.println("Login failed for doctor with CPR " + cprNumber + ".");
        return false;
      }
    }
    catch (SQLException e){
      e.printStackTrace();
      return false;
    }
  }

  public Doctor findDoctor(long cprNumber, String password){
    try{
      conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
          "postgres", "heslo");
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM doctor WHERE cpr = ? AND password = ?");
      stmt.setLong(1, cprNumber);
      stmt.setString(2, password);
      ResultSet rs = stmt.executeQuery();
      if(rs.next()){
        return new Doctor(rs.getLong(1), rs.getString(2));
      } else{
        return null;
      }
    } catch(SQLException e){
      e.printStackTrace();
      return null;
    }
  }
}
