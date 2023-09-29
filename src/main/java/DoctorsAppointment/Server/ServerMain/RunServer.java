package DoctorsAppointment.Server.ServerMain;

import DoctorsAppointment.Server.Model.DataModelServerImpl;
import DoctorsAppointment.Server.ServerNetwork.RMIServer;
import DoctorsAppointment.Server.ServerNetwork.RMIServerImplementation;

import java.rmi.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class RunServer
{
  public static void main(String[] args)
      throws RemoteException, AlreadyBoundException, SQLException
  {
    RMIServerImplementation rmiServer=new RMIServerImplementation(new DataModelServerImpl());
    rmiServer.startServer();
    System.out.println("Server started");
  }
}
