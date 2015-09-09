/* 
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.OfferEntity;
import pt.uc.dei.aor.proj.db.tools.OfferOutcome;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplicant;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.exceptions.OfferException;

/**
 * @author 
 */
@Stateless
public class OfferFacade extends AbstractFacade<OfferEntity> {

    @PersistenceContext(unitName = "myPU")
    private EntityManager em;

    @EJB
    private ApplicantFacade applicantFacade;
    @EJB
    private ApplicationFacade applicationFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OfferFacade() {
        super(OfferEntity.class);
    }

    /**
     *
     * @return List of offers with
     */
    public List<OfferEntity> lstOfferAccepted() {
        Query query = em.createNamedQuery("OfferEntity.findByOutcomeAccepted", OfferEntity.class);
        return query.getResultList();
    }

    /**
     *
     * @return
     */
    public List<OfferEntity> lstOfferRejected() {
        Query query = em.createNamedQuery("OfferEntity.findByOutcomeRejected", OfferEntity.class);
        return query.getResultList();
    }

    /**
     *
     * @return
     */
    public List<OfferEntity> lstOfferWaiting() {
        Query query = em.createNamedQuery("OfferEntity.findByOutcomeWaiting", OfferEntity.class);
        return query.getResultList();
    }

    /**
     *
     * @param outcome
     * @return
     */
    public int lstOfferOutcomeSize(OfferOutcome outcome) {
        if (outcome.equals(OfferOutcome.ACCEPTED)) {
            Query query = em.createNamedQuery("OfferEntity.findByOutcomeAccepted", OfferEntity.class);
            return query.getResultList().size();
        } else if (outcome.equals(OfferOutcome.REFUSED)) {
            Query query = em.createNamedQuery("OfferEntity.findByOutcomeRejected", OfferEntity.class);
            return query.getResultList().size();
        } else {
            Query query = em.createNamedQuery("OfferEntity.findByOutcomeWaiting", OfferEntity.class);
            return query.getResultList().size();
        }
    }

    /**
     *
     * @param application
     * @return true if an application has an offer
     */
    public boolean knowIfAnApplicationAsOffer(ApplicationEntity application) {
        Query query = em.createNamedQuery("OfferEntity.findByApplication", OfferEntity.class);
        query.setParameter("application", application);
        return !query.getResultList().isEmpty();
    }

    /**
     *
     * @param application
     * @param offerDescription
     * @throws EJBException
     * @throws OfferException
     */
    public void makeOffer(ApplicationEntity application, String offerDescription) throws EJBException, OfferException {
        //is offer description is not empty
        if (!offerDescription.equals("")) {
            OfferEntity offer = new OfferEntity();
            offer.setOfferDate(new Date());
            offer.setApplication(application);
            offer.setOfferOutcome(OfferOutcome.WAITING);
            offer.setOfferDescription(offerDescription);
            create(offer);

        } else {
            throw new OfferException();
        }
    }

    /**
     * Edit Offer Outcome.
     *
     * @param selectedOffer
     * @param offer
     * @return
     * @throws EJBException
     */
    public String editOfferOutcome(OfferEntity selectedOffer, OfferEntity offer) throws EJBException {
        //if select option is Accepted
        if (offer.getOfferOutcome().equals(OfferOutcome.ACCEPTED)) {
            selectedOffer.setOfferOutcome(OfferOutcome.ACCEPTED);
            //set status application and set applicant to hired
            selectedOffer.getApplication().getApplicant().setStatus(StatusApplicant.HIRED);
            selectedOffer.getApplication().setStatus(StatusApplication.HIRED);
            selectedOffer.getApplication().getApplicant().setHiringDate(new Date());
            selectedOffer.getApplication().getPosition().setVacancies(selectedOffer.getApplication().getPosition().getVacancies() - 1);
            selectedOffer.getApplication().setHiringDate(new Date());
            edit(selectedOffer);
            applicantFacade.edit(selectedOffer.getApplication().getApplicant());
            applicationFacade.edit(selectedOffer.getApplication());
            return "applicants.xhtml?faces-redirect=true";
        //if select option is Refused
        } else if (offer.getOfferOutcome().equals(OfferOutcome.REFUSED)) {
            selectedOffer.setOfferOutcome(OfferOutcome.REFUSED);
            selectedOffer.getApplication().setMotive(RejectionMotive.NEGOTIATION);
            edit(selectedOffer);
            applicantFacade.edit(selectedOffer.getApplication().getApplicant());
            applicationFacade.edit(selectedOffer.getApplication());
            return "applicants.xhtml?faces-redirect=true";
        } else {
            return null;
        }

    }

    /**
     *
     * @param application
     * @return an offer by an application
     */
    public OfferEntity getOfferByApplication(ApplicationEntity application) {
        Query query = em.createNamedQuery("OfferEntity.findByApplication", OfferEntity.class);
        query.setParameter("application", application);
        return (OfferEntity) query.getSingleResult();

    }

    /////////////////////Getters && Setters////////////////////
    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public ApplicantFacade getApplicantFacade() {
        return applicantFacade;
    }

    public void setApplicantFacade(ApplicantFacade applicantFacade) {
        this.applicantFacade = applicantFacade;
    }

    public ApplicationFacade getApplicationFacade() {
        return applicationFacade;
    }

    public void setApplicationFacade(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

}
