package DoctorsAppointment.Shared;

import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;

public  interface PatientDAO
{
    Patient createPatient(long cprNumber,int phoneNumber,String firstName,String lastName,String password) throws SQLException;
}
