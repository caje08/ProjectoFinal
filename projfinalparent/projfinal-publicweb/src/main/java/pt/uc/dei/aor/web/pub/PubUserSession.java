package pt.uc.dei.aor.web.pub;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import pt.uc.dei.aor.proj.db.entities.UserEntity;

/**
 *
 * @author 
 */
@Stateful
@SessionScoped
public class PubUserSession {
    
    private UserEntity loggedUser;
//    private Long loggedId;
//    private String name;
//    private String email;
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public PubUserSession() {
    }

    public UserEntity getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserEntity loggedUser) {
        this.loggedUser = loggedUser;
    }

    
}
