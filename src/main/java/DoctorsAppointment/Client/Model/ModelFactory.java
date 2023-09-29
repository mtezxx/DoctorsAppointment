package DoctorsAppointment.Client.Model;

public class ModelFactory
{
  private Model model;
  private ClientFactory cf;

  public ModelFactory(ClientFactory cf)
  {
    this.cf = cf;
  }

  public Model getModel() {
    if(model == null) {
      model = new ModelManager(cf.getClient());
    }
    return model;
  }
}


