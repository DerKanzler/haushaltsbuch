package haushaltsbuch.gui.dialogs;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.bean.util.KontoCollection;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.logic.dialogs.LogicMonatsBuchungen;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

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

public class UIKapitalKontoDialog extends UIKapitalDialog {
	
	private KontoCollection konto;
	
	private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	public UIKapitalKontoDialog(Shell parentShell, Integer type, KontoCollection konto) {
		super(parentShell, type);
		this.konto = konto;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.getShell().setText("Jahresübersicht " + konto.getYear() + " - " + konto.getKonto().getKuerzel());
		Composite composite = (Composite)super.createDialogArea(parent);
	
		fillFields();
		
		return composite;
	}

	private void fillFields() {
		titleLabel.setText(konto.getKonto().getKtobez());
		currentLabel.setText(Tools.printBigDecimal(konto.getKonto().getSaldo()) + " €");
		yearInLabel.setText(Tools.printBigDecimal(konto.getYearIn()) + " €");
		yearOutLabel.setText(Tools.printBigDecimal(konto.getYearOut()) + " €");
		diffLabel.setText(Tools.printBigDecimal(konto.getDiff()) + " €");
		if (konto.getDiff().compareTo(new BigDecimal(0)) < 0) diffLabel.setForeground(red);
		else if (konto.getDiff().compareTo(new BigDecimal(0)) > 0) diffLabel.setForeground(green);
		else diffLabel.setForeground(black);
		if (type == KAPITAL_CHART) {
			TableItem ti = new TableItem(table, SWT.NONE);
			for (Kontostand kst: konto.getKontostaende()) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(kst.getKtostdat());
				ti.setText(gc.get(GregorianCalendar.MONTH), Tools.printBigDecimalWithoutDigits(kst.getKtostsaldo()));
			}
			
			DateAxis axis = (DateAxis)chart.getXYPlot().getDomainAxis();
			GregorianCalendar gc = new GregorianCalendar();
	        gc.set(konto.getYear()-1, Calendar.DECEMBER, 16);   
	        Date start = gc.getTime();
	        gc.set(konto.getYear(), Calendar.DECEMBER, 15);   
	        axis.setRange(start, gc.getTime());
		}
		else if (type == KAPITAL_BOOKINGS) {
			SearchBuchung sb = new SearchBuchung();
			sb.setFromAccount(konto.getKonto());
			sb.setToAccount(konto.getKonto());
			sb.setSameAccount(true);
			GregorianCalendar gc = new GregorianCalendar();
			gc.set(konto.getYear(), GregorianCalendar.JANUARY, 1);
			sb.setFromDate(gc.getTime());
			gc.set(konto.getYear(), GregorianCalendar.DECEMBER, 31, 23, 59, 59);
			sb.setToDate(gc.getTime());
			try {
				LogicMonatsBuchungen logic = new LogicMonatsBuchungen();
				Vector<Buchung> data = logic.search(sb);
				bookingsTable.update(data);
			}
			catch (Exception e) {
				bookingsTable.getTable().setVisible(false);
			}
		}
	}

	/**
	 * Creates the dataset for the chart
	 */
	@Override
	protected TimeSeriesCollection createDataset() {
		TimeSeries series = new TimeSeries("TimeSeries");
		for (Kontostand k: konto.getKontostaende()) {
			series.add(new Month(k.getKtostdat()), k.getKtostsaldo());
		}
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
	}
	
}