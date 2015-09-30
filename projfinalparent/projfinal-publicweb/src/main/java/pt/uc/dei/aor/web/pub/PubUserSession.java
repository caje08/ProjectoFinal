package pt.uc.dei.aor.web.pub;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;

/**
 *
 * @author 
 */
@Stateful
@SessionScoped
public class PubUserSession {
    
    private UserEntity loggedUser;
    
//    @EJB
//	private UserEntityFacade userEntityFacade;
//    private Long loggedId;
//    private String name;
//    private String email;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public PubUserSession() {
    }

    /**
	 *
	 * @return UserEntity, that will be the logged User during the entire time that will be on session
	 * @throws UserNotFoundException
	 * @throws UserGuideException
	 */
//	public UserEntity getLoggedUser() throws UserNotFoundException, UserGuideException, NoResultException {
//		//loggedUser = userGuideFacade.findUserByUsername();
//		loggedUser = userEntityFacade.findUserByEmail();
//		Logger.getLogger(UserData.class.getName()).log(Level.INFO, "\nLogged User = "+loggedUser.getEmail());
//		return loggedUser;
//	}
    
    public UserEntity getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserEntity loggedUser) {
        this.loggedUser = loggedUser;
    }

    
}
