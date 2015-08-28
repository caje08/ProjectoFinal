package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity implementation class for Entity: Utilizador
 *
 */
@Entity
@Table(name = "utilizador")
@NamedQueries({
	@NamedQuery(name = "User.findByEmailPass", query = "SELECT u FROM UserEntity u WHERE u.email = :email AND u.password = :password"),
	@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
	@NamedQuery(name = "User.findByName", query = "SELECT u FROM UserEntity u WHERE u.name = :name"),
	@NamedQuery(name = "User.findPlayByName", query = "SELECT u FROM UserEntity u WHERE u.email = :email"),
	@NamedQuery(name = "User.findById", query = "SELECT u FROM UserEntity u WHERE u.userid = :id")})

public class UserEntity implements Serializable {

	public static final String FIND_BY_EMAIL_AND_PASS = "User.findByEmailPass";
	public static final String FIND_BY_EMAIL = "User.findByEmail";
	public static final String FIND_BY_NAME = "User.findByName";
	public static final String FIND__PLAY_BY_NAME = "User.findPlayByName";


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid", nullable = false, unique = true)
	private Long userid;
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	@NotNull
	@Column(name = "password", nullable = false)
	private String password;
	@NotNull
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@NotNull
	@Column(name = "datanascimento")
	private String datanascimento;
	@NotNull
	@Column(name = "role", nullable = false)
	private Role role;
	@ElementCollection
	@CollectionTable(name="cargos",joinColumns=@JoinColumn(name="email",referencedColumnName="email"),uniqueConstraints=@UniqueConstraint(columnNames={"cargo","email"}))
	@Enumerated(EnumType.STRING)
	@Column(name="cargo")
	private Collection<Role> roles;


	static Logger logger = LoggerFactory.getLogger(UserEntity.class);

	public UserEntity() {
		super();
		//this.role="appuser";
	}

	public UserEntity(String name, String password, String email, String dtnasc, Role role) {
		super();
		this.name = name;
		this.password = password;
		this.email = email;
		this.datanascimento = dtnasc;
		this.role=role;
		this.roles=new ArrayList<Role>();
	}

	public Long getUserId() {
		return userid;
	}

	public void setUserId(Long id) {
		this.userid = id;
	}

	public String getDatanascimento() {
		return datanascimento;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;

	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Role cargo) {
		this.roles.add(cargo);
	}

	public void setDatanascimento(String datanascimento) {
		this.datanascimento = datanascimento;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (userid != null ? userid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof UserEntity)) {
			return false;
		}
		UserEntity other = (UserEntity) object;
		return (this.userid != null || other.userid == null)
				&& (this.userid == null || this.userid.equals(other.userid));
	}

	@Override
	public String toString() {
		// // return "entities.User[ id=" + id + " ]";
		//
		return name + " -> " + email;

	}
}
