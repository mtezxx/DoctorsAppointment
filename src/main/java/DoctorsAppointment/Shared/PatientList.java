package DoctorsAppointment.Shared;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientList implements Serializable
{
  private ArrayList<Patient> patients;

  public PatientList(){
    patients=new ArrayList<Patient>();
  }

  public void add(Patient patient){
    patients.add(patient);
  }

  public void set(Patient patient,int index){
    patients.set(index,patient);
  }

  public Patient get(int index)
  {
    if (index < patients.size())
    {
      return patients.get(index);
    }
    else
    {
      return null;
    }
  }

  public Patient get(int cprNumber,int phoneNumber,String firstName,String lastName,String password){
    for (int i = 0; i < patients.size(); i++)
    {
      Patient temp=patients.get(i);

      if (temp.getCprNumber()==cprNumber && temp.getPhoneNumber()==phoneNumber && temp.getFirstName().equals(firstName) && temp.getLastName().equals(lastName) && temp.getPassword().equals(password))
      {
        return temp;
      }
    }
    return null;
  }

  public int getIndex(int cprNumber,int phoneNumber,String firstName,String lastName,String password){
    for (int i = 0; i < patients.size(); i++)
    {
      Patient temp=patients.get(i);

      if (temp.getCprNumber()==cprNumber && temp.getPhoneNumber()==phoneNumber && temp.getFirstName().equals(firstName) && temp.getLastName().equals(lastName) && temp.getPassword().equals(password))
      {
        return i;
      }
    }
    return -1;
  }

  public int size(){
    return patients.size();
  }

 public String toString(){
  String strgTemp="";
   for (int i = 0; i < patients.size(); i++)
   {
     Patient temp=patients.get(i);
     strgTemp += temp + "\n";
   }
    return  strgTemp;
  }
}
