package pt.uc.dei.aor.proj.ejb;

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;

@Local
public interface UserEJBLocal {
	public void populate();

	public List<UserEntity> getUsers();

	public UserEntity findByEmail(String email);

	public UserEntity findById(long id);

	public Collection<Role> getUserListOfRoles(String email);
	
	public void populateCandidates();

	public List<UserEntity> getCandidateUsers();

}