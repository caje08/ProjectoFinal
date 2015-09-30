package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "utilizador")
@NamedQueries({
	@NamedQuery(name = "AdminEntity.findAll", query = "SELECT ag FROM AdminEntity ag"),
	@NamedQuery(name = "AdminEntity.findByUserId", query = "SELECT ag FROM AdminEntity ag WHERE ag.userId = :userId"),
	@NamedQuery(name = "AdminEntity.findByUsername", query = "SELECT ag FROM AdminEntity ag WHERE  ag.username= :username"),
	@NamedQuery(name = "AdminEntity.findByEmail", query = "SELECT ag FROM AdminEntity ag WHERE ag.email = :email"),
	@NamedQuery(name = "AdminEntity.findByPassword", query = "SELECT ag FROM AdminEntity ag WHERE ag.password = :password"),
	@NamedQuery(name = "ApplicantEntity.findByEmailAndPass", query = "SELECT a FROM ApplicantEntity a WHERE a.email LIKE :email and a.password LIKE :password"),
	@NamedQuery(name = "ApplicantEntity.findAll", query = "SELECT a FROM ApplicantEntity a"),
	@NamedQuery(name = "ApplicantEntity.findByUserId", query = "SELECT a FROM ApplicantEntity a WHERE a.userId = :userId"),
	@NamedQuery(name = "ApplicantEntity.findByUsername", query = "SELECT a FROM ApplicantEntity a WHERE a.username = :username"),
	@NamedQuery(name = "UserEntity.findByName", query = "SELECT u FROM UserEntity u WHERE u.username = :username"),
	@NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
	@NamedQuery(name = "UserEntity.findByEmailPass", query = "SELECT u FROM UserEntity u WHERE u.email = :email AND u.password = :password"),
	@NamedQuery(name = "UserEntity.findById", query = "SELECT u FROM UserEntity u WHERE u.userId = :userId"),
	@NamedQuery(name = "UserEntity.findLstRolesByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
	@NamedQuery(name = "UserEntity.findLstUsersByRole", query = "SELECT distinct u FROM UserEntity u join u.roles cargos WHERE cargos = :cargo"),
	@NamedQuery(name = "UserEntity.findLstUsersExceptRole", query = "SELECT distinct u FROM UserEntity u join u.roles cargos WHERE cargos <> :cargo"),
	@NamedQuery(name = "ApplicantEntity.findByEmail", query = "SELECT a FROM ApplicantEntity a WHERE a.email = :email")})


@Inheritance(strategy = InheritanceType.JOINED)
public class UserEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_EMAIL_AND_PASS = "UserEntity.findByEmailPass";
	public static final String FIND_BY_EMAIL = "UserEntity.findByEmail";
	public static final String FIND_BY_NAME = "UserEntity.findByName";

	@Column(name = "firstname", nullable = false)
	@Basic
	private String firstName;

	@Column(name = "lastname", nullable = false)
	@Basic
	private String lastName;

	@Column(name = "password", nullable = false)
	@Basic
	private String password;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId", nullable = false, unique = true)
	private Long userId;

	@Column(name = "email", nullable = false)
	@Basic
	private String email;

	@Column(name = "username", unique = true, nullable = false)
	@Basic
	private String username;

	@NotNull
	@Column(name = "role", nullable = false)
	protected Role role;

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="cargos",joinColumns=@JoinColumn(name="email",referencedColumnName="email"),uniqueConstraints=@UniqueConstraint(columnNames={"cargo","email"}))
	@Enumerated(EnumType.STRING)
	@Column(name="cargo")
	protected Collection<Role> roles;

	static Logger logger = LoggerFactory.getLogger(UserEntity.class);

	public UserEntity() {
		super();
		//this.roles=new ArrayList<Role>();
	}

	public UserEntity(String firstName, String lastName, String password, Long userId, String email, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.userId = userId;
		this.email = email;
		this.username = username;
		//this.roles=new ArrayList<Role>();
	}
	public UserEntity(String firstName, String lastName, String password, String email, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		//        this.userId = userId;
		this.email = email;
		this.username = username;
		//this.roles=new ArrayList<Role>();
	}

	public void initArray(){
		this.roles=new ArrayList<Role>();
		this.roles.add(Role.USERPUBLIC);
	}
	
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Role cargo) {
		this.roles.add(cargo);
	}
	
	public void setRoles(Collection<Role> cargos) {
		this.roles=cargos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}



	/*@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserEntity other = (UserEntity) obj;
		if (!Objects.equals(this.userId, other.userId)) {
			return false;
		}
		return true;
	}
	 */

}
