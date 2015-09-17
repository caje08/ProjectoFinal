/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.PieChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 *
 * @author
 */
public class ChartDynamicReports {

	private String xAxisKey;
	private String yAxisKey;
	private String xAxisTitle;
	private String yAxisTitle;
	private String chartTitle;
	private String reportTitle;
	private List<Object[]> series;
	private final String PDFDESTINATION = BundleUtils.getSettings("pdfgeneratefile");

	public ChartDynamicReports(List<Object[]> series, String xAxisKey, String yAxisKey, String xAxisTitle, String yAxisTitle, String chartTitle, String reportTitle) {
		this.series = series;
		this.xAxisKey = xAxisKey;
		this.yAxisKey = yAxisKey;
		this.xAxisTitle = xAxisTitle;
		this.yAxisTitle = yAxisTitle;
		this.chartTitle = chartTitle;
		this.reportTitle = reportTitle;
	}

	/**
	 *
	 * @return pdf file containing bar chart and table
	 * @throws FileNotFoundException
	 * @throws DRException
	 */
	public JasperReportBuilder buildBarChart() throws FileNotFoundException, DRException {
		JasperReportBuilder report = report();
		//constroi tabela com dados enviados para o grafico
		FontBuilder boldFont = stl.fontArial().setFontSize(8);

		File pdfFile = new File(PDFDESTINATION);

		TextColumnBuilder<String> outcomeColumn = col.column(xAxisTitle, xAxisKey, type.stringType());
		TextColumnBuilder<Integer> qtApplicantsColumn = col.column(yAxisTitle, yAxisKey, type.integerType());

		JasperReportBuilder subreport = report();
		subreport
		.setTemplate(Templates.reportTemplate)
		.columns(outcomeColumn, qtApplicantsColumn)
		.setDataSource(createDataSource());

		BarChartBuilder chart = cht.barChart()
				.customizers(new ChartCustomizer())
				.setTitle(chartTitle)
				.setCategory(outcomeColumn)//xxAxis
				.setShowTickMarks(Boolean.TRUE)
				.setShowValues(Boolean.TRUE)
				.series(cht.serie(qtApplicantsColumn))//yyAxis
				.setCategoryAxisFormat(cht.axisFormat().setLabel(xAxisTitle));
		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdirs();
			}
			report
			.setTemplate(Templates.reportTemplate)
			.title(Templates.createTitleComponent(reportTitle))
			.summary(
					cmp.verticalGap(20),
					cmp.horizontalList(chart, cmp.subreport(subreport)))
					.setDataSource(createDataSource())
					.toPdf(Exporters.pdfExporter(PDFDESTINATION+"/report.pdf"));
		} catch (DRException e) {
			e.printStackTrace();
		}
		return report;
	}

	/**
	 *
	 * @return pdf file containing pie chart and table
	 * @throws FileNotFoundException
	 * @throws DRException
	 */
	public JasperReportBuilder buildPieChart() throws FileNotFoundException, DRException {
		JasperReportBuilder report = report();
		//constroi tabela com dados enviados para o grafico
		FontBuilder boldFont = stl.fontArial().setFontSize(8);

		File pdfFile = new File(PDFDESTINATION);
		TextColumnBuilder<String> motives = col.column(xAxisTitle, xAxisKey, type.stringType());
		TextColumnBuilder<Integer> numApplicants = col.column(yAxisTitle, yAxisKey, type.integerType());

		PieChartBuilder pieChart = cht.pieChart()
				.setTitle(chartTitle)
				.setShowPercentages(Boolean.TRUE)
				.setShowValues(Boolean.TRUE)
				.setKey(motives)
				.series(cht.serie(numApplicants));

		BarChartBuilder chart = cht.barChart()
				.customizers(new ChartCustomizer())
				.setTitle(chartTitle)
				.setCategory(motives)//xxAxis
				.setShowTickMarks(Boolean.TRUE)
				.setShowValues(Boolean.TRUE)
				.series(cht.serie(numApplicants))//yyAxis
				.setCategoryAxisFormat(cht.axisFormat().setLabel(xAxisTitle));
		cht.barChart()
		.customizers(new ChartCustomizer())
		.setTitle(chartTitle)
		.setCategory(motives)//xxAxis
		.setShowTickMarks(Boolean.TRUE)
		.setShowValues(Boolean.TRUE)
		.series(cht.serie(numApplicants))//yyAxis
		.setCategoryAxisFormat(cht.axisFormat().setLabel(xAxisTitle));

		try {
			if (!pdfFile.exists()) {
				pdfFile.mkdirs();
			}
			report
			.setTemplate(Templates.reportTemplate)
			.title(Templates.createTitleComponent(reportTitle))
			.summary(
					cmp.verticalGap(20),
					cmp.horizontalList(chart, pieChart)
					.newRow()
					.add(cmp.line()))
					.columns(motives, numApplicants)
					.setDataSource(createDataSource())
					.toPdf(Exporters.pdfExporter(PDFDESTINATION+"/report.pdf"));
		} catch (DRException e) {
			e.printStackTrace();
		}
		return report;
	}

	/**
	 * Generated datasource to present in graphics. Item (xx axis),
	 *
	 * @return
	 */
	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource(xAxisKey, yAxisKey);
		for (Object[] o : series) {
			dataSource.add(o[0], o[1]);
		}
		return dataSource;
	}

	private JRDataSource createDataSourceInterview() {
		DRDataSource dataSource = new DRDataSource(xAxisKey, yAxisKey, yAxisKey);
		for (Object[] o : series) {
			dataSource.add(o[0], o[1], o[2]);
		}
		return dataSource;
	}

	private ComponentBuilder<?, ?> components(String label1, ComponentBuilder<?, ?> component1, String label2, ComponentBuilder<?, ?> component2, String label3, ComponentBuilder<?, ?> component3) {
		HorizontalListBuilder list = cmp.horizontalList()
				.setGap(10);
		list.add(component(label1, component1));
		list.add(component(label2, component2));
		list.add(component(label3, component3));
		return list;
	}

	private ComponentBuilder<?, ?> component(String label, ComponentBuilder<?, ?> component) {
		TextFieldBuilder<String> labelField = cmp.text(label)
				.setFixedRows(1)
				.setStyle(Templates.bold8LeftStyle);
		return cmp.verticalList(labelField, component);
	}

	private class ChartCustomizer implements DRIChartCustomizer, Serializable {

		private static final long serialVersionUID = 1L;

		@Override
		public void customize(JFreeChart chart, ReportParameters reportParameters) {
			BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
			renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			renderer.setBaseItemLabelsVisible(true);
		}
	}

	public String getxAxisKey() {
		return xAxisKey;
	}

	public void setxAxisKey(String xAxisKey) {
		this.xAxisKey = xAxisKey;
	}

	public String getyAxisKey() {
		return yAxisKey;
	}

	public void setyAxisKey(String yAxisKey) {
		this.yAxisKey = yAxisKey;
	}

	public String getxAxisTitle() {
		return xAxisTitle;
	}

	public void setxAxisTitle(String xAxisTitle) {
		this.xAxisTitle = xAxisTitle;
	}

	public String getyAxisTitle() {
		return yAxisTitle;
	}

	public void setyAxisTitle(String yAxisTitle) {
		this.yAxisTitle = yAxisTitle;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public List<Object[]> getSeries() {
		return series;
	}

	public void setSeries(List<Object[]> series) {
		this.series = series;
	}

	@Override
	public String toString() {
		return "BarChartReport{" + "chartTitle=" + chartTitle + ", reportTitle=" + reportTitle + '}';
	}

}
