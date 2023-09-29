package DoctorsAppointment.Client.Views.SharedViewClasses;

import DoctorsAppointment.Client.Model.ModelFactory;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorDashboardViewModel;
import DoctorsAppointment.Client.ViewModel.DoctorsViewModels.DoctorLoginViewModel;
import DoctorsAppointment.Client.ViewModel.General.WelcomeViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientDashboardViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientLoginViewModel;
import DoctorsAppointment.Client.ViewModel.PatientsViewModels.PatientRegistrationViewModel;

import java.rmi.RemoteException;

public class ViewModelFactory
{
  private PatientRegistrationViewModel patientRVM;
  private PatientLoginViewModel patientLoginVM;
  private PatientDashboardViewModel patientDashboardVM;

  private WelcomeViewModel welcomeVM;

  private DoctorLoginViewModel doctorLoginVM;
  private DoctorDashboardViewModel doctorDashboardVM;
  private ModelFactory mf;

  public ViewModelFactory(ModelFactory mf)
  {
    this.mf = mf;
  }

  public PatientRegistrationViewModel getPatientRVM() {
    if (patientRVM == null) {
      patientRVM = new PatientRegistrationViewModel(mf.getModel());
    }
    return patientRVM;
  }

  public PatientDashboardViewModel getPatientDashboardVM() {
    if (patientDashboardVM == null) {
      patientDashboardVM = new PatientDashboardViewModel(mf.getModel());
    }
    return patientDashboardVM;
  }

  public PatientLoginViewModel getPatientLoginVM() throws RemoteException
  {
    if (patientLoginVM == null) {
      patientLoginVM = new PatientLoginViewModel(mf.getModel());
    }
    return patientLoginVM;
  }

  public DoctorLoginViewModel getDoctorLoginVM() throws RemoteException
  {
    if (doctorLoginVM == null) {
      doctorLoginVM = new DoctorLoginViewModel(mf.getModel());
    }
    return doctorLoginVM;
  }

  public DoctorDashboardViewModel getDoctorDashboardVM() {
    if (doctorDashboardVM == null) {
      doctorDashboardVM = new DoctorDashboardViewModel(mf.getModel());
    }
    return doctorDashboardVM;
  }

  public WelcomeViewModel getWelcomeVM() {
    if (welcomeVM == null) {
      welcomeVM = new WelcomeViewModel(mf.getModel());
    }
    return welcomeVM;
  }
}
