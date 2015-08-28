package pt.uc.dei.aor.proj.db.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "GroupsEntity.findAll", query = "SELECT g FROM GroupsEntity g"),
    @NamedQuery(name = "GroupsEntity.findByGroupName", query = "SELECT g FROM GroupsEntity g WHERE g.groupname LIKE :groupname")})

public  class GroupsEntity implements Serializable {


    @Column(name="groupname")
    @Id
    private String groupname;

    public GroupsEntity(){

    }


   public String getGroupname() {
        return this.groupname;
    }


  public void setGroupname (String groupname) {
        this.groupname = groupname;
    }

}

