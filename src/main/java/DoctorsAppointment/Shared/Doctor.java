package DoctorsAppointment.Shared;

import java.io.Serializable;

public class Doctor implements Serializable
{
  private long cprNumber;
  private String password;


  public Doctor(long cprNumber, String password){
    this.cprNumber = cprNumber;
    this.password = password;
  }

  public long getCprNumber()
  {
    return cprNumber;
  }

  public String getPassword()
  {
    return password;
  }

}

