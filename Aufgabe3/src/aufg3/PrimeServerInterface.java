package aufg3;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by felix on 15.06.17.
 */
public interface PrimeServerInterface extends Remote
{
    public boolean primeService(long num)throws RemoteException;
}
