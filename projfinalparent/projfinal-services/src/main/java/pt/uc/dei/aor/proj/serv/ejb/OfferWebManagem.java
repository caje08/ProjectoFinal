/* 
 */
package pt.uc.dei.aor.proj.serv.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIPanel;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.OfferEntity;
import pt.uc.dei.aor.proj.db.tools.OfferOutcome;
import pt.uc.dei.aor.proj.serv.exceptions.OfferException;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.OfferFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;

/**
 *
 * @author 
 */
@Named(value = "offerWebManagem")
@ViewScoped
public class OfferWebManagem implements Serializable {

    private List<ApplicationEntity> lstApplicationInNegotiationProcess;
    private List<OfferEntity> lstOffer;

    private ApplicationEntity selectedApplication;
    private OfferEntity selectedOffer;
    private UIPanel panelGroup;
    private UIPanel panelOffers;
    private OfferEntity offer;
    private String offerDescription;
    private OfferOutcome[] lstOfferOutcomes;

    @EJB
    private ApplicationFacade applicationFacade;
    @EJB
    private OfferFacade offerFacade;

    /**
     * Creates a new instance of OfferWebManagem
     */
    public OfferWebManagem() {
    }

    @PostConstruct
    public void init() {
        this.offer = new OfferEntity();
        this.selectedApplication = new ApplicationEntity();
    }

    /**
     * Creates an Offer
     * @return 
     */
    public String createOffer() {
        try {
            offerFacade.makeOffer(selectedApplication, offerDescription);
            return "negotiation.xhtml?faces-redirect=true";
        } catch (EJBException ex) {
            Logger.getLogger(OfferWebManagem.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (OfferException ex) {
            Logger.getLogger(OfferWebManagem.class.getName()).log(Level.SEVERE, null, ex);
            JSFUtil.addWarningMessage(ex.getMessage());
            return null;
        }
    }
    
    /**
     * 
     * @return String OfferEntity description of selectedOffer
     */
    public String getDescriptionOfOffer(){
        return selectedOffer.getOfferDescription();
    }

    /**
     * Edits Offer outcome
     */
    public void editOfferOutcome() {
        try {
            offerFacade.editOfferOutcome(selectedOffer, offer);
            panelGroup.setRendered(false);
        } catch (EJBException ex) {
            Logger.getLogger(OfferWebManagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showOfferPanel(OfferEntity offer) {
        selectedOffer = offer;
        panelGroup.setRendered(true);
    }
 

    public void showPanel(ApplicationEntity application) {
        selectedApplication = application;
        panelGroup.setRendered(true);
        panelOffers.setRendered(false);
    }

    /**
     * 
     * @param application
     * @return true if application is in Offer Process
     */
    public boolean alreadyInOfferProcess(ApplicationEntity application) {
        return offerFacade.knowIfAnApplicationAsOffer(application);
    }
    
    /**
     * 
     * @param offer
     * @return true if OfferOutcome status is Waiting
     */
    public boolean ifisOutcomeWaiting(OfferEntity offer){
        return offer.getOfferOutcome().equals(OfferOutcome.WAITING);
    }
    
    
    /////////////////////Getters && Setters////////////////////

    public List<ApplicationEntity> getLstApplicationInNegotiationProcess() {
        lstApplicationInNegotiationProcess = applicationFacade.lstApplicationInNegotiationProcess();
        return lstApplicationInNegotiationProcess;
    }

    public void setLstApplicationInNegotiationProcess(List<ApplicationEntity> lstApplicationInNegotiationProcess) {
        this.lstApplicationInNegotiationProcess = lstApplicationInNegotiationProcess;
    }

    public ApplicationEntity getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ApplicationEntity selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public ApplicationFacade getApplicationFacade() {
        return applicationFacade;
    }

    public void setApplicationFacade(ApplicationFacade applicationFacade) {
        this.applicationFacade = applicationFacade;
    }

    public UIPanel getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(UIPanel panelGroup) {
        this.panelGroup = panelGroup;
    }

    public UIPanel getPanelOffers() {
        return panelOffers;
    }

    public void setPanelOffers(UIPanel panelOffers) {
        this.panelOffers = panelOffers;
    }

    public OfferFacade getOfferFacade() {
        return offerFacade;
    }

    public void setOfferFacade(OfferFacade offerFacade) {
        this.offerFacade = offerFacade;
    }

    public OfferEntity getOffer() {
        return offer;
    }

    public void setOffer(OfferEntity offer) {
        this.offer = offer;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public OfferOutcome[] getLstOfferOutcomes() {
        lstOfferOutcomes = OfferOutcome.values();
        return lstOfferOutcomes;
    }

    public void setLstOfferOutcomes(OfferOutcome[] lstOfferOutcomes) {
        this.lstOfferOutcomes = lstOfferOutcomes;
    }

    public List<OfferEntity> getLstOffer() {
        lstOffer=offerFacade.findAll();
        return lstOffer;
    }

    public void setLstOffer(List<OfferEntity> lstOffer) {
        this.lstOffer = lstOffer;
    }

    public OfferEntity getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(OfferEntity selectedOffer) {
        this.selectedOffer = selectedOffer;
    }
    
}
