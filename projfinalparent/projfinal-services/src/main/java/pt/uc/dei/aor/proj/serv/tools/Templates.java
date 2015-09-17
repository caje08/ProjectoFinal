/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 *
 * @author
 */

public class Templates {

	public static final StyleBuilder rootStyle;
	public static final StyleBuilder boldStyle;
	public static final StyleBuilder italicStyle;
	public static final StyleBuilder boldCenteredStyle;
	public static final StyleBuilder boldLeftStyle;
	public static final StyleBuilder bold8LeftStyle;
	public static final StyleBuilder bold8CenteredStyle;
	public static final StyleBuilder bold10CenteredStyle;
	public static final StyleBuilder bold14CenteredStyle;
	public static final StyleBuilder columnStyle;
	public static final StyleBuilder columnTitleStyle;
	public static final StyleBuilder groupStyle;
	public static final StyleBuilder subtotalStyle;
	public static final ReportTemplateBuilder reportTemplate;
	public static final CurrencyType currencyType;
	public static final ComponentBuilder<?, ?> dynamicReportsComponent;
	public static final ComponentBuilder<?, ?> footerComponent;

	static {
		rootStyle = stl.style().setPadding(0);
		boldStyle = stl.style(rootStyle).bold();
		italicStyle = stl.style(rootStyle).italic();
		boldCenteredStyle = stl.style(boldStyle)
				.setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		boldLeftStyle = stl.style(boldStyle)
				.setAlignment(HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
		bold8LeftStyle = stl.style(boldLeftStyle)
				.setFontSize(8);
		bold8CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(8);
		bold10CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(10);
		bold14CenteredStyle = stl.style(boldCenteredStyle)
				.setFontSize(14);
		columnStyle = stl.style(rootStyle).setAlignment(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
		columnTitleStyle = stl.style(columnStyle)
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setBackgroundColor(new Color(213, 210, 202))
				.bold();
		groupStyle = stl.style(boldStyle)
				.setHorizontalAlignment(HorizontalAlignment.LEFT);
		subtotalStyle = stl.style(boldStyle)
				.setTopBorder(stl.pen1Point());
		StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
		StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(118, 106, 98));
		StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
				.setBackgroundColor(new Color(213, 210, 202));
		StyleBuilder crosstabCellStyle = stl.style(columnStyle)
				.setBorder(stl.pen1Point());
		TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
				.setHeadingStyle(0, stl.style(rootStyle).bold());
		reportTemplate = template()
				.setLocale(Locale.ENGLISH)
				.setColumnStyle(columnStyle)
				.setColumnTitleStyle(columnTitleStyle)
				.setGroupStyle(groupStyle)
				.setGroupTitleStyle(groupStyle)
				.setSubtotalStyle(subtotalStyle)
				.highlightDetailEvenRows()
				.crosstabHighlightEvenRows()
				.setCrosstabGroupStyle(crosstabGroupStyle)
				.setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
				.setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
				.setCrosstabCellStyle(crosstabCellStyle)
				.setTableOfContentsCustomizer(tableOfContentsCustomizer);
		currencyType = new CurrencyType();
		HyperLinkBuilder link = hyperLink("http://www.acertarorumo.pt/");
		dynamicReportsComponent
		= cmp.horizontalList(
				cmp.verticalList(
						cmp.text("Acertar o Rumo").setStyle(bold8CenteredStyle).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text("http://www.acertarorumo.pt/").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(200);
		footerComponent = cmp.pageXofY()
				.setStyle(
						stl.style(boldCenteredStyle)
						.setTopBorder(stl.pen1Point()));
	}

	/**
	 * Creates custom component which is possible to add to any report band
	 * component
	 *
	 * @param label
	 * @return
	 */
	public static ComponentBuilder<?, ?> createTitleComponent(String label) {
		return cmp.horizontalList()
				.add(
						dynamicReportsComponent,
						cmp.text(label).setStyle(bold10CenteredStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT))
						.newRow()
						.add(cmp.line())
						.newRow()
						.add(cmp.verticalGap(25));
	}

	public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
		return new CurrencyValueFormatter(label);
	}

	public static class CurrencyType extends BigDecimalType {

		private static final long serialVersionUID = 1L;

		@Override
		public String getPattern() {
			return "$ #,###.00";
		}
	}

	private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {

		private static final long serialVersionUID = 1L;
		private String label;

		public CurrencyValueFormatter(String label) {
			this.label = label;
		}

		@Override
		public String format(Number value, ReportParameters reportParameters) {
			return label + currencyType.valueToString(value, reportParameters.getLocale());
		}
	}

}
