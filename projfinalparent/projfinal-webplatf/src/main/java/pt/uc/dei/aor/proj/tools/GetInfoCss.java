package pt.uc.dei.aor.proj.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.CSS;
import pt.uc.dei.aor.proj.serv.exceptions.CSSInUseDoesNotExistsException;
import pt.uc.dei.aor.proj.serv.facade.CSSFacade;

/**
 * @author 
 */
@RequestScoped
@Named
public class GetInfoCss {
    
    @EJB
    private CSSFacade cSSFacade;

    /**
     * @return CSS in Use
     * @throws CSSInUseDoesNotExistsException 
     */
    public CSS getCssInUse(){
    	CSS out=null;
        try {
			out= cSSFacade.getCSSInUse();
		} catch (CSSInUseDoesNotExistsException e) {
			Logger.getLogger(CSSFacade.class.getName()).log(Level.SEVERE,"Inside getCSSInUse() with errors from CSSFacade",e.getMessage());
		}
        return out;
    }

    public CSSFacade getcSSFacade() {
        return cSSFacade;
    }

    public void setcSSFacade(CSSFacade cSSFacade) {
        this.cSSFacade = cSSFacade;
    }
    
    
    
}
