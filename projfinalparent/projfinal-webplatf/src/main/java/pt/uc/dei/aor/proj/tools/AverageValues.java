/*
 */
package pt.uc.dei.aor.proj.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.mail.MessagingException;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.ejb.SendEmailAttachedFiles;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.tools.BundleUtils;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.UserData;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class AverageValues implements Serializable {

	private static final String PDFDESTINATION = BundleUtils.getSettings("pdfgeneratefile");

	private PdfWriter docWriter = null;
	private ManagerEntity manager;
	private ManagerEntity selectedManager;

	private List<ApplicationEntity> applicationsHired;
	private List<ManagerEntity> lstManagers;

	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;
	@EJB
	private ManagerFacade managerFacade;
	@EJB
	private UserData userData;

	public AverageValues() {
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public PdfWriter getDocWriter() {
		return docWriter;
	}

	public void setDocWriter(PdfWriter docWriter) {
		this.docWriter = docWriter;
	}

	public ManagerEntity getManager() {
		return manager;
	}

	public void setManager(ManagerEntity manager) {
		this.manager = manager;
	}

	public ManagerEntity getSelectedManager() {
		return selectedManager;
	}

	public void setSelectedManager(ManagerEntity selectedManager) {
		this.selectedManager = selectedManager;
	}

	public List<ApplicationEntity> getApplicationsHired() {
		return applicationFacade.lstApplicationsStatusHired();
	}

	public void setApplicationsHired(List<ApplicationEntity> applicationsHired) {
		this.applicationsHired = applicationsHired;
	}

	public List<ManagerEntity> getLstManagers() {
		return managerFacade.findAll();
	}

	public void setLstManagers(List<ManagerEntity> lstManagers) {
		this.lstManagers = lstManagers;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public InterviewFeedbackFacade getInterviewFeedbackFacade() {
		return interviewFeedbackFacade;
	}

	public void setInterviewFeedbackFacade(InterviewFeedbackFacade interviewFeedbackFacade) {
		this.interviewFeedbackFacade = interviewFeedbackFacade;
	}

	public ManagerFacade getManagerFacade() {
		return managerFacade;
	}

	public void setManagerFacade(ManagerFacade managerFacade) {
		this.managerFacade = managerFacade;
	}

	/**
	 *
	 * @return double average time between application date and first interview
	 */
	public String averageTimeToInterview() {
		try {
			return String.valueOf(interviewFeedbackFacade.avgTimeToInterview() + " days");
		} catch (Exception ex) {
			Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			return "N/A";
		}

	}

	/**
	 *
	 * @return double average time between application date and hiring date
	 */
	public String averageTimeToHire() {
		try {
			return String.valueOf(applicationFacade.avgTimeToHire()+ " days");
		} catch (Exception ex) {
			Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			return "N/A";
		}
	}

	/**
	 * Generates PDF average report and sends email to selectedmanager
	 */
	public void toPdf() {

		File pdfFile = new File(PDFDESTINATION);
		try {
			FileOutputStream fosPDF;
			Document doc = new Document();
			try {
				if (!pdfFile.exists()) {
					pdfFile.mkdirs();
				}
				fosPDF = new FileOutputStream(new File(PDFDESTINATION + "/report.pdf"));
				docWriter = PdfWriter.getInstance(doc, fosPDF);
				doc.open();
				String avgInterview = "Average time until first interview: " + averageTimeToInterview();
				String avgHire = "Average time to hire: " + averageTimeToHire();
				doc.add(new Paragraph(avgInterview));
				doc.add(new Paragraph(avgHire));
				doc.close();
			} catch (FileNotFoundException ex) {
				Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			} catch (DocumentException ex) {
				Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			}
			sendEmail(selectedManager.getEmail(), userData.getLoggedUser().getUsername());
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
	}

	/**
	 * Sends email to given email, from given adminname with attached files
	 * @param String email
	 * @param String adminName
	 */
	public void sendEmail(String email, String adminName) {
		String[] attachFiles = new String[1];
		attachFiles[0] = PDFDESTINATION + "/report.pdf";
		try {
			SendEmailAttachedFiles.sendEmailWithAttachments(email, "Report", adminName + " has sent you the enclosed report", attachFiles);
		} catch (MessagingException | EJBException ex) {
			Logger.getLogger(AverageValues.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage("Error sending email");
		}
	}

}
