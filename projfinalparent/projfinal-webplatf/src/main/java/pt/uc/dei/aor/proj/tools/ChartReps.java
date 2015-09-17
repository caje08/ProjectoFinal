/*
 */
package pt.uc.dei.aor.proj.tools;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.mail.EmailException;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.OfferEntity;
import pt.uc.dei.aor.proj.db.tools.OfferOutcome;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.facade.OfferFacade;
import pt.uc.dei.aor.proj.serv.tools.GeneratePDF;

/**
 *
 * @author 
 */
@Named
@ViewScoped
public class ChartReps implements Serializable {

	private BarChartModel barModelOffer;
	private BarChartModel barModelInterview;
	private PieChartModel pieModelRejected;
	private ManagerEntity manager;
	private ManagerEntity selectedmanager;

	private List<Object[]> offerOutcome;
	private List<OfferEntity> offers;
	private List<String> offerOutcomes;
	private List<ManagerEntity> lstManagers;
	private List<InterviewFeedbackEntity> interviews;
	private List<Object[]> phoneInterviewsOutcome;
	private List<Object[]> presentialInterviewsOutcome;
	private List<String> rejectionMotives;
	private List<Object[]> rejectionMotive;

	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;
	@EJB
	private OfferFacade offerFacade;
	@EJB
	ApplicationFacade applicationFacade;
	@EJB
	private ManagerFacade managerFacade;

	public ChartReps() {
	}

	public BarChartModel getBarModelInterview() {
		createBarModelInterview();
		return barModelInterview;
	}

	public BarChartModel getBarModelOffer() {
		createBarModelOffer();
		return barModelOffer;
	}

	public PieChartModel getPieModelRejected() {
		createPieModel();
		return pieModelRejected;
	}

	public ManagerEntity getManager() {
		return manager;
	}

	public void setManager(ManagerEntity manager) {
		this.manager = manager;
	}

	public ManagerEntity getSelectedmanager() {
		return selectedmanager;
	}

	public void setSelectedmanager(ManagerEntity selectedmanager) {
		this.selectedmanager = selectedmanager;
	}

	public List<Object[]> getOfferOutcome() {
		if (offerOutcome == null) {
			offerOutcome = new ArrayList<>();
			offerOutcome.add(new Object[]{(String) offerOutcomes.get(0), (Integer) offerFacade.lstOfferAccepted().size()});
			offerOutcome.add(new Object[]{(String) offerOutcomes.get(1), (Integer) offerFacade.lstOfferRejected().size()});
			offerOutcome.add(new Object[]{(String) offerOutcomes.get(2), (Integer) offerFacade.lstOfferWaiting().size()});
		}
		return offerOutcome;
	}

	public List<OfferEntity> getOffers() {
		return offers;
	}

	public void setOffers(List<OfferEntity> offers) {
		this.offers = offers;
	}

	public List<String> getOfferOutcomes() {
		offerOutcomes = new ArrayList<>();
		offerOutcomes.add(OfferOutcome.ACCEPTED.toString());
		offerOutcomes.add(OfferOutcome.REFUSED.toString());
		offerOutcomes.add(OfferOutcome.WAITING.toString());
		return offerOutcomes;
	}

	public void setOfferOutcomes(List<String> offerOutcomes) {
		this.offerOutcomes = offerOutcomes;
	}

	public List<ManagerEntity> getLstManagers() {
		return managerFacade.findAll();
	}

	public void setLstManagers(List<ManagerEntity> lstManagers) {
		this.lstManagers = lstManagers;
	}

	public List<InterviewFeedbackEntity> getInterviews() {
		return interviews;
	}

	public void setInterviews(List<InterviewFeedbackEntity> interviews) {
		this.interviews = interviews;
	}

