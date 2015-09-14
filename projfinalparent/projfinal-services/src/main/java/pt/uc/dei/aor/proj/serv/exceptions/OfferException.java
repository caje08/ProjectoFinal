/*
 */

package pt.uc.dei.aor.proj.serv.exceptions;

/**
 * @author 
 */
public class OfferException extends Exception {

    
    public OfferException() {
        super("Cannot make an offer whitout offer description");
    }
}
