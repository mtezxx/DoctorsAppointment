package DoctorsAppointment.Shared;

import java.io.Serializable;

public class Patient implements Serializable
{
  private final long cprNumber;

  private final int phoneNumber;

  private final String firstName;

  private final String lastName;

  private String password;


  public Patient(long cprNumber,int phoneNumber ,String firstName,String lastName, String password){
    this.cprNumber=cprNumber;
    this.phoneNumber=phoneNumber;
    this.firstName=firstName;
    this.lastName=lastName;
    this.password=password;
  }

  public long getCprNumber()
  {
    return cprNumber;
  }

  public int getPhoneNumber()
  {
    return phoneNumber;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(){
    this.password=password;
  }
  public String getName(){
    return ""+getFirstName()+" "+getLastName();
  }
}
