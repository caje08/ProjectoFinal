package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "adminentity")
@XmlRootElement
@DiscriminatorValue("adminentity")
public class AdminEntity extends UserEntity implements Serializable {

	public AdminEntity() {
		super();
	}

	public AdminEntity(String firstName, String lastName, String password, String email, String username) {
		super(firstName, lastName, password, email, username);
		this.roles=new ArrayList<Role>();
		this.roles.add(Role.ADMIN);
		this.roles.add(Role.MANAGER);
		this.roles.add(Role.INTERVIEWER);
	}

	@Override
	public Role getRole() {
		return role;
	}

	@Override
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public Collection<Role> getRoles() {
		return roles;
	}

	@Override
	public void setRoles(Role cargo) {
		this.roles.add(cargo);
	}
}
