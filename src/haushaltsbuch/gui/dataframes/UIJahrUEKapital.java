package haushaltsbuch.gui.dataframes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;

import haushaltsbuch.bean.util.KontoCollection;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dialogs.UIKapitalDialog;
import haushaltsbuch.gui.dialogs.UIKapitalJahrDialog;
import haushaltsbuch.gui.dialogs.UIKapitalKontoDialog;
import haushaltsbuch.logic.LogicJahrUEKapital;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.JahrUEKapitalTable;

public class UIJahrUEKapital extends UIAbstractDataFrame implements SelectionListener, ISelectionChangedListener {

	private Spinner yearText;
	private Button previousYearButton, nextYearButton;

	private Label dispCurrentLabel, dispBeginnLabel, dispEndLabel, dispDiffLabel;
	private Label lockCurrentLabel, lockBeginnLabel, lockEndLabel, lockDiffLabel;
	private Label sumCurrentLabel, sumBeginnLabel, sumEndLabel, sumDiffLabel;

	private JahrUEKapitalTable table;
	private Button bookingsButton, detailsButton;

	private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	private LogicJahrUEKapital logic;
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
			LogicMain.instance().getUser().getFormat());

	/**
	 * Constructor: Since UIJahrUEKapital extends Composite the super constructor is
	 * called here with the given arguments. Builds the visible user interface. Uses
	 * GridLayout with 4 columns.
	 * 
	 * @param Composite
	 * @param Integer
	 */
	public UIJahrUEKapital(UIDataFrame parent, Integer style) {
		super(parent, style);

		GridLayout layout = new GridLayout(4, false);
		this.setLayout(layout);

		Label yearLabel = new Label(this, SWT.NONE);
		yearLabel.setText(res.getString("Year"));

		yearText = new Spinner(this, SWT.BORDER);
		yearText.setTextLimit(4);
		yearText.setIncrement(1);
		yearText.addSelectionListener(this);

		previousYearButton = new Button(this, SWT.FLAT);
		previousYearButton.setImage(
				new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/back.png")));
		previousYearButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		previousYearButton.addSelectionListener(this);

		nextYearButton = new Button(this, SWT.FLAT);
		nextYearButton.setImage(new Image(this.getDisplay(),
				this.getClass().getClassLoader().getResourceAsStream("images/forward.png")));
		nextYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));
		nextYearButton.addSelectionListener(this);

		Group summaryGroup = new Group(this, SWT.NONE);
		GridData summaryGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		summaryGroupGD.horizontalSpan = 4;
		summaryGroup.setLayoutData(summaryGroupGD);
		// summaryGroup.setText(res.getString("Summary"));
		// Calls the method to fill this group with the appropiate widgets.
		buildSummaryGroup(summaryGroup);

		Composite accountsGroup = new Composite(this, SWT.NONE);
		GridData accountsGroupGD = new GridData(GridData.FILL_BOTH);
		accountsGroupGD.horizontalSpan = 4;
		accountsGroup.setLayoutData(accountsGroupGD);
		// accountsGroup.setText(res.getString("Accounts"));
		accountsGroup.setLayout(new GridLayout(4, false));

		table = new JahrUEKapitalTable(accountsGroup, 4);
		table.getTableViewer().addSelectionChangedListener(this);

		bookingsButton = new Button(accountsGroup, SWT.PUSH);
		GUITools.setButtonLayout(bookingsButton, 3);
		bookingsButton.setText("Buchungen");
		bookingsButton.setEnabled(false);
		bookingsButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				KontoCollection k = table.getSelectedAccount();
				new UIKapitalKontoDialog(Display.getDefault().getActiveShell(), UIKapitalDialog.KAPITAL_BOOKINGS, k)
						.open();
			}

		});

		detailsButton = new Button(accountsGroup, SWT.PUSH);
		GUITools.setButtonLayout(detailsButton, 1);
		detailsButton.setText("Details");
		detailsButton.setEnabled(false);
		detailsButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				KontoCollection k = table.getSelectedAccount();
				new UIKapitalKontoDialog(Display.getDefault().getActiveShell(), UIKapitalDialog.KAPITAL_CHART, k)
						.open();
			}

		});

		fillFields(Tools.getLastMonth().getYear());
	}

	private void buildSummaryGroup(Group group) {
		GridLayout layout = new GridLayout(8, false);
		group.setLayout(layout);

		Label dispTitleLabel = new Label(group, SWT.LEFT);
		GUITools.setBoldFont(dispTitleLabel);
		GridData titleLabelGD = new GridData(GridData.FILL_HORIZONTAL);
		titleLabelGD.horizontalSpan = 3;
		dispTitleLabel.setLayoutData(titleLabelGD);
		dispTitleLabel.setText("Summe verfügbares Kapital");

		new Label(group, SWT.NONE).setText("Aktueller Stand");
		dispCurrentLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(dispCurrentLabel);
		dispCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button dispChartButton = new Button(group, SWT.FLAT);
		dispChartButton.setImage(
				new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
		GridData chartButtonGD = new GridData(GridData.HORIZONTAL_ALIGN_END);
		chartButtonGD.horizontalSpan = 3;
		dispChartButton.setLayoutData(chartButtonGD);
		dispChartButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				new UIKapitalJahrDialog(Display.getDefault().getActiveShell(), UIKapitalJahrDialog.KAPITAL_VERFUEGBAR,
						logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_VERFUEGBAR)).open();
			}

		});

		new Label(group, SWT.NONE).setText(res.getString("BeginningYear"));
		dispBeginnLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(dispBeginnLabel);
		dispBeginnLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Jahresausgang");
		dispEndLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(dispEndLabel);
		dispEndLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Differenz");
		dispDiffLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(dispDiffLabel);
		dispDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridData separatorGD = new GridData(GridData.FILL_HORIZONTAL);
		separatorGD.horizontalSpan = 8;
		new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorGD);

		Label lockTitleLabel = new Label(group, SWT.LEFT);
		GUITools.setBoldFont(lockTitleLabel);
		lockTitleLabel.setLayoutData(titleLabelGD);
		lockTitleLabel.setText("Summe gesperrtes Kapital");

		new Label(group, SWT.NONE).setText("Aktueller Stand");
		lockCurrentLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(lockCurrentLabel);
		lockCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button lockChartButton = new Button(group, SWT.FLAT);
		lockChartButton.setImage(
				new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
		lockChartButton.setLayoutData(chartButtonGD);
		lockChartButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				new UIKapitalJahrDialog(Display.getDefault().getActiveShell(), UIKapitalJahrDialog.KAPITAL_GESPERRT,
						logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_GESPERRT)).open();
			}

		});

		new Label(group, SWT.NONE).setText(res.getString("BeginningYear"));
		lockBeginnLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(lockBeginnLabel);
		lockBeginnLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Jahresausgang");
		lockEndLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(lockEndLabel);
		lockEndLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Differenz");
		lockDiffLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(lockDiffLabel);
		lockDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorGD);

		Label sumTitleLabel = new Label(group, SWT.LEFT);
		GUITools.setBoldFont(sumTitleLabel);
		sumTitleLabel.setLayoutData(titleLabelGD);
		sumTitleLabel.setText("Gesamtkapital");

		new Label(group, SWT.NONE).setText("Aktueller Stand");
		sumCurrentLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(sumCurrentLabel);
		sumCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button sumChartButton = new Button(group, SWT.FLAT);
		sumChartButton.setImage(
				new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
		sumChartButton.setLayoutData(chartButtonGD);
		sumChartButton.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event e) {
				new UIKapitalJahrDialog(Display.getDefault().getActiveShell(), UIKapitalJahrDialog.KAPITAL_GESAMT,
						logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_GESAMT)).open();
			}

		});

		new Label(group, SWT.NONE).setText(res.getString("BeginningYear"));
		sumBeginnLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(sumBeginnLabel);
		sumBeginnLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Jahresausgang");
		sumEndLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(sumEndLabel);
		sumEndLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NONE).setText("Differenz");
		sumDiffLabel = new Label(group, SWT.RIGHT);
		GUITools.setBoldFont(sumDiffLabel);
		sumDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void fillFields(Integer year) {
		LocalDate lastDate = Tools.getLastDate();
		if (Month.JANUARY.equals(lastDate.getMonth())) {
			yearText.setMaximum(lastDate.getYear() - 1);
			if (year == (lastDate.getYear() - 1)) {
				year = year - 1;
			}
		} else {
			yearText.setMaximum(Tools.getLastDate().getYear());
		}
		yearText.setMinimum(Tools.getFirstDate().getYear());
		yearText.setSelection(year);
		if (year == yearText.getMinimum()) {
			previousYearButton.setEnabled(false);
		} else
			previousYearButton.setEnabled(true);
		if (year == yearText.getMaximum()) {
			nextYearButton.setEnabled(false);
		} else
			nextYearButton.setEnabled(true);

		logic = new LogicJahrUEKapital(year);

		dispCurrentLabel.setText(Tools.printBigDecimal(logic.getDispCurrent()) + " €");
		dispBeginnLabel.setText(Tools.printBigDecimal(logic.getDispBeginn()) + " €");
		dispEndLabel.setText(Tools.printBigDecimal(logic.getDispEnd()) + " €");
		dispDiffLabel.setText(Tools.printBigDecimal(logic.getDispDiff()) + " €");
		if (logic.getDispDiff().compareTo(new BigDecimal(0)) < 0)
			dispDiffLabel.setForeground(red);
		else if (logic.getDispDiff().compareTo(new BigDecimal(0)) > 0)
			dispDiffLabel.setForeground(green);
		else
			dispDiffLabel.setForeground(black);

		lockCurrentLabel.setText(Tools.printBigDecimal(logic.getLockCurrent()) + " €");
		lockBeginnLabel.setText(Tools.printBigDecimal(logic.getLockBeginn()) + " €");
		lockEndLabel.setText(Tools.printBigDecimal(logic.getLockEnd()) + " €");
		lockDiffLabel.setText(Tools.printBigDecimal(logic.getLockDiff()) + " €");
		if (logic.getLockDiff().compareTo(new BigDecimal(0)) < 0)
			lockDiffLabel.setForeground(red);
		else if (logic.getLockDiff().compareTo(new BigDecimal(0)) > 0)
			lockDiffLabel.setForeground(green);
		else
			lockDiffLabel.setForeground(black);

		sumCurrentLabel.setText(Tools.printBigDecimal(logic.getSumCurrent()) + " €");
		sumBeginnLabel.setText(Tools.printBigDecimal(logic.getSumBeginn()) + " €");
		sumEndLabel.setText(Tools.printBigDecimal(logic.getSumEnd()) + " €");
		sumDiffLabel.setText(Tools.printBigDecimal(logic.getSumDiff()) + " €");
		if (logic.getSumDiff().compareTo(new BigDecimal(0)) < 0)
			sumDiffLabel.setForeground(red);
		else if (logic.getSumDiff().compareTo(new BigDecimal(0)) > 0)
			sumDiffLabel.setForeground(green);
		else
			sumDiffLabel.setForeground(black);

		table.update(logic.getKonten());
	}

	/**
	 * Does nothing.
	 */
	public void widgetDefaultSelected(SelectionEvent se) {
	}

	/**
	 * Catches the SelectionEvent when a button is clicked.
	 */
	public void widgetSelected(SelectionEvent se) {
		if (se.getSource().equals(previousYearButton)) {
			fillFields(yearText.getSelection() - 1);
		} else if (se.getSource().equals(nextYearButton)) {
			fillFields(yearText.getSelection() + 1);
		} else if (se.getSource().equals(yearText)) {
			fillFields(yearText.getSelection());
		}
	}

	/**
	 * Catches the event when the selected row of the table is changed.
	 * Enables/diables the edit button then.
	 */
	public void selectionChanged(SelectionChangedEvent sce) {
		if (table.getSelectedAccount() != null) {
			bookingsButton.setEnabled(true);
			detailsButton.setEnabled(true);
		} else {
			bookingsButton.setEnabled(false);
			detailsButton.setEnabled(false);
		}
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		fillFields(Integer.valueOf(yearText.getText()));
		setToBeUpdated(false);
	}

}