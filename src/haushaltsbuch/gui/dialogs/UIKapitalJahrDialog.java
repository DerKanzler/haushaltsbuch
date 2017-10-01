package haushaltsbuch.gui.dialogs;

import haushaltsbuch.bean.util.KapitalCollection;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class UIKapitalJahrDialog extends UIKapitalDialog {

	private KapitalCollection kapital;
	private Integer view;

	public final static int KAPITAL_VERFUEGBAR = 0;
	public final static int KAPITAL_GESPERRT = 1;
	public final static int KAPITAL_GESAMT = 2;
	public final static int KAPITAL_ERSPARNIS = 3;

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
		case (KAPITAL_ERSPARNIS):
			titleLabel.setText("Ersparnis");
			yearInTitle.setVisible(false);
			yearInLabel.setVisible(false);
			yearOutTitle.setVisible(false);
			yearOutLabel.setVisible(false);
			diffTitle.setText("Ersparnis");
			XYPlot plot = (XYPlot)chart.getPlot();
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
			renderer.setSeriesLinesVisible(0, false);
			break;
		}
		
		if (!view.equals(KAPITAL_ERSPARNIS)) {
			yearInLabel.setText(Tools.printBigDecimal(kapital.getYearIn()) + " €");
			yearOutLabel.setText(Tools.printBigDecimal(kapital.getYearOut()) + " €");
		}
		currentLabel.setText(Tools.printBigDecimal(kapital.getCurrent()) + " €");
		diffLabel.setText(Tools.printBigDecimal(kapital.getDiff()) + " €");		
		if (kapital.getDiff().compareTo(new BigDecimal(0)) < 0) diffLabel.setForeground(red);
		else if (kapital.getDiff().compareTo(new BigDecimal(0)) > 0) diffLabel.setForeground(green);
		else diffLabel.setForeground(black);
		
		TableItem ti = new TableItem(table, SWT.NONE);
		Iterator<Map.Entry<Date, BigDecimal>> i = kapital.getValues().entrySet().iterator();
		while (i.hasNext()) {
			Entry<Date, BigDecimal> e = i.next();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(e.getKey());
			ti.setText(gc.get(GregorianCalendar.MONTH), Tools.printBigDecimalWithoutDigits(e.getValue()));
		}
		
		DateAxis axis = (DateAxis)chart.getXYPlot().getDomainAxis();
		GregorianCalendar gc = new GregorianCalendar();
        gc.set(kapital.getYear()-1, Calendar.DECEMBER, 16);   
        Date start = gc.getTime();
        gc.set(kapital.getYear(), Calendar.DECEMBER, 15);   
        axis.setRange(start, gc.getTime());
	}

	/**
	 * Creates the dataset for the chart
	 */
	@Override
	protected TimeSeriesCollection createDataset() {
		TimeSeries series = new TimeSeries("TimeSeries");
		
		Iterator<Map.Entry<Date, BigDecimal>> i = kapital.getValues().entrySet().iterator();
		while (i.hasNext()) {
			Entry<Date, BigDecimal> e = i.next();
			series.add(new Month(e.getKey()), e.getValue());
		}

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

}