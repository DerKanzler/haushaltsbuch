package haushaltsbuch.gui.dialogs;

import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import haushaltsbuch.dao.BenutzerDAO;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.widgets.AutoResizeTableLayout;
import haushaltsbuch.widgets.BookingsTable;

public abstract class UIKapitalDialog extends Dialog {

    protected Integer type = KAPITAL_CHART;

    public final static Integer KAPITAL_CHART = 0;
    public final static Integer KAPITAL_BOOKINGS = 1;

    protected Label yearInTitle, yearOutTitle, diffTitle;

    protected Label titleLabel, currentLabel, yearInLabel, yearOutLabel, diffLabel;
    protected BookingsTable bookingsTable;
    protected Table table;
    protected JFreeChart chart;

    protected UIKapitalDialog(Shell parentShell, Integer type) {
        super(parentShell);
        this.type = type;
    }

    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    @Override
    protected void initializeBounds() {
        Point p = getInitialLocation(new Point(1000, 800));
        getShell().setBounds(p.x, p.y, 1000, 800);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, "Schließen", true);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        GridLayout layout = new GridLayout(8, false);
        layout.marginTop = 10;
        layout.marginWidth = 20;
        composite.setLayout(layout);

        titleLabel = new Label(composite, SWT.LEFT);
        GUITools.setBoldFont(titleLabel);
        GridData titleLabelGD = new GridData(GridData.FILL_HORIZONTAL);
        titleLabelGD.horizontalSpan = 6;
        titleLabel.setLayoutData(titleLabelGD);

        new Label(composite, SWT.NONE).setText("Aktueller Stand");
        currentLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(currentLabel);
        currentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        yearInTitle = new Label(composite, SWT.NONE);
        yearInTitle.setText(res.getString("BeginningYear"));
        yearInLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(yearInLabel);
        yearInLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        yearOutTitle = new Label(composite, SWT.NONE);
        yearOutTitle.setText("Jahresausgang");
        yearOutLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(yearOutLabel);
        yearOutLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        diffTitle = new Label(composite, SWT.NONE);
        diffTitle.setText("Differenz");
        diffLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(diffLabel);
        diffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        if (type == KAPITAL_CHART) {
            table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL);
            GridData tableGD = new GridData(GridData.FILL_HORIZONTAL);
            tableGD.horizontalSpan = 8;
            table.setLayoutData(tableGD);
            table.setHeaderVisible(true);
            table.setLinesVisible(true);

            AutoResizeTableLayout autoTableLayout = new AutoResizeTableLayout(table);
            for (String month : new DateFormatSymbols(BenutzerDAO.instance().getUser().getFormat()).getMonths()) {
                if (month != null && month.isEmpty() == false) {
                    TableColumn column = new TableColumn(table, SWT.RIGHT);
                    column.setText(month);
                    column.setMoveable(false);
                    autoTableLayout.addColumnData(new ColumnWeightData(1));
                }
            }
            table.setLayout(autoTableLayout);

            chart = createChart(createDataset());
            final ChartComposite chartComposite = new ChartComposite(composite, SWT.NONE, chart, false, false, false,
                    false, true);
            GridData chartGD = new GridData(GridData.FILL_BOTH);
            chartGD.horizontalSpan = 8;
            chartComposite.setLayoutData(chartGD);
        } else if (type == KAPITAL_BOOKINGS) {
            bookingsTable = new BookingsTable(composite, 8);
        }
        return composite;
    }

    @Override
    public void create() {
        super.create();
        this.getButton(IDialogConstants.CANCEL_ID).setFocus();
    }

    /**
     * Creates the dataset for the chart
     */
    protected abstract TimeSeriesCollection createDataset();

    /**
     * Creates the chart based on a dataset
     */
    protected JFreeChart createChart(TimeSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart("", "", "", dataset, false, true, false);

        Color compBG = Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
        chart.setBackgroundPaint(new java.awt.Color(compBG.getRed(), compBG.getGreen(), compBG.getBlue()));
        chart.setBorderVisible(false);
        chart.setAntiAlias(true);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setNoDataMessage("No data available");
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setOutlinePaint(java.awt.Color.LIGHT_GRAY);
        plot.setBackgroundPaint(new java.awt.Color(210, 220, 230));
        plot.setDomainCrosshairPaint(new java.awt.Color(130, 150, 180));
        plot.setRangeCrosshairPaint(new java.awt.Color(130, 150, 180));

        NumberAxis na = (NumberAxis) plot.getRangeAxis();
        na.setNumberFormatOverride(NumberFormat.getInstance(BenutzerDAO.instance().getUser().getFormat()));
        plot.setRangeAxis(na);

        DateAxis axis = new DateAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMMM", BenutzerDAO.instance().getUser().getFormat()));
        plot.setDomainAxis(axis);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new java.awt.Color(254, 184, 0));
        renderer.setSeriesShape(0, new Ellipse2D.Double(0 - 5.0, 0 - 5.0, 2.0 * 5.0, 2.0 * 5.0), false);
        renderer.setSeriesFillPaint(0, java.awt.Color.WHITE, false);
        renderer.setUseFillPaint(true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f), false);

        return chart;
    }

}