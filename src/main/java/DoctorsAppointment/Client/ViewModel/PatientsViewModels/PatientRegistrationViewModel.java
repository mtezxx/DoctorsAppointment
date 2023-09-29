package DoctorsAppointment.Client.ViewModel.PatientsViewModels;


import DoctorsAppointment.Client.Model.Model;
import DoctorsAppointment.Shared.Patient;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import static java.lang.Integer.parseInt;

public class PatientRegistrationViewModel
{
  private StringProperty cprNumber;
  private StringProperty phoneNumber;
  private StringProperty firstName;
  private StringProperty lastName;
  private StringProperty password;
  private Model model;

  public PatientRegistrationViewModel(Model model) {
    this.model = model;
    cprNumber= new SimpleStringProperty();
    phoneNumber= new SimpleStringProperty();
    firstName= new SimpleStringProperty();
    lastName= new SimpleStringProperty();
    password= new SimpleStringProperty();
  }

  public StringProperty cprNumberProperty(){
    return cprNumber;
  }
  public StringProperty phoneNumberProperty(){
    return phoneNumber;
  }

  public StringProperty firstNameProperty(){
    return firstName;
  }
  public StringProperty lastNameProperty(){
    return lastName;
  }
  public StringProperty passwordProperty(){
    return password;
  }

  public void registerPatient(){
    Patient patient = new Patient(Long.parseLong(cprNumber.get()), parseInt(phoneNumber.get()),firstName.get(),lastName.get(), password.get());
    model.addPatient(patient);
  }
}
