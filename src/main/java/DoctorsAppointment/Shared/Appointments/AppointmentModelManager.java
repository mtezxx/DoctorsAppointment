package DoctorsAppointment.Shared.Appointments;

import DoctorsAppointment.Shared.Appointments.States.Approved;
import DoctorsAppointment.Shared.Appointments.States.Canceled;
import DoctorsAppointment.Shared.Appointments.States.Pending;
import DoctorsAppointment.Shared.Patient;
import DoctorsAppointment.Shared.PatientDAO;
import DoctorsAppointment.Shared.PatientModelManager;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class AppointmentModelManager implements AppointmentDAO
{

  private static AppointmentDAO instance;

  public AppointmentModelManager() //throws SQLException
  {
    try{
      DriverManager.registerDriver(new org.postgresql.Driver());
    } catch (SQLException e){
      e.printStackTrace();
    }
  }

  public synchronized static AppointmentDAO getInstance() throws SQLException
  {
    if (instance==null){
      instance=new AppointmentModelManager();
    }
    return instance;
  }

  public Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=doctors_appointment",
        "postgres", "heslo");
  }

  @Override public Appointment createAppointment(long cpr, String name, int phone_number, String symptoms, java.sql.Timestamp date, AppointmentState state) throws SQLException
  {
    try(Connection connection=getConnection())
    {

      PreparedStatement statement=connection.prepareStatement("INSERT INTO appointment (cpr, name, phone_number, symptoms, date, status) VALUES (?,?,?,?,?,?)");
      statement.setLong(1, cpr );
      statement.setString(2, name);
      statement.setInt(3, phone_number);
      statement.setString(4, symptoms);
      statement.setTimestamp(5, date);
      statement.setString(6, state.toString());
      statement.executeUpdate();
      statement.close();
      PreparedStatement stmt = connection.prepareStatement("INSERT INTO reservedtimeslots (date) VALUES (?)");
      stmt.setTimestamp(1, date);
      stmt.executeUpdate();
      stmt.close();
      return new Appointment(cpr,name,phone_number,symptoms,date);
    }
  }

  @Override public Appointment readAppointment(long cpr, String name,
      int phone_number, String symptoms, Timestamp date, AppointmentState state)
      throws SQLException
  {
    try(Connection connection=getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointment WHERE cpr = ?");
      statement.setLong(1, cpr);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        String namePatient = resultSet.getString("name");
        int phoneNumber = resultSet.getInt("phone_number");
        String symptomsPatient = resultSet.getString("symptoms");
        Timestamp appointmentDate = resultSet.getTimestamp("date");
        String appointmentState = resultSet.getString("status");
        AppointmentState appointmentStateObj;

        if (appointmentState.equals("PENDING")) {
          appointmentStateObj = new Pending();
        } else if (appointmentState.equals("CANCELED")) {
          appointmentStateObj = new Canceled();
        } else if (appointmentState.equals("APPROVED")) {
          appointmentStateObj = new Approved();
        } else {
          appointmentStateObj = null;
        }

        return new Appointment(cpr, namePatient, phoneNumber, symptomsPatient, appointmentDate, appointmentStateObj);
      } else {
        return null;
      }
    }
  }

  @Override public void updateAppointment(Appointment appointment)
      throws SQLException
  {
    try (Connection connection = getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE appointment SET name = ?, phone_number = ?, symptoms = ?, date = ?, status = ? WHERE cpr = ?")) {
      preparedStatement.setString(1, appointment.getName());
      preparedStatement.setInt(2, appointment.getPhone_number());
      preparedStatement.setString(3, appointment.getSymptoms());
      preparedStatement.setTimestamp(4, appointment.getDate());
      preparedStatement.setString(5, appointment.getState().toString());
    }
  }

  @Override public ArrayList<Appointment> readPatientsAppointment(long cpr) throws SQLException
  {
    ArrayList<Appointment> listOfPatientsAppointments = new ArrayList<>();
    Appointment appointment;
    try(Connection connection=getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointment WHERE cpr = ?");
      statement.setLong(1, cpr);
      ResultSet resultSet = statement.executeQuery();


      while (resultSet.next()) {
        String namePatient = resultSet.getString("name");
        int phoneNumber = resultSet.getInt("phone_number");
        String symptomsPatient = resultSet.getString("symptoms");
        Timestamp appointmentDate = resultSet.getTimestamp("date");
        String appointmentState = resultSet.getString("status");
        AppointmentState appointmentStateObj;

        if (appointmentState.equals("PENDING")) {
          appointmentStateObj = new Pending();
        } else if (appointmentState.equals("CANCELED")) {
          appointmentStateObj = new Canceled();
        } else if (appointmentState.equals("APPROVED")) {
          appointmentStateObj = new Approved();
        } else {
          appointmentStateObj = null;
        }

        appointment = new Appointment(cpr, namePatient, phoneNumber, symptomsPatient, appointmentDate, appointmentStateObj);
        listOfPatientsAppointments.add(appointment);
      }
    }
    return listOfPatientsAppointments;
  }

  @Override public void cancelAppointment(Appointment appointment)
  {
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment WHERE cpr=? AND date=?");
      stmt.setLong(1, appointment.getCpr());
      stmt.setTimestamp(2, appointment.getDate());
      ResultSet rs = stmt.executeQuery();

      if(rs.next()){
        PreparedStatement delete = connection.prepareStatement("DELETE FROM appointment WHERE cpr=? AND date=?");
        delete.setLong(1, rs.getLong("cpr"));
        delete.setTimestamp(2, rs.getTimestamp("date"));
        delete.executeUpdate();
        delete.close();
      }

      stmt.close();
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println("Cancelled.");
  }

  public int getNumOfReservationsPatient(long cpr){
    int numOfReservations = 0;
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT count(*) FROM appointment WHERE cpr=?");
      stmt.setLong(1, cpr);
      ResultSet rs = stmt.executeQuery();

      if(rs.next()){
        numOfReservations = rs.getInt(1);
      }

      stmt.close();

    } catch(SQLException e){
      e.printStackTrace();
    }
    return numOfReservations;
  }

  public ArrayList<Appointment> getAllAppointments(){
    ArrayList<Appointment> allAppointments = new ArrayList<>();
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment WHERE status!=?");
      stmt.setString(1, "APPROVED");
      ResultSet rs = stmt.executeQuery();
      while(rs.next()){
        Appointment appointment = new Appointment(rs.getLong("cpr"), rs.getString("name"), rs.getInt("phone_number"), rs.getString("symptoms"), rs.getTimestamp("date"), new Pending());
        allAppointments.add(appointment);
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    return allAppointments;
  }

  public ArrayList<Appointment> getEverySingleAppointment(){
    ArrayList<Appointment> allAppointments = new ArrayList<>();
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment");
      ResultSet rs = stmt.executeQuery();
      while(rs.next()){
        Appointment appointment = new Appointment(rs.getLong("cpr"), rs.getString("name"), rs.getInt("phone_number"), rs.getString("symptoms"), rs.getTimestamp("date"), new Pending());
        allAppointments.add(appointment);
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    return allAppointments;
  }

  public ArrayList<Appointment> getApprovedAppointments(){
    ArrayList<Appointment> approvedAppointments = new ArrayList<>();
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointment WHERE status=?");
      stmt.setString(1, "APPROVED");
      ResultSet rs = stmt.executeQuery();
      while(rs.next()){
        Appointment appointment = new Appointment(rs.getLong("cpr"), rs.getString("name"), rs.getInt("phone_number"), rs.getString("symptoms"), rs.getTimestamp("date"), new Approved());
        approvedAppointments.add(appointment);
      }
    } catch (SQLException e){
      e.printStackTrace();
    }
    return approvedAppointments;
  }

  public void approveAppointment(Appointment appointment) {
    appointment.setState(new Approved());
    try (Connection connection = getConnection()) {
      PreparedStatement stmt = connection.prepareStatement("UPDATE appointment SET status=? WHERE cpr=? AND name=? AND phone_number=? AND symptoms=? AND date=?");
      stmt.setString(1, "APPROVED");
      stmt.setLong(2, appointment.getCpr());
      stmt.setString(3, appointment.getName());
      stmt.setInt(4, appointment.getPhone_number());
      stmt.setString(5, appointment.getSymptoms());
      stmt.setTimestamp(6, appointment.getDate());
      stmt.executeUpdate();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override public void updateStatus(Appointment appointment)
  {
    try(Connection connection = getConnection()){
      PreparedStatement stmt = connection.prepareStatement("UPDATE appointment SET status=? WHERE cpr=?, name=?, phone_number=?, symptoms=?, date=?");
      stmt.setString(1, appointment.getState().toString());
      stmt.setLong(2, appointment.getCpr());
      stmt.setString(3, appointment.getName());
      stmt.setInt(4, appointment.getPhone_number());
      stmt.setString(5, appointment.getSymptoms());
      stmt.setTimestamp(6, appointment.getDate());
      stmt.executeUpdate();
      stmt.close();
    } catch (SQLException e){
      e.printStackTrace();
    }
  }

  @Override public void removeDay(Date date)
  {
    try(Connection conn = getConnection()){
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO daysoff VALUES(?)");
      stmt.setDate(1, date);
      stmt.executeUpdate();
      stmt.close();
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  public ArrayList<Date> getDaysOff(){
    ArrayList<Date> listOfDaysOff = new ArrayList<>();
    try(Connection conn = getConnection()){
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM daysoff");
      ResultSet rs = stmt.executeQuery();
      while(rs.next()){
        listOfDaysOff.add(rs.getDate("date"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    return listOfDaysOff;
  }

  public ArrayList<LocalTime> getTimeslots() {
    ArrayList<LocalTime> timeslots = new ArrayList<>();
    try (Connection conn = getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reservedtimeslots");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Time time = rs.getTime("date");
        LocalTime localTime = time.toLocalTime();
        timeslots.add(localTime);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return timeslots;
  }

}