	public List<Object[]> getPhoneInterviewsOutcome() {
		if (phoneInterviewsOutcome == null) {
			phoneInterviewsOutcome = new ArrayList<>();
			phoneInterviewsOutcome.add(new Object[]{(String) "Accepted", (Integer) interviewFeedbackFacade.lstInterviewFeedbackAcceptedPhone().size()});
			phoneInterviewsOutcome.add(new Object[]{(String) "Refused", (Integer) interviewFeedbackFacade.lstInterviewFeedbackRefusedPhone().size()});
		}
		return phoneInterviewsOutcome;
	}

	public void setPhoneInterviews(List<Object[]> phoneInterviewsOutcome) {
		this.phoneInterviewsOutcome = phoneInterviewsOutcome;
	}

	public List<Object[]> getPresentialInterviewsOutcome() {
		if (presentialInterviewsOutcome == null) {
			presentialInterviewsOutcome = new ArrayList<>();
			presentialInterviewsOutcome.add(new Object[]{(String) "Accepted", (Integer) interviewFeedbackFacade.lstInterviewFeedbackAcceptedPresential().size()});
			presentialInterviewsOutcome.add(new Object[]{(String) "Refused", (Integer) interviewFeedbackFacade.lstInterviewFeedbackRefusedPresential().size()});
		}
		return presentialInterviewsOutcome;
	}

	public void setPresentialInterviewsOutcome(List<Object[]> presentialInterviewsOutcome) {
		this.presentialInterviewsOutcome = presentialInterviewsOutcome;
	}

	public List<String> getRejectionMotives() {
		rejectionMotives = new ArrayList<>();
		rejectionMotives.add(RejectionMotive.CV.toString());
		rejectionMotives.add(RejectionMotive.NEGOTIATION.toString());
		rejectionMotives.add(RejectionMotive.PERSONALITY.toString());
		return rejectionMotives;
	}

	public void setRejectionMotives(List<String> rejectionMotives) {
		this.rejectionMotives = rejectionMotives;
	}

	public List<Object[]> getRejectionMotive() {
		if (rejectionMotive == null) {
			rejectionMotive = new ArrayList<>();
			rejectionMotive.add(new Object[]{(String) rejectionMotives.get(0), (Integer) applicationFacade.lstApplicationsByRejectionMotiveCV().size()});
			rejectionMotive.add(new Object[]{(String) rejectionMotives.get(1), (Integer) applicationFacade.lstApplicationsByRejectionMotiveNEGOTIATION().size()});
			rejectionMotive.add(new Object[]{(String) rejectionMotives.get(2), (Integer) applicationFacade.lstApplicationsByRejectionMotivePERSONALITY().size()});
		}
		return rejectionMotive;
	}

	public void setRejectionMotive(List<Object[]> rejectionMotive) {
		this.rejectionMotive = rejectionMotive;
	}

	public InterviewFeedbackFacade getInterviewFeedbackFacade() {
		return interviewFeedbackFacade;
	}

	public void setInterviewFeedbackFacade(InterviewFeedbackFacade interviewFeedbackFacade) {
		this.interviewFeedbackFacade = interviewFeedbackFacade;
	}

	public OfferFacade getOfferFacade() {
		return offerFacade;
	}

