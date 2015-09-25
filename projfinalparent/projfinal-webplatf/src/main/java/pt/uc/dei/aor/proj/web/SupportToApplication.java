package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.ejb.ApplicationWebManagem;

@Named
@SessionScoped
public class SupportToApplication implements Serializable{


	private static final long serialVersionUID = -7903872778519572328L;
	private UserEntity activeSuppUser;
	private UserEntity temporarySuppUser;
	private PositionEntity activeSuppPosition;
	private PositionEntity temporarySuppPosition;
	private ApplicationEntity activeSuppApplication;
	private StatusApplication newstatus;
	private RejectionMotive motive;
	@Inject
	private ActiveSession suppSession;
	@Inject
	private ApplicationWebManagem update;
	
	public SupportToApplication() {
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * 
	 * @return true is new ApplicationStatus is rejected 
	 */
	public boolean newStatusRejected(){
		Logger.getLogger(ApplicationWebManagem.class.getName()).log(
				Level.INFO,
				"Inside newStatusRejected(), before if with this.newstatus="+this.newstatus);
		if(this.newstatus==null) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(
					Level.INFO,
					"Inside newStatusRejected(), returning false as this.newstatus is null ="+this.newstatus);
			return false;
		} else {
			
			this.update.setNewstatus(newstatus);
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(
					Level.INFO,
					"Inside newStatusRejected(), returning boolean with this.newstatus="+this.newstatus+" and setting update ="+update.getNewstatus());
			return this.newstatus.equals(StatusApplication.REJECTED);
		}		
	}

	public UserEntity getActiveSuppUser() {
		return activeSuppUser;
	}


	public void setActiveSuppUser(UserEntity activeSuppUser) {
		this.activeSuppUser = activeSuppUser;
	}


	public UserEntity getTemporarySuppUser() {
		return temporarySuppUser;
	}


	public void setTemporarySuppUser(UserEntity temporarySuppUser) {
		this.temporarySuppUser = temporarySuppUser;
	}


	public PositionEntity getActiveSuppPosition() {
		return activeSuppPosition;
	}


	public void setActiveSuppPosition(PositionEntity activeSuppPosition) {
		this.activeSuppPosition = activeSuppPosition;
	}


	public PositionEntity getTemporarySuppPosition() {
		return temporarySuppPosition;
	}


	public void setTemporarySuppPosition(PositionEntity temporarySuppPosition) {
		this.temporarySuppPosition = temporarySuppPosition;
	}


	public ApplicationEntity getActiveSuppApplication() {
		return activeSuppApplication;
	}


	public void setActiveSuppApplication(ApplicationEntity activeSuppApplication) {
		this.activeSuppApplication = activeSuppApplication;
	}


	public StatusApplication getNewstatus() {
		return newstatus;
	}


	public void setNewstatus(StatusApplication newstatus) {
		this.newstatus = newstatus;
	}


	public RejectionMotive getMotive() {
		return motive;
	}


	public void setMotive(RejectionMotive motive) {
		this.motive = motive;
	}


	public ActiveSession getSuppSession() {
		return suppSession;
	}


	public void setSuppSession(ActiveSession suppSession) {
		this.suppSession = suppSession;
	}

	
}
