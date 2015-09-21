/*
 */
package pt.uc.dei.aor.proj.tools;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.facade.PositionFacade;
import pt.uc.dei.aor.proj.serv.tools.GeneratePDF;


/**
 *
 * @author
 */
@Named
@ViewScoped
public class ReppApplicantsBB implements Serializable {

	private Date begin;
	private Date end;
	private Date beginChart;
	private Date endChart;
	private PositionEntity position;
	private PositionEntity selectedPosition;
	private ManagerEntity selectedmanager;
	private ManagerEntity manager;

	private BarChartModel zoomModelPosition;
	private LineChartModel dateModel;
	private LineChartModel dateSpontaneousModel;

	private List<ManagerEntity> lstManagers;
	private List<ApplicationEntity> spontaneousApplications;
	private List<ApplicantEntity> applicantsPerTime;
	private List<ApplicantEntity> applicantsByPosition;
	private List<ApplicationEntity> applicationsPerTime;
	private List<ApplicationEntity> applicationsByPosition;
	private List<PositionEntity> positions;
	private List<Object[]> applicants;
	private List<Object[]> spontaneousApplicants;
	private List<Object[]> applicantsPerPosition;

	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private PositionFacade positionFacade;
	@EJB
	private ManagerFacade managerFacade;

	/**
	 *
	 */
	 public ReppApplicantsBB() {
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public Date getBeginChart() {
		return beginChart;
	}

	public void setBeginChart(Date beginChart) {
		this.beginChart = beginChart;
	}

	public Date getEndChart() {
		return endChart;
	}

	public void setEndChart(Date endChart) {
		this.endChart = endChart;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public PositionEntity getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(PositionEntity selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public ManagerEntity getSelectedmanager() {
		return selectedmanager;
	}

	public void setSelectedmanager(ManagerEntity selectedmanager) {
		this.selectedmanager = selectedmanager;
	}

	public ManagerEntity getManager() {
		return manager;
	}

	public void setManager(ManagerEntity manager) {
		this.manager = manager;
	}

	public BarChartModel getZoomModelPosition() {
		createZoomModel();
		return zoomModelPosition;
	}

	public LineChartModel getDateModel() {
		if (begin != null || end != null) {
			createDateModel();
		} else {
			createDateModel1();
		}
		return dateModel;
	}

	public List<ManagerEntity> getLstManagers() {
		return managerFacade.findAll();
	}

	public void setLstManagers(List<ManagerEntity> lstManagers) {
		this.lstManagers = lstManagers;
	}

	public LineChartModel getDateSpontaneousModel() {
		if (begin != null || end != null) {
			createDateSpontaneousModel();
		} else {
			createDateSpontaneousModel1();
		}
		return dateSpontaneousModel;
	}

	public List<ApplicationEntity> getSpontaneousApplications() {
		return spontaneousApplications;
	}

	public void setSpontaneousApplications(List<ApplicationEntity> spontaneousApplications) {
		this.spontaneousApplications = spontaneousApplications;
	}

	public List<ApplicantEntity> getApplicantsPerTime() {
		return applicantsPerTime;
	}

	public void setApplicantsPerTime(List<ApplicantEntity> applicantsPerTime) {
		this.applicantsPerTime = applicantsPerTime;
	}

	public List<ApplicantEntity> getApplicantsByPosition() {
		return applicantsByPosition;
	}
	
	public List<ApplicationEntity> getApplicationsPerTime() {
		return applicationsPerTime;
	}

	public void setApplicationsPerTime(List<ApplicationEntity> applicationsPerTime) {
		this.applicationsPerTime = applicationsPerTime;
	}

	public List<ApplicationEntity> getApplicationsByPosition() {
		return applicationsByPosition;
	}

	public void setApplicationsByPosition(
			List<ApplicationEntity> applicationsByPosition) {
		this.applicationsByPosition = applicationsByPosition;
	}

	public void setApplicantsByPosition(List<ApplicantEntity> applicantsByPosition) {
		this.applicantsByPosition = applicantsByPosition;
	}

	public List<PositionEntity> getPositions() {
		return positionFacade.findAll();
	}

	public void setPositions(List<PositionEntity> positions) {
		this.positions = positions;
	}

	public List<Object[]> getApplicants() {
		Calendar date1 = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date1.setTime(end);
		date1.add(Calendar.DAY_OF_MONTH, 1);
		date.setTime(begin);
		if (applicants == null) {
			applicants = new ArrayList<>();
			while (date.before(date1)) {
				String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
				applicants.add(new Object[]{(String) day, (Integer) applicationFacade.lstApplicationByDate(date.getTime()).size()});
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return applicants;
	}

	public void setApplicants(List<Object[]> applicants) {
		this.applicants = applicants;
	}

	public List<Object[]> getSpontaneousApplicants() {
		Calendar date1 = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date1.setTime(end);
		date1.add(Calendar.DAY_OF_MONTH, 1);
		date.setTime(begin);
		if (spontaneousApplicants == null) {
			spontaneousApplicants = new ArrayList<>();
			while (date.before(date1)) {
				String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
				spontaneousApplicants.add(new Object[]{(String) day, (Integer) applicationFacade.lstSpontaneousApplicationsByDate(date.getTime()).size()});
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return spontaneousApplicants;
	}

	public void setSpontaneousApplicants(List<Object[]> spontaneousApplicants) {
		this.spontaneousApplicants = spontaneousApplicants;
	}

	public List<Object[]> getApplicantsPerPosition() {
		if (applicantsPerPosition == null) {
			applicantsPerPosition = new ArrayList<>();
			for (int i = 0; i < getPositions().size(); i++) {
				applicantsPerPosition.add(new Object[]{(String) getPositions().get(i).getTitle(),(Integer) applicationFacade.lstApplicationsByPosition(getPositions().get(i)).size()});
			}
		}
		return applicantsPerPosition;
	}

	public void setApplicantsPerPosition(List<Object[]> applicantsPerPosition) {
		this.applicantsPerPosition = applicantsPerPosition;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public PositionFacade getPositionFacade() {
		return positionFacade;
	}

	public void setPositionFacade(PositionFacade positionFacade) {
		this.positionFacade = positionFacade;
	}

	public ManagerFacade getManagerFacade() {
		return managerFacade;
	}

	public void setManagerFacade(ManagerFacade managerFacade) {
		this.managerFacade = managerFacade;
	}

	private BarChartModel initBarModelPosition() {
		BarChartModel modelPosition = new BarChartModel();
		ChartSeries position = new ChartSeries();
		position.setLabel("Position");
		for (int i = 0; i < getPositions().size(); i++) {
			position.set(getPositions().get(i).getTitle(), applicationFacade.lstApplicationsByPosition(getPositions().get(i)).size());
		}
		modelPosition.addSeries(position);
		return modelPosition;
	}

	/**
	 *
	 */
	private void createZoomModel() {
		zoomModelPosition = initBarModelPosition();
		zoomModelPosition.setTitle("Positions");
		zoomModelPosition.setZoom(true);
		zoomModelPosition.setLegendPosition("ne");
		Axis yAxis = zoomModelPosition.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setLabel("# of Applicants");
	}

	private void createBarModelPosition() {
		BarChartModel barModelPosition = new BarChartModel();
		barModelPosition = initBarModelPosition();

		barModelPosition.setTitle("Positions");
		barModelPosition.setLegendPosition("ne");

		Axis xAxis = barModelPosition.getAxis(AxisType.X);
		xAxis.setLabel("Positions");

		Axis yAxis = barModelPosition.getAxis(AxisType.Y);
		yAxis.setLabel("# of Applicants");
		yAxis.setMin(0);
	}

	private void createDateModel() {
		dateModel = new LineChartModel();
		Calendar date1 = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date1.setTime(end);
		date1.add(Calendar.DAY_OF_MONTH, 1);
		date.setTime(begin);
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Applicants Per Time");
		while (date.before(date1)) {
			String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
			series1.set(day, applicationFacade.lstApplicationByDate(date.getTime()).size());
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		dateModel.addSeries(series1);
		dateModel.setTitle("Zoom for Details");
		dateModel.setZoom(true);
		dateModel.getAxis(AxisType.Y).setLabel("# of Applicants");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		String d = new SimpleDateFormat("yyyy-MM-dd").format(end);
		axis.setMax(d);
		axis.setTickFormat("%b %#d, %y");
		dateModel.getAxes().put(AxisType.X, axis);
	}

	private void createDateSpontaneousModel() {
		dateSpontaneousModel = new LineChartModel();
		Calendar date1 = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date1.setTime(end);
		date1.add(Calendar.DAY_OF_MONTH, 1);
		date.setTime(begin);
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Spontaneous Applications Per Time");
		while (date.before(date1)) {
			String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
			series1.set(day, applicationFacade.lstSpontaneousApplicationsByDate(date.getTime()).size());
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		dateSpontaneousModel.addSeries(series1);
		dateSpontaneousModel.setTitle("Zoom for Details");
		dateSpontaneousModel.setZoom(true);
		dateSpontaneousModel.getAxis(AxisType.Y).setLabel("# of Applications");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		String d = new SimpleDateFormat("yyyy-MM-dd").format(end);
		axis.setMax(d);
		axis.setTickFormat("%b %#d, %y");
		dateSpontaneousModel.getAxes().put(AxisType.X, axis);
	}

	private void createDateModel1() {
		dateModel = new LineChartModel();
		Calendar today = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, -31);
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Applicants Per Time");
		while (date.before(today)) {
			String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
			series1.set(day, applicationFacade.lstApplicationByDate(date.getTime()).size());
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		dateModel.addSeries(series1);
		dateModel.setTitle("Zoom for Details");
		dateModel.setZoom(true);
		dateModel.getAxis(AxisType.Y).setLabel("# of Applicants");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMin("2015-09-01");
		axis.setMax("2015-09-31");
		axis.setTickFormat("%b %#d, %y");
		dateModel.getAxes().put(AxisType.X, axis);
	}

	private void createDateSpontaneousModel1() {
		dateSpontaneousModel = new LineChartModel();
		Calendar today = new GregorianCalendar();
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DAY_OF_MONTH, -31);
		LineChartSeries series1 = new LineChartSeries();
		series1.setLabel("Applicants Per Time");
		while (date.before(today)) {
			String day = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
			series1.set(day, applicationFacade.lstSpontaneousApplicationsByDate(date.getTime()).size());
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		dateSpontaneousModel.addSeries(series1);
		dateSpontaneousModel.setTitle("Zoom for Details");
		dateSpontaneousModel.setZoom(true);
		dateSpontaneousModel.getAxis(AxisType.Y).setLabel("# of Applicants");
		DateAxis axis = new DateAxis("Dates");
		axis.setTickAngle(-50);
		axis.setMin("2015-09-01");
		axis.setMax("2015-09-31");
		axis.setTickFormat("%b %#d, %y");
		dateSpontaneousModel.getAxes().put(AxisType.X, axis);
	}

	/**
	 *
	 * @param positionCode
	 * @return PositionEntity
	 */
	public PositionEntity findPositionByCode(Long positionCode) {
		return positionFacade.getPositionByCode(positionCode);
	}

	/**
	 *
	 * @param Date begin
	 * @param Date end
	 * @return List<ApplicantEntity> who have made an application between begin and end dates
	 */
	public List<ApplicantEntity> lstApplicantsPerTime(Date begin, Date end) {
		applicantsPerTime = applicationFacade.lstApplicantsPerTime(begin, end);
		beginChart = begin;
		endChart = end;
		return applicantsPerTime;
	}

	/**
	 *
	 * @return List<ApplicantEntity> of have applied to selectedPosition
	 */
	public List<ApplicantEntity> lstApplicantsByPosition() {
		applicantsByPosition = applicationFacade.lstApplicantsByPosition(findPositionByCode(selectedPosition.getPositionCode()));
		return applicantsByPosition;
	}

	/**
	 *
	 * @param begin
	 * @param end
	 * @return List<ApplicantEntity> who have made a spontaneous application between begin and end dates
	 */
	public List<ApplicationEntity> lstSpontaneousApplications(Date begin, Date end) {
		spontaneousApplications = applicationFacade.lstSpontaneousApplicationsPerTime(begin, end);
		return spontaneousApplications;
	}

	/**
	 *
	 * @param position
	 * @return
	 */
	public boolean showTablePosition(PositionEntity position) {
		return position != null;
	}

	/**
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public boolean showTableTime(Date begin, Date end) {
		return begin != null || end != null;
	}

	/**
	 * Generates PDF applications report and sends email to selectedmanager
	 */
	public void sendApplicationsReport() {
		try {
			GeneratePDF.applicationsReportPdf(getApplicants(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Generates PDF spontaneous applications report and sends email to selectedmanager
	 */
	public void sendSpontaneousReport() {
		try {
			GeneratePDF.applicationsReportPdf(getSpontaneousApplicants(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Generates PDF applications per position report and sends email to selectedmanager
	 */
	public void sendPositionsReport() {
		try {
			GeneratePDF.positionsReportPdf(getApplicantsPerPosition(), selectedmanager);
		} catch (DRException | FileNotFoundException | EmailException ex) {
			Logger.getLogger(ChartReps.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
