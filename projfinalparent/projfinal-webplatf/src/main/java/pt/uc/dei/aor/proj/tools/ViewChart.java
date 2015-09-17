/* 
 */
package pt.uc.dei.aor.proj.tools;

import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.mail.MessagingException;

import org.apache.commons.codec.binary.Base64;
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
import pt.uc.dei.aor.proj.serv.ejb.SendEmailAttachedFiles;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.facade.OfferFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author 
 */
@Named
@ViewScoped
public class ViewChart implements Serializable {

	private BarChartModel barModelInterview;
	private BarChartModel barModelOffer;
	private PieChartModel pieModelRejected;
	private String base64Str;
	private ManagerEntity manager;
	private ManagerEntity selectedmanager;
	private PdfWriter docWriter = null;

	private List<InterviewFeedbackEntity> interviews;
	private List<OfferEntity> offers;
	private List<OfferOutcome> offerOutcomes;
	private List<RejectionMotive> rejectionMotives;
	private List<ManagerEntity> lstManagers;

	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;
	@EJB
	private OfferFacade offerFacade;
	@EJB
	ApplicationFacade applicationFacade;
	@EJB
	private ManagerFacade managerFacade;

	/**
	 *
	 * @return
	 */
	 public BarChartModel getBarModelInterview() {
		 createBarModelInterview();
		 return barModelInterview;
	 }

	 /**
	  *
	  * @return
	  */
	 public BarChartModel getBarModelOffer() {
		 createBarModelOffer();
		 return barModelOffer;
	 }

	 /**
	  *
	  * @return
	  */
	 public PieChartModel getPieModelRejected() {
		 createPieModel();
		 return pieModelRejected;
	 }

	 /**
	  *
	  * @return
	  */
	 public String getBase64Str() {
		 return base64Str;
	 }

	 /**
	  *
	  * @param base64Str
	  */
	 public void setBase64Str(String base64Str) {
		 this.base64Str = base64Str;
	 }

	 /**
	  *
	  * @return
	  */
	 public ManagerEntity getManager() {
		 return manager;
	 }

	 /**
	  *
	  * @param manager
	  */
	 public void setManager(ManagerEntity manager) {
		 this.manager = manager;
	 }

	 /**
	  *
	  * @return
	  */
	 public ManagerEntity getSelectedmanager() {
		 return selectedmanager;
	 }

	 /**
	  *
	  * @param selectedmanager
	  */
	 public void setSelectedmanager(ManagerEntity selectedmanager) {
		 this.selectedmanager = selectedmanager;
	 }

	 /**
	  *
	  * @return
	  */
	 public PdfWriter getDocWriter() {
		 return docWriter;
	 }

	 /**
	  *
	  * @param docWriter
	  */
	 public void setDocWriter(PdfWriter docWriter) {
		 this.docWriter = docWriter;
	 }

	 /**
	  *
	  * @return
	  */
	 public List<InterviewFeedbackEntity> getInterviews() {
		 return interviews;
	 }

	 /**
	  *
	  * @param interviews
	  */
	 public void setInterviews(List<InterviewFeedbackEntity> interviews) {
		 this.interviews = interviews;
	 }

	 /**
	  *
	  * @return
	  */
	 public List<OfferEntity> getOffers() {
		 return offers;
	 }

	 /**
	  *
	  * @param offers
	  */
	 public void setOffers(List<OfferEntity> offers) {
		 this.offers = offers;
	 }

	 /**
	  *
	  * @return
	  */
	 public List<OfferOutcome> getOfferOutcomes() {
		 offerOutcomes = new ArrayList<>();
		 offerOutcomes.add(OfferOutcome.ACCEPTED);
		 offerOutcomes.add(OfferOutcome.REFUSED);
		 offerOutcomes.add(OfferOutcome.WAITING);
		 return offerOutcomes;
	 }

	 public void setOfferOutcomes(List<OfferOutcome> offerOutcomes) {
		 this.offerOutcomes = offerOutcomes;
	 }

	 /**
	  *
	  * @return
	  */
	 public List<RejectionMotive> getRejectionMotives() {
		 rejectionMotives = new ArrayList<>();
		 rejectionMotives.add(RejectionMotive.CV);
		 rejectionMotives.add(RejectionMotive.NEGOTIATION);
		 rejectionMotives.add(RejectionMotive.PERSONALITY);
		 return rejectionMotives;
	 }

	 /**
	  *
	  * @param rejectionMotives
	  */
	 public void setRejectionMotives(List<RejectionMotive> rejectionMotives) {
		 this.rejectionMotives = rejectionMotives;
	 }

	 /**
	  *
	  * @return
	  */
	 public List<ManagerEntity> getLstManagers() {
		 return managerFacade.findAll();
	 }

	 /**
	  *
	  * @param lstManagers
	  */
	 public void setLstManagers(List<ManagerEntity> lstManagers) {
		 this.lstManagers = lstManagers;
	 }

	 /**
	  *
	  * @return
	  */
	 public InterviewFeedbackFacade getInterviewFeedbackFacade() {
		 return interviewFeedbackFacade;
	 }

	 /**
	  *
	  * @param interviewFeedbackFacade
	  */
	 public void setInterviewFeedbackFacade(InterviewFeedbackFacade interviewFeedbackFacade) {
		 this.interviewFeedbackFacade = interviewFeedbackFacade;
	 }

	 /**
	  *
	  * @return
	  */
	 public OfferFacade getOfferFacade() {
		 return offerFacade;
	 }

	 /**
	  *
	  * @param offerFacade
	  */
	 public void setOfferFacade(OfferFacade offerFacade) {
		 this.offerFacade = offerFacade;
	 }

