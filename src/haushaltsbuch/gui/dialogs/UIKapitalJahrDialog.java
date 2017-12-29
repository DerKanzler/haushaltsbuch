package haushaltsbuch.gui.dialogs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import haushaltsbuch.bean.util.KapitalCollection;
import haushaltsbuch.util.Tools;

public class UIKapitalJahrDialog extends UIKapitalDialog {

	private KapitalCollection kapital;
	private Integer view;

	public final static int KAPITAL_VERFUEGBAR = 0;
	public final static int KAPITAL_GESPERRT = 1;
	public final static int KAPITAL_GESAMT = 2;

	private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	public UIKapitalJahrDialog(Shell parentShell, Integer view, KapitalCollection kapital) {
		super(parentShell, KAPITAL_CHART);
		this.kapital = kapital;
		this.view = view;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText("Jahresübersicht " + kapital.getYear());
		Composite composite = (Composite) super.createDialogArea(parent);

		fillFields();

		return composite;
	}

	private void fillFields() {
		switch (view) {
		case (KAPITAL_VERFUEGBAR):
			titleLabel.setText("Summe verfügbares Kapital");
			break;
		case (KAPITAL_GESPERRT):
			titleLabel.setText("Summe gesperrtes Kapital");
			break;
		case (KAPITAL_GESAMT):
			titleLabel.setText("Gesamtkapital");
			break;
		}

		yearInLabel.setText(Tools.printBigDecimal(kapital.getYearIn()) + " €");
		yearOutLabel.setText(Tools.printBigDecimal(kapital.getYearOut()) + " €");
		currentLabel.setText(Tools.printBigDecimal(kapital.getCurrent()) + " €");
		diffLabel.setText(Tools.printBigDecimal(kapital.getDiff()) + " €");

		if (kapital.getDiff().compareTo(new BigDecimal(0)) < 0) {
			diffLabel.setForeground(red);
		} else if (kapital.getDiff().compareTo(new BigDecimal(0)) > 0) {
			diffLabel.setForeground(green);
		} else {
			diffLabel.setForeground(black);
		}

		TableItem ti = new TableItem(table, SWT.NONE);
		Iterator<Map.Entry<LocalDate, BigDecimal>> i = kapital.getValues().entrySet().iterator();
		while (i.hasNext()) {
			Entry<LocalDate, BigDecimal> e = i.next();
			ti.setText(e.getKey().getMonthValue() - 1, Tools.printBigDecimalWithoutDigits(e.getValue()));
			System.out.println(e.getKey().getMonthValue() - 1);
		}

		DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
		LocalDate start = LocalDate.of(kapital.getYear() - 1, Month.DECEMBER, 1);
		LocalDate end = LocalDate.of(kapital.getYear(), Month.DECEMBER, 15);
		axis.setRange(Tools.toDate(start), Tools.toDate(end));
	}

	/**
	 * Creates the dataset for the chart
	 */
	@Override
	protected TimeSeriesCollection createDataset() {
		TimeSeries series = new TimeSeries("TimeSeries");

		boolean yearInRelevant = false;
		Iterator<Map.Entry<LocalDate, BigDecimal>> i = kapital.getValues().entrySet().iterator();
		while (i.hasNext()) {
			Entry<LocalDate, BigDecimal> e = i.next();
			series.add(new Month(e.getKey().getMonthValue(), e.getKey().getYear()), e.getValue());
			yearInRelevant = java.time.Month.JANUARY.equals(e.getKey().getMonth()) ? true : yearInRelevant || false;
		}
		if (yearInRelevant) {
			series.add(new Month(12, kapital.getYear() - 1), kapital.getYearIn());
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

}