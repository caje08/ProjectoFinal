package pt.uc.dei.aor.proj.ejb;

import java.util.List;

import javax.ejb.Local;

import pt.uc.dei.aor.proj.entities.UserEntity;

@Local
public interface UserEJBLocal {
	public void populate();

	public List<UserEntity> getUsers();

	public UserEntity findByEmail(String email);

	public UserEntity findById(long id);



}