	 /**
	  *
	  * @return
	  */
	 public ApplicationFacade getApplicationFacade() {
		 return applicationFacade;
	 }

	 /**
	  *
	  * @param applicationFacade
	  */
	 public void setApplicationFacade(ApplicationFacade applicationFacade) {
		 this.applicationFacade = applicationFacade;
	 }

	 /**
	  *
	  * @return
	  */
	 public ManagerFacade getManagerFacade() {
		 return managerFacade;
	 }

	 /**
	  *
	  * @param managerFacade
	  */
	 public void setManagerFacade(ManagerFacade managerFacade) {
		 this.managerFacade = managerFacade;
	 }

	 /**
	  *
	  * @return
	  */
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

	 /**
	  *
	  */
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
	  * @param outcome
	  * @return
	  */
	 public int lstApplicantsByOfferOutcomeSize(OfferOutcome outcome) {
		 return offerFacade.lstOfferOutcomeSize(outcome);
	 }

	 /**
	  *
	  * @return
	  */
	 public int lstAllOffersSize() {
		 return offerFacade.findAll().size();
	 }

	 /**
	  *
	  * @return
	  */
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

	 /**
	  *
	  */
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

	 /**
	  *
	  * @param rejectionMotive
	  * @return
	  */
	 public int lstApplicantsByRejectionMotiveSize(RejectionMotive rejectionMotive) {
		 return applicationFacade.lstApplicationsByRejectionMotive(rejectionMotive).size();
	 }

	 /**
	  *
	  * @return
	  */
	 public double rejectionMotivePercentage() {
		 double a = lstApplicantsByRejectionMotiveSize(RejectionMotive.CV);
		 double b = lstApplicantsByRejectionMotiveSize(RejectionMotive.NEGOTIATION);
		 double c = lstApplicantsByRejectionMotiveSize(RejectionMotive.PERSONALITY);
		 return (a + b + c);
	 }

	 /**
	  *
	  */
	 private void createPieModel() {
		 pieModelRejected = new PieChartModel();

		 pieModelRejected.set("CV not suitable", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.CV).size());
		 pieModelRejected.set("Personality not suitable", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.PERSONALITY).size());
		 pieModelRejected.set("Offer not accepted", applicationFacade.lstApplicationsByRejectionMotive(RejectionMotive.NEGOTIATION).size());

		 pieModelRejected.setTitle("Rejected Applicants");
		 pieModelRejected.setLegendPosition("e");
	 }

	 public void submittedBase64Str() {
		 if (base64Str.split(",").length > 1) {
			 String encoded = base64Str.split(",")[1];
			 byte[] decoded = Base64.decodeBase64(encoded);
			 try {
				 RenderedImage renderedImage = ImageIO.read(new ByteArrayInputStream(decoded));
				 ImageIO.write(renderedImage, "png", new File("c:/CSANTOS/DEI/Projecto/tmp/critSW.png"));
				 imageToPDF();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
		 }
	 }

	 public void imageToPDF() {
		 Document document = new Document();
		 String input = "c:/CSANTOS/DEI/Projecto/tmp/critSW.png";
		 String output = "c:/CSANTOS/DEI/Projecto/tmp/questions and answers.pdf";
		 try {
			 FileOutputStream fos = new FileOutputStream(output);
			 PdfWriter writer = PdfWriter.getInstance(document, fos);
			 writer.open();
			 document.open();
			 document.add(Image.getInstance(input));
			 document.add(new Paragraph(""));
			 document.add(new Paragraph("Report created on " + new java.util.Date().toString()));
			 document.close();
			 writer.close();
		 } catch (Exception ex) {
			 Logger.getLogger(ViewChart.class.getName()).log(Level.SEVERE, null, ex);
		 }
	 }

	 public void send() {
		 sendEmail(selectedmanager.getEmail(), "CarlosSantos", "c:/CSANTOS/DEI/Projecto/tmp/questions and answers.pdf");
	 }

	 public void sendEmail(String email, String adminName, String pdfReportFile) {
		 String[] attachFiles = new String[1];
		 attachFiles[0] = pdfReportFile;
		 try {
			 SendEmailAttachedFiles.sendEmailWithAttachments(email, "Report", adminName + " has sent you the enclosed report", attachFiles);
		 } catch (MessagingException | EJBException ex) {
			 Logger.getLogger(ViewChart.class.getName()).log(Level.SEVERE, null, ex);
			 JSFUtil.addErrorMessage("Error sending email");
		 }
	 }

	 public void preProcessPDF(Object document) throws BadElementException, DocumentException {
		 submittedBase64Str();
		 FileOutputStream fosPDF;
		 Document doc = (Document) document;
		 try {
			 fosPDF = new FileOutputStream(new File("c:/CSANTOS/DEI/Projecto/tmp/Angola - African Economic Outlook.PT.pdf"));
			 docWriter = PdfWriter.getInstance(doc, fosPDF);
			 doc.open();
			 String chartImage = "c:/CSANTOS/DEI/Projecto/tmp/critSW.png";
			 doc.add(Image.getInstance(chartImage));
		 } catch (FileNotFoundException ex) {
			 Logger.getLogger(ViewChart.class.getName()).log(Level.SEVERE, null, ex);
		 } catch (IOException ex) {
			 Logger.getLogger(ViewChart.class.getName()).log(Level.SEVERE, null, ex);
		 }
	 }

	 public Boolean pdfExists() {
		 File file = new File("c:/CSANTOS/DEI/Projecto/tmp/questions and answers.pdf");
		 return file.exists();
	 }

}
