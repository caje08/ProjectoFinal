/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import static net.sf.dynamicreports.report.builder.DynamicReports.concatenatedReport;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.mail.MessagingException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.exception.DRException;

import org.apache.commons.mail.EmailException;

import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.serv.tools.BundleUtils;
import pt.uc.dei.aor.proj.serv.ejb.SendEmailAttachedFiles;

/**
 *
 * @author
 */
public class GeneratePDF {

	@Resource
	private static SessionContext ctx;
	private static final String PDFDESTINATION = BundleUtils.getSettings("pdfgeneratefile");

	/**
	 * Generates pdf offers outcome report and email it to user
	 * @param offerOutcome
	 * @param user
	 * @throws DRException
	 * @throws FileNotFoundException
	 * @throws EmailException
	 */
	public static void offersOutcomeReportPdf(List<Object[]> offerOutcome, UserEntity user) throws DRException, FileNotFoundException, EmailException {
		ChartDynamicReports reportOffers = new ChartDynamicReports(offerOutcome, "offerOutcome", "qtApplicants", "Offers Outcome", "# of Applicants", "Offers Outcome", "Offers Outcome Report");
		JasperReportBuilder page1 = reportOffers.buildBarChart();
		sendEmail(user.getEmail(), ctx.getCallerPrincipal().getName());
	}

	/**
	 * Generates pdf interviews outcome report and email it to user
	 * @param phoneInterviewsOutcome
	 * @param presentialInterviewsOutcome
	 * @param user
	 * @throws DRException
	 * @throws FileNotFoundException
	 * @throws EmailException
	 */
	public static void interviewsOutcomeReportPdf(List<Object[]> phoneInterviewsOutcome, List<Object[]> presentialInterviewsOutcome, UserEntity user) throws DRException, FileNotFoundException, EmailException {
		ChartDynamicReports reportPhone = new ChartDynamicReports(phoneInterviewsOutcome, "interviewOutcome", "qtInterviews", "Interview Outcome", "# of Interviews", "Phone Interviews Outcome", "Interviews Outcome Report");
		ChartDynamicReports reportPresential = new ChartDynamicReports(presentialInterviewsOutcome, "interviewOutcome", "qtInterviews", "Interview Outcome", "# of Interviews", "Presential Interviews Outcome", "Interviews Outcome Report");
		JasperReportBuilder page1 = reportPhone.buildBarChart();
		JasperReportBuilder page2 = reportPresential.buildBarChart();
		//merge pages reports
		concatenatedReport()
		.concatenate(page1, page2)
		.toPdf(Exporters.pdfExporter(PDFDESTINATION + "/report.pdf"));
		sendEmail(user.getEmail(), ctx.getCallerPrincipal().getName());
	}

	/**
	 * Generates pdf rejected applications report and email it to user
	 * @param rejectionMotive
	 * @param user
	 * @throws DRException
	 * @throws FileNotFoundException
	 * @throws EmailException
	 */
	public static void rejectedReportPdf(List<Object[]> rejectionMotive, UserEntity user) throws DRException, FileNotFoundException, EmailException {
		ChartDynamicReports reportRejected = new ChartDynamicReports(rejectionMotive, "rejectionMotives", "qtApplicants", "Rejection Motives", "# of Applicants", "Rejection Motives", "Rejection Motives Report");
		JasperReportBuilder page1 = reportRejected.buildPieChart();
		sendEmail(user.getEmail(), ctx.getCallerPrincipal().getName());
	}

	/**
	 * Generates pdf applications report and email it to user
	 * @param applicantsPerTime
	 * @param user
	 * @throws DRException
	 * @throws FileNotFoundException
	 * @throws EmailException
	 */
	public static void applicationsReportPdf(List<Object[]> applicantsPerTime, UserEntity user) throws DRException, FileNotFoundException, EmailException {
		ChartDynamicReports reportApplications = new ChartDynamicReports(applicantsPerTime, "date", "qtApplicants", "Date", "# of Applicants", "Applicants", "Applicants by Date Report");
		JasperReportBuilder page1 = reportApplications.buildBarChart();
		sendEmail(user.getEmail(), ctx.getCallerPrincipal().getName());
	}

	/**
	 * Generates pdf position report and email it to user
	 * @param applicantsPerPosition
	 * @param user
	 * @throws DRException
	 * @throws FileNotFoundException
	 * @throws EmailException
	 */
	public static void positionsReportPdf(List<Object[]> applicantsPerPosition, UserEntity user) throws DRException, FileNotFoundException, EmailException {
		ChartDynamicReports reportApplications = new ChartDynamicReports(applicantsPerPosition, "position", "qtApplicants", "Positions", "# of Applicants", "Applicants", "Applicants Per PositionEntity Report");
		JasperReportBuilder page1 = reportApplications.buildBarChart();
		sendEmail(user.getEmail(), ctx.getCallerPrincipal().getName());
	}

	/**
	 * Sends email to given email from given admin
	 * @param email
	 * @param adminName
	 */
	public static void sendEmail(String email, String adminName) {
		String[] attachFiles = new String[1];
		attachFiles[0] = PDFDESTINATION + "/report.pdf";
		try {
			SendEmailAttachedFiles.sendEmailWithAttachments(email, "Report", adminName + " has sent you the enclosed report", attachFiles);
		} catch (MessagingException | EJBException ex) {
			Logger.getLogger(GeneratePDF.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage("Error sending email");
		}
	}

}
