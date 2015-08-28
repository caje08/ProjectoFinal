package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
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
	@NamedQuery(name = "UserGuide.findByName", query = "SELECT u FROM UserGuide u WHERE u.username = :username"),
	@NamedQuery(name = "UserGuide.findByEmail", query = "SELECT u FROM UserGuide u WHERE u.email = :email"),
	@NamedQuery(name = "UserGuide.findByEmailPass", query = "SELECT u FROM UserGuide u WHERE u.email = :email AND u.password = :password"),
	@NamedQuery(name = "ApplicantEntity.findByEmail", query = "SELECT a FROM ApplicantEntity a WHERE a.email = :email")})

@Inheritance(strategy = InheritanceType.JOINED)
public class UserGuide implements Serializable {

	public static final String FIND_BY_EMAIL_AND_PASS = "UserGuide.findByEmailPass";

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

	public UserGuide() {
		//this.roles=new ArrayList<Role>();
	}

	public UserGuide(String firstName, String lastName, String password, Long userId, String email, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.userId = userId;
		this.email = email;
		this.username = username;
		//this.roles=new ArrayList<Role>();
	}
	public UserGuide(String firstName, String lastName, String password, String email, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		//        this.userId = userId;
		this.email = email;
		this.username = username;
		//this.roles=new ArrayList<Role>();
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

	@Override
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
		final UserGuide other = (UserGuide) obj;
		if (!Objects.equals(this.userId, other.userId)) {
			return false;
		}
		return true;
	}


}
