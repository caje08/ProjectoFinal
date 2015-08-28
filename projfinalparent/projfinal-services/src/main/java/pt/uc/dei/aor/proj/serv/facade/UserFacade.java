package pt.uc.dei.aor.proj.serv.facade;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pt.uc.dei.aor.proj.db.entities.UserEntity;

/**
 *
 * @author 
 */
@Stateless
public class UserFacade extends AbstractFacade<UserEntity> {
    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(UserEntity.class);
    }
    
    public UserEntity findByEmailPass(String email, String password) {
        TypedQuery<UserEntity> q = em.createNamedQuery(UserEntity.FIND_BY_EMAIL_AND_PASS, UserEntity.class);
        q.setParameter("email", email);
        q.setParameter("password",password);
        try{
            return q.getSingleResult();
        }catch(Exception e){
            System.err.println("Single result not found: " + e);
            return null;
        }        
    }
    
    
    public UserEntity updateUser(UserEntity usr,String name, String email, String password){
        usr.setName(name);
        usr.setEmail(email);
        usr.setPassword(password);
        return super.edit(usr);
    }
    
    public UserEntity updateUser(UserEntity usr){
    	return super.edit(usr);
    }
    
    public boolean validaEmail(String str) {
        if (!str.isEmpty()) {
            if (str.matches("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\." + "[a-z0-9-]+)*(\\.[a-z]{2,4})$")) {
                return true;
            }
        }
        return false;
    }
    
}
