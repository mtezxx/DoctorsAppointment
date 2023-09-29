package DoctorsAppointment.Shared;
import java.beans.PropertyChangeListener;

public interface ListenerSubject
{
  void addListener(String event,PropertyChangeListener listener);
  void removeListener(String event,PropertyChangeListener listener);
}
