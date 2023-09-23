package at.kanzler.haushaltsbuch.gui.dialogs;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.bean.Kontostand;
import at.kanzler.haushaltsbuch.bean.util.KontoCollection;
import at.kanzler.haushaltsbuch.bean.util.SearchBuchung;
import at.kanzler.haushaltsbuch.logic.dialogs.LogicMonatsBuchungen;
import at.kanzler.haushaltsbuch.util.Tools;

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
        Composite composite = (Composite) super.createDialogArea(parent);

        fillFields();

        return composite;
    }

    private void fillFields() {
        titleLabel.setText(konto.getKonto().getKtobez());
        currentLabel.setText(Tools.printBigDecimal(konto.getKonto().getSaldo()) + " €");
        yearInLabel.setText(Tools.printBigDecimal(konto.getYearIn()) + " €");
        yearOutLabel.setText(Tools.printBigDecimal(konto.getYearOut()) + " €");
        diffLabel.setText(Tools.printBigDecimal(konto.getDiff()) + " €");

        if (konto.getDiff().compareTo(new BigDecimal(0)) < 0) {
            diffLabel.setForeground(red);
        } else if (konto.getDiff().compareTo(new BigDecimal(0)) > 0) {
            diffLabel.setForeground(green);
        } else {
            diffLabel.setForeground(black);
        }

        if (type == KAPITAL_CHART) {
            TableItem ti = new TableItem(table, SWT.NONE);
            for (Kontostand kst : konto.getKontostaende()) {
                ti.setText(kst.getKtostdat().getMonthValue() - 1,
                        Tools.printBigDecimalWithoutDigits(kst.getKtostsaldo()));
                System.out.println(kst.getKtostdat().getMonthValue() - 1);
            }

            DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
            LocalDate start = LocalDate.of(konto.getYear() - 1, Month.DECEMBER, 1);
            LocalDate end = LocalDate.of(konto.getYear(), Month.DECEMBER, 15);
            axis.setRange(Tools.toDate(start), Tools.toDate(end));
        } else if (type == KAPITAL_BOOKINGS) {
            SearchBuchung sb = new SearchBuchung();
            sb.setFromAccount(konto.getKonto());
            sb.setToAccount(konto.getKonto());
            sb.setSameAccount(true);
            sb.setFromDate(LocalDate.of(konto.getYear(), Month.JANUARY, 1));
            sb.setToDate(LocalDate.of(konto.getYear(), Month.DECEMBER, 31));
            try {
                LogicMonatsBuchungen logic = new LogicMonatsBuchungen();
                Vector<Buchung> data = logic.search(sb);
                bookingsTable.update(data);
            } catch (Exception e) {
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

        boolean yearInRelevant = false;
        for (Kontostand k : konto.getKontostaende()) {
            series.add(new Month(k.getKtostdat().getMonthValue(), k.getKtostdat().getYear()), k.getKtostsaldo());
            yearInRelevant = java.time.Month.JANUARY.equals(k.getKtostdat().getMonth()) ? true
                    : yearInRelevant || false;
        }
        if (yearInRelevant) {
            series.add(new Month(12, konto.getYear() - 1), konto.getYearIn());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

}