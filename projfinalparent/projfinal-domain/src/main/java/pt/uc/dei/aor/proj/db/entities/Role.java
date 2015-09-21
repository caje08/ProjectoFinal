package pt.uc.dei.aor.proj.db.entities;

public enum Role {

	ADMIN,
	INTERVIEWER,
	MANAGER,
	USERPUBLIC,
	CANDIDATE;

	public String getRole() {
		return this.toString();
	}

}
