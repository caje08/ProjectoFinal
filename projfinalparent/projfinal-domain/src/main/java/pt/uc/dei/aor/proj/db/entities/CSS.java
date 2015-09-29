package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
    @NamedQuery(name = "Css.findByUse", query = "SELECT c FROM CSS c WHERE c.inUse=true"),
    @NamedQuery(name = "Css.findByName", query = "SELECT c FROM CSS c WHERE c.name=:name")})
public class CSS implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ccsId;

    @Column(name = "logopath", nullable = false)
    @Basic
    private String logoPath;

    @Column(name = "name", nullable = false)
    @Basic
    private String name;

    @Column(name = "creationdate")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "address")
    @Basic
    private String address;

    @Column(name = "latitudecoordinate")
    @Basic
    private String latitudeCoordinate;

    @Column(name = "longitudecoordinate")
    @Basic
    private String longitudeCoordinate;

    @Column(name = "phone")
    @Basic
    private String phone;

    @Column(name = "fax")
    @Basic
    private String fax;

    @Column(name = "email") 
    @Basic
    private String email;

    @Column(name = "companydescription")
    @Basic
    @Size(min = 1, max=10000)
    private String companyDescription;

    @Column(name = "labelinternaltemplatecolor")
    @Basic
    private String labelInternalWebTemplateColor;

    @Column(name = "labelcommandLink")
    @Basic
    private String labelCommandLink;

    @Column(name = "labeltemplatecandidatecolor")
    @Basic
    private String labelTemplateCandidateColor;

    @Column(name = "labelcolorbutton")
    @Basic
    private String labelColorButton;

    @Column(name = "backgroundcolorbutton")
    @Basic
    private String backgroundColorButton;
    
    @Column(name = "backgroundinternalwebtoptemplatecolor")
    @Basic
    private String backgroundInternalWebTopTemplateColor;

    @Column(name = "backgroundinternalwebcontenttemplatecolor")
    @Basic
    private String backgroundInternalWebContentTemplateColor;

    @Column(name = "backgroundcontentcandidatetemplateColor")
    @Basic
    private String backgroundContentCandidateTemplateColor;

    @Column(name = "backgroundcandidatebottomcolor")
    @Basic
    private String backgroundCandidateBottomColor;

    @Column(name = "backgroundcandidatetopcolor")
    @Basic
    private String backgroundCandidateTopColor;

    @Column(name = "inuse")
    @Basic
    private boolean inUse;

    public CSS() {
    }

    public Long getCcsId() {
        return ccsId;
    }

    public void setCcsId(Long ccsId) {
        this.ccsId = ccsId;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitudeCoordinate() {
        return latitudeCoordinate;
    }

    public void setLatitudeCoordinate(String latitudeCoordinate) {
        this.latitudeCoordinate = latitudeCoordinate;
    }

    public String getLongitudeCoordinate() {
        return longitudeCoordinate;
    }

    public void setLongitudeCoordinate(String longitudeCoordinate) {
        this.longitudeCoordinate = longitudeCoordinate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getLabelCommandLink() {
        return labelCommandLink;
    }

    public void setLabelCommandLink(String labelCommandLink) {
        this.labelCommandLink = labelCommandLink;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

	public String getLabelInternalWebTemplateColor() {
		return labelInternalWebTemplateColor;
	}

	public void setLabelInternalWebTemplateColor(
			String labelInternalWebTemplateColor) {
		this.labelInternalWebTemplateColor = labelInternalWebTemplateColor;
	}

	public String getLabelTemplateCandidateColor() {
		return labelTemplateCandidateColor;
	}

	public void setLabelTemplateCandidateColor(String labelTemplateCandidateColor) {
		this.labelTemplateCandidateColor = labelTemplateCandidateColor;
	}

	public String getLabelColorButton() {
		return labelColorButton;
	}

	public void setLabelColorButton(String labelColorButton) {
		this.labelColorButton = labelColorButton;
	}

	public String getBackgroundColorButton() {
		return backgroundColorButton;
	}

	public void setBackgroundColorButton(String backgroundColorButton) {
		this.backgroundColorButton = backgroundColorButton;
	}

	public String getBackgroundInternalWebTopTemplateColor() {
		return backgroundInternalWebTopTemplateColor;
	}

	public void setBackgroundInternalWebTopTemplateColor(
			String backgroundInternalWebTopTemplateColor) {
		this.backgroundInternalWebTopTemplateColor = backgroundInternalWebTopTemplateColor;
	}

	public String getBackgroundInternalWebContentTemplateColor() {
		return backgroundInternalWebContentTemplateColor;
	}

	public void setBackgroundInternalWebContentTemplateColor(
			String backgroundInternalWebContentTemplateColor) {
		this.backgroundInternalWebContentTemplateColor = backgroundInternalWebContentTemplateColor;
	}

	public String getBackgroundContentCandidateTemplateColor() {
		return backgroundContentCandidateTemplateColor;
	}

	public void setBackgroundContentCandidateTemplateColor(
			String backgroundContentCandidateTemplateColor) {
		this.backgroundContentCandidateTemplateColor = backgroundContentCandidateTemplateColor;
	}

	public String getBackgroundCandidateBottomColor() {
		return backgroundCandidateBottomColor;
	}

	public void setBackgroundCandidateBottomColor(
			String backgroundCandidateBottomColor) {
		this.backgroundCandidateBottomColor = backgroundCandidateBottomColor;
	}

	public String getBackgroundCandidateTopColor() {
		return backgroundCandidateTopColor;
	}

	public void setBackgroundCandidateTopColor(String backgroundCandidateTopColor) {
		this.backgroundCandidateTopColor = backgroundCandidateTopColor;
	}
    
}