	public void setOfferFacade(OfferFacade offerFacade) {
		this.offerFacade = offerFacade;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public ManagerFacade getManagerFacade() {
		return managerFacade;
	}

	public void setManagerFacade(ManagerFacade managerFacade) {
		this.managerFacade = managerFacade;
	}

	public int lstApplicantsByOfferOutcomeSize(OfferOutcome outcome) {
		return offerFacade.lstOfferOutcomeSize(outcome);
	}

	public int lstAllOffersSize() {
		return offerFacade.findAll().size();
	}

	private BarChartModel initBarModelOffer() {
		BarChartModel modelOffer = new BarChartModel();

		ChartSeries offer = new ChartSeries();
		offer.setLabel("Offers");
		offer.set("Accepted", offerFacade.lstOfferAccepted().size());
		offer.set("Rejected", offerFacade.lstOfferRejected().size());
		offer.set("Waiting", offerFacade.lstOfferWaiting().size());

		modelOffer.addSeries(offer);

		return modelOffer;
	}

	private void createBarModelOffer() {
		barModelOffer = initBarModelOffer();

		barModelOffer.setTitle("Offers Outcome");
		barModelOffer.setLegendPosition("ne");

		Axis xAxis = barModelOffer.getAxis(AxisType.X);
		xAxis.setLabel("Offers Outcome");

		Axis yAxis = barModelOffer.getAxis(AxisType.Y);
		yAxis.setLabel("# of Offers");
		yAxis.setMin(0);
	}

	private BarChartModel initBarModelInterview() {
		BarChartModel modelInterview = new BarChartModel();

		ChartSeries phone = new ChartSeries();
		phone.setLabel("Phone Interviews");
		phone.set("Accepted", interviewFeedbackFacade.lstInterviewFeedbackAcceptedPhone().size());
		phone.set("Refused", interviewFeedbackFacade.lstInterviewFeedbackRefusedPhone().size());

		ChartSeries presential = new ChartSeries();
		presential.setLabel("Presential Interviews");
		presential.set("Accepted", interviewFeedbackFacade.lstInterviewFeedbackAcceptedPresential().size());
		presential.set("Refused", interviewFeedbackFacade.lstInterviewFeedbackRefusedPresential().size());

		modelInterview.addSeries(phone);
		modelInterview.addSeries(presential);

		return modelInterview;
	}

	private void createBarModelInterview() {
		barModelInterview = initBarModelInterview();

		barModelInterview.setTitle("Interviews Outcome");
		barModelInterview.setLegendPosition("ne");

		Axis xAxis = barModelInterview.getAxis(AxisType.X);
		xAxis.setLabel("Interview Outcome");

		Axis yAxis = barModelInterview.getAxis(AxisType.Y);
		yAxis.setLabel("# of Interviews");
		yAxis.setMin(0);
	}

	/**
	 *
	 * @param rejectionMotive
	 * @return int Quantity of rejected applications due to given rejection motive (rejectionMotive)
	 */
	public int lstApplicantsByRejectionMotiveSize(RejectionMotive rejectionMotive) {
		return applicationFacade.lstApplicationsByRejectionMotive(rejectionMotive).size();
	}

	/**
	 *
	 * @return double total of rejected applications
	 */
	public double rejectionMotivePercentage() {
		double a = lstApplicantsByRejectionMotiveSize(RejectionMotive.CV);
		double b = lstApplicantsByRejectionMotiveSize(RejectionMotive.NEGOTIATION);
		double c = lstApplicantsByRejectionMotiveSize(RejectionMotive.PERSONALITY);
		return (a + b + c);
	}

	private void createPieModel() {
		pieModelRejected = new PieChartModel();

		pieModelRejected.set("CV not suitable", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.CV).size());
		pieModelRejected.set("Personality not suitable", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.PERSONALITY).size());
		pieModelRejected.set("Offer not accepted", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.NEGOTIATION).size());

		pieModelRejected.setTitle("Rejected Applicants");
		pieModelRejected.setLegendPosition("e");
	}

	/**
	 * Generates PDF offers report and sends email to selectedmanager
	 */
	public void sendOffersReport() {
		try {
			GeneratePDF.offersOutcomeReportPdf(getOfferOutcome(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Generates PDF interview report and sends email to selectedmanager
	 */
	public void sendInterviewReport() {
		try {
			GeneratePDF.interviewsOutcomeReportPdf(getPhoneInterviewsOutcome(), getPresentialInterviewsOutcome(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Generates PDF rejected applications report and sends email to selectedmanager
	 */
	public void sendRejectedReport() {
		try {
			GeneratePDF.rejectedReportPdf(getRejectionMotive(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
