package de.funky_clan.mc.net;

/**
 * @author synopia
 */
public class NetworkException extends RuntimeException {
    public NetworkException() {}

    public NetworkException( String message ) {
        super( message );
    }

    public NetworkException( Throwable cause ) {
        super( cause );
    }

    public NetworkException( String message, Throwable cause ) {
        super( message, cause );
    }
}
