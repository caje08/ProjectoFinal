/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.OfferOutcome;

/**
 *
 * @author 
 */
@Entity
@Table(name = "offerentity")
@XmlRootElement
@DiscriminatorValue("offerentity")
@NamedQueries({
    @NamedQuery(name = "OfferEntity.findByOutcomeAccepted", query = "SELECT o FROM OfferEntity o WHERE o.offerOutcome = pt.uc.dei.aor.proj.db.tools.OfferOutcome.ACCEPTED"),
    @NamedQuery(name = "OfferEntity.findByOutcomeRejected", query = "SELECT o FROM OfferEntity o WHERE o.offerOutcome = pt.uc.dei.aor.proj.db.tools.OfferOutcome.REFUSED"),
    @NamedQuery(name = "OfferEntity.findByApplication", query = "SELECT o FROM OfferEntity o WHERE o.application = :application"),
    @NamedQuery(name = "OfferEntity.findByOutcomeWaiting", query = "SELECT o FROM OfferEntity o WHERE o.offerOutcome = pt.uc.dei.aor.proj.db.tools.OfferOutcome.WAITING")})
public class OfferEntity implements Serializable {

    @Column(name = "offerId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerId;

    @Column(name = "offerdescription", nullable = false)
    @Basic
    private String offerDescription;

    @Column(name = "offerdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Basic
    private Date offerDate;

    @Column(name = "offeroutcome")
    @Basic
    private OfferOutcome offerOutcome;

    @OneToOne
    @JoinColumn(name = "application_ID", unique = true, nullable = true, insertable = true, updatable = true)
    private ApplicationEntity application;

    public OfferEntity() {
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public OfferOutcome getOfferOutcome() {
        return offerOutcome;
    }

    public void setOfferOutcome(OfferOutcome offerOutcome) {
        this.offerOutcome = offerOutcome;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

}
