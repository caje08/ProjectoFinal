/*
 */
package pt.uc.dei.aor.proj.db.tools;

import java.io.Serializable;

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
	}



}
