package DoctorsAppointment;

import DoctorsAppointment.Client.Model.ClientFactory;
import DoctorsAppointment.Client.Model.ModelFactory;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewHandler;
import DoctorsAppointment.Client.Views.SharedViewClasses.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application
{
  @Override
  public void start(Stage primaryStage) throws Exception {
    ClientFactory cf = new ClientFactory();
    ModelFactory mf = new ModelFactory(cf);
    ViewModelFactory vmf = new ViewModelFactory(mf);
    ViewHandler view = new ViewHandler(vmf);
    view.start();
  }

  public static void main(String[] args) {
    launch(args);
  }
}