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

    @Column(name = "labelplatformtemplatecolor")
    @Basic
    private String labelPlatformTemplateColor;

    @Column(name = "labelcommandLink")
    @Basic
    private String labelCommandLink;

    @Column(name = "labelapplicanttemplatecolor")
    @Basic
    private String labelApplicantTemplateColor;

    @Column(name = "labelbuttoncolor")
    @Basic
    private String labelButtonColor;

    @Column(name = "backgroundbuttonColor")
    @Basic
    private String backgroundButtonColor;

    @Column(name = "backgroundapplicantcontenttemplateColor")
    @Basic
    private String backgroundApplicantContentTemplateColor;

    @Column(name = "backgroundbottomapplicantcolor")
    @Basic
    private String backgroundBottomApplicantColor;

    @Column(name = "backgroundtopapplicantcolor")
    @Basic
    private String backgroundTopApplicantColor;

    @Column(name = "backgroundtopplatformtemplatecolor")
    @Basic
    private String backgroundTopPlatformTemplateColor;

    @Column(name = "backgroundplatformcontenttemplatecolor")
    @Basic
    private String backgroundPlatformContentTemplateColor;

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

    public String getLabelPlatformTemplateColor() {
        return labelPlatformTemplateColor;
    }

    public void setLabelPlatformTemplateColor(String labelPlatformTemplateColor) {
        this.labelPlatformTemplateColor = labelPlatformTemplateColor;
    }

    public String getLabelCommandLink() {
        return labelCommandLink;
    }

    public void setLabelCommandLink(String labelCommandLink) {
        this.labelCommandLink = labelCommandLink;
    }

    public String getLabelApplicantTemplateColor() {
        return labelApplicantTemplateColor;
    }

    public void setLabelApplicantTemplateColor(String labelApplicantTemplateColor) {
        this.labelApplicantTemplateColor = labelApplicantTemplateColor;
    }

    public String getBackgroundButtonColor() {
        return backgroundButtonColor;
    }

    public void setBackgroundButtonColor(String backgroundButtonColor) {
        this.backgroundButtonColor = backgroundButtonColor;
    }


    public String getLabelButtonColor() {
        return labelButtonColor;
    }

    public void setLabelButtonColor(String labelButtonColor) {
        this.labelButtonColor = labelButtonColor;
    }

    public String getBackgroundApplicantContentTemplateColor() {
        return backgroundApplicantContentTemplateColor;
    }

    public void setBackgroundApplicantContentTemplateColor(String backgroundApplicantContentTemplateColor) {
        this.backgroundApplicantContentTemplateColor = backgroundApplicantContentTemplateColor;
    }

    public String getBackgroundBottomApplicantColor() {
        return backgroundBottomApplicantColor;
    }

    public void setBackgroundBottomApplicantColor(String backgroundBottomApplicantColor) {
        this.backgroundBottomApplicantColor = backgroundBottomApplicantColor;
    }

    public String getBackgroundTopApplicantColor() {
        return backgroundTopApplicantColor;
    }

    public void setBackgroundTopApplicantColor(String backgroundTopApplicantColor) {
        this.backgroundTopApplicantColor = backgroundTopApplicantColor;
    }

    public String getBackgroundTopPlatformTemplateColor() {
        return backgroundTopPlatformTemplateColor;
    }

    public void setBackgroundTopPlatformTemplateColor(String backgroundTopPlatformTemplateColor) {
        this.backgroundTopPlatformTemplateColor = backgroundTopPlatformTemplateColor;
    }

    public String getBackgroundPlatformContentTemplateColor() {
        return backgroundPlatformContentTemplateColor;
    }

    public void setBackgroundPlatformContentTemplateColor(String backgroundPlatformContentTemplateColor) {
        this.backgroundPlatformContentTemplateColor = backgroundPlatformContentTemplateColor;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

}
