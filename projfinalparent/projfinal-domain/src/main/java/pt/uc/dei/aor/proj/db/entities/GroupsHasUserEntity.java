package pt.uc.dei.aor.proj.db.entities;


import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public  class GroupsHasUserEntity implements Serializable {

    @Column(name="userguideid")
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userguideId;

    @ManyToOne(targetEntity=GroupsEntity.class)
    private GroupsEntity groups;

    @ManyToOne(targetEntity=UserEntity.class)
    private UserEntity userGuide;

    @Column(name="username")
    @Basic
    private String username;

    public GroupsHasUserEntity(){
    }

   public Long getUserguideId() {
        return this.userguideId;
    }

  public void setUserguideId (Long userguideId) {
        this.userguideId = userguideId;
    }

   public GroupsEntity getGroups() {
        return this.groups;
    }

  public void setGroups (GroupsEntity groups) {
        this.groups = groups;
    }

   public UserEntity getUserGuide() {
        return this.userGuide;
    }

  public void setUserGuide (UserEntity userGuide) {
        this.userGuide = userGuide;
    }

   public String getUsername() {
        return this.username;
    }

  public void setUsername (String username) {
        this.username = username;
    }

}