package pt.uc.dei.aor.web.pub;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.ejb.UserEJBLocal;

@Named
@RequestScoped
public class PubLogin {
	@EJB
	private UserEJBLocal userEJB;

	private String username;
	private String password;

	public PubLogin() {
		super();

	}

	public PubLogin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public void populate() {
		userEJB.populate();
	}

	public List<UserEntity> getUsers() {
		return userEJB.getUsers();
	}

	public void populateCandidates() {
		System.out.println("Em PubLogin. populateCandidates() vai entrar em UserEJB. populateCandidates()\n");
		userEJB.populateCandidates();
	}

	public List<UserEntity> getCandidateUsers() {
		return userEJB.getCandidateUsers();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
