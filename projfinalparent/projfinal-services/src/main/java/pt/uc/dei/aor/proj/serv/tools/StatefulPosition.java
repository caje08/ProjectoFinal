/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import pt.uc.dei.aor.proj.db.entities.PositionEntity;

/**
 *
 * @author
 */
@Stateful
@SessionScoped
public class StatefulPosition implements Serializable {

	private PositionEntity position;

	public StatefulPosition() {
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
		Logger.getLogger(StatefulPosition.class.getName()).log(Level.INFO, "inside setPosition() and stating  positionentity where title is =" + position.getTitle());
	}



}
