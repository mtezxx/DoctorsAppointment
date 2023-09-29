package DoctorsAppointment.Shared;

import java.sql.*;

public class PatientModelManager implements PatientDAO
{
  private static PatientDAO instance;

  private PatientModelManager() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public synchronized static PatientDAO getInstance() throws SQLException
  {
    if (instance==null){
      instance=new PatientModelManager();
    }
    return instance;
  }

  public Connection getConnection() throws SQLException
  {
   return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
       "postgres", "heslo");
  }

  @Override public Patient createPatient(long cprNumber, int phoneNumber,
      String firstName, String lastName, String password) throws SQLException
  {
    try(Connection connection=getConnection())
    {
      PreparedStatement statement=connection.prepareStatement("INSERT INTO patient VALUES (?,?,?,?,?)");
      statement.setLong(1, cprNumber );
      statement.setString(2, firstName);
      statement.setString(3, lastName);
      statement.setInt(4, phoneNumber);
      statement.setString(5, password);
      statement.executeUpdate();
      return new Patient(cprNumber,phoneNumber,firstName,lastName,password);
    }
  }
}

