package DoctorsAppointment.Client.Model;

import DoctorsAppointment.Client.Network.Client;
import DoctorsAppointment.Client.Network.RMIClient;
import DoctorsAppointment.Server.ServerNetwork.ClientCallback;

public class ClientFactory
{
  private Client client;

  public Client getClient() {
    if(client == null) {
      client = new RMIClient();
    }
    return client;
  }
}
