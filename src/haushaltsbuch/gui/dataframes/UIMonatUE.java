package haushaltsbuch.gui.dataframes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.Vector;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.LogicMonatUE;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.CostTypeGroupsComposite;

public class UIMonatUE extends UIAbstractDataFrame implements SelectionListener {

	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
			LogicMain.instance().getUser().getFormat());

	private LogicMonatUE logic;

	private Composite accountsComposite, costtypesComposite;

	private Button nextMonthButton, previousMonthButton;
	private CDateTime monthText;
	private Label disposableAmountData, offlimitAmountData, totalAssetsData, totalRevenuesData, totalExpensesData,
			totalSavingsData;

	private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	/**
	 * Constructor: Since UIMonatUE extends Composite the super constructor is
	 * called here with the given arguments. Builds the visible user interface. Uses
	 * GridLayout with 4 columns.
	 * 
	 * @param Composite
	 * @param Integer
	 */
	public UIMonatUE(UIDataFrame parent, Integer style) {
		super(parent, style);

		GridLayout layout = new GridLayout(4, false);
		this.setLayout(layout);

		new Label(this, SWT.NONE).setText(res.getString("Month"));

		monthText = new CDateTime(this, CDT.BORDER | CDT.DROP_DOWN | CDT.BUTTON_RIGHT | CDT.COMPACT);
		GridData monthTextGD = new GridData(GridData.GRAB_HORIZONTAL);
		monthTextGD.widthHint = 133;
		monthText.setLayoutData(monthTextGD);
		monthText.setLocale(LogicMain.instance().getUser().getFormat());
		monthText.setPattern("MMMM yyyy");
		monthText.setSelection(Tools.toDate(Tools.getLastMonth()));
		monthText.addSelectionListener(this);

		previousMonthButton = new Button(this, SWT.FLAT);
		previousMonthButton.setImage(
				new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/back.png")));
		previousMonthButton.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		previousMonthButton.addSelectionListener(this);

		nextMonthButton = new Button(this, SWT.FLAT);
		nextMonthButton.setImage(new Image(this.getDisplay(),
				this.getClass().getClassLoader().getResourceAsStream("images/forward.png")));
		nextMonthButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));
		nextMonthButton.addSelectionListener(this);

		Group summaryGroup = new Group(this, SWT.NONE);
		GridData summaryGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		summaryGroupGD.horizontalSpan = 4;
		summaryGroup.setLayoutData(summaryGroupGD);
		summaryGroup.setText(res.getString("Summary"));
		// Calls the method to fill this group with the appropiate widgets.
		buildSummaryGroup(summaryGroup);

		Group accountsGroup = new Group(this, SWT.NONE);
		GridData accountsGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		accountsGroupGD.horizontalSpan = 4;
		accountsGroup.setLayoutData(accountsGroupGD);
		accountsGroup.setText(res.getString("Accounts"));
		buildAccountsGroup(accountsGroup);

		Group costtypesGroup = new Group(this, SWT.NONE);
		GridData costtypesGroupGD = new GridData(GridData.FILL_BOTH);
		costtypesGroupGD.horizontalSpan = 4;
		costtypesGroup.setLayoutData(costtypesGroupGD);
		costtypesGroup.setText(res.getString("Costtypes"));
		buildCosttypesGroup(costtypesGroup);

		fillFields(Tools.getLastMonth());
	}

	private void buildSummaryGroup(Group group) {
		GridLayout layout = new GridLayout(8, false);
		group.setLayout(layout);

		Label disposableAmountLabel = new Label(group, SWT.LEFT);
		disposableAmountLabel.setText(res.getString("DisposableAmount"));
		disposableAmountData = new Label(group, SWT.RIGHT);
		disposableAmountData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(disposableAmountData);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label offlimitAmountLabel = new Label(group, SWT.LEFT);
		offlimitAmountLabel.setText(res.getString("OfflimitAmount"));
		offlimitAmountData = new Label(group, SWT.RIGHT);
		offlimitAmountData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(offlimitAmountData);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label totalAssetsLabel = new Label(group, SWT.LEFT);
		totalAssetsLabel.setText(res.getString("TotalAssets"));
		totalAssetsData = new Label(group, SWT.RIGHT);
		totalAssetsData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(totalAssetsData);

		Label totalRevenuesLabel = new Label(group, SWT.LEFT);
		totalRevenuesLabel.setText(res.getString("TotalRevenues"));
		totalRevenuesData = new Label(group, SWT.RIGHT);
		totalRevenuesData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(totalRevenuesData);
		totalRevenuesData.setForeground(green);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label totalExpensesLabel = new Label(group, SWT.LEFT);
		totalExpensesLabel.setText(res.getString("TotalExpenses"));
		totalExpensesData = new Label(group, SWT.RIGHT);
		totalExpensesData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(totalExpensesData);
		totalExpensesData.setForeground(red);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label totalSavingsLabel = new Label(group, SWT.LEFT);
		totalSavingsLabel.setText(res.getString("TotalSavings"));
		totalSavingsData = new Label(group, SWT.RIGHT);
		totalSavingsData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(totalSavingsData);
	}

	private void fillSummaryGroup() {
		disposableAmountData.setText(Tools.printBigDecimal(logic.getDisposableSum()) + " €");
		if (logic.getDisposableSum().compareTo(new BigDecimal(0)) < 0)
			disposableAmountData.setForeground(red);
		else
			disposableAmountData.setForeground(black);

		offlimitAmountData.setText(Tools.printBigDecimal(logic.getOffLimitSum()) + " €");
		if (logic.getOffLimitSum().compareTo(new BigDecimal(0)) < 0)
			offlimitAmountData.setForeground(red);
		else
			offlimitAmountData.setForeground(black);

		totalAssetsData.setText(Tools.printBigDecimal(logic.getTotalAssets()) + " €");
		if (logic.getTotalAssets().compareTo(new BigDecimal(0)) < 0)
			totalAssetsData.setForeground(red);
		else
			totalAssetsData.setForeground(black);

		totalRevenuesData.setText(Tools.printBigDecimal(logic.getTotalRevenues()) + " €");
		totalExpensesData.setText(Tools.printBigDecimal(logic.getTotalExpenses()) + " €");
		totalSavingsData.setText(Tools.printBigDecimal(logic.getTotalSavings()) + " €");

		if (logic.getTotalSavings().compareTo(new BigDecimal(0)) < 0)
			totalSavingsData.setForeground(red);
		else if (logic.getTotalSavings().compareTo(new BigDecimal(0)) == 0)
			totalSavingsData.setForeground(black);
		else
			totalSavingsData.setForeground(green);
	}

	private void buildAccountsGroup(Group group) {
		group.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(group, SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		accountsComposite = new Composite(sc, SWT.NONE);
		sc.setContent(accountsComposite);

		GridLayout layout = new GridLayout(15, false);
		accountsComposite.setLayout(layout);

		sc.setMinSize(accountsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		group.setSize(sc.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void fillAccountsGroup(Vector<Kontostand> kost) {
		for (Control c : accountsComposite.getChildren()) {
			c.dispose();
		}

		Integer count = 0;
		for (Kontostand k : kost) {
			count++;
			Label accountLabel = new Label(accountsComposite, SWT.NONE);
			accountLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			accountLabel.setText(k.getKonto().getKuerzel());

			Label accountSum = new Label(accountsComposite, SWT.NONE);
			accountSum.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL));
			GUITools.setBoldFont(accountSum);
			accountSum.setText(Tools.printBigDecimal(k.getKtostsaldo()) + " €");
			if (k.getKtostsaldo().compareTo(new BigDecimal(0)) < 0)
				accountSum.setForeground(red);

			Label accountEndDate = new Label(accountsComposite, SWT.NONE);
			accountEndDate.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_END));
			GUITools.setSmallFont(accountEndDate);
			accountEndDate.setText(Tools.printDate(k.getKonto().getAbldat()));

			if (count % 4 != 0)
				new Label(accountsComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		accountsComposite.layout();
		accountsComposite.getParent().layout();
		accountsComposite.getParent().getParent().layout();
		accountsComposite.getParent().getParent().getParent().layout();
	}

	private void buildCosttypesGroup(Group group) {
		group.setLayout(new FillLayout());

		ScrolledComposite sc = new ScrolledComposite(group, SWT.V_SCROLL | SWT.H_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		costtypesComposite = new Composite(sc, SWT.NONE);
		sc.setContent(costtypesComposite);

		FormLayout layout = new FormLayout();
		costtypesComposite.setLayout(layout);
	}

	private void fillCosttypesGroup(SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> koartgrp) {
		for (Control c : costtypesComposite.getChildren()) {
			c.dispose();
		}

		Integer count = 0;

		for (Map.Entry<Kostenartgruppe, Vector<Kostenartsaldo>> e : koartgrp.entrySet()) {

			Composite comp = new CostTypeGroupsComposite(costtypesComposite, e.getKey(), e.getValue());

			if (count < 4) {
				FormData formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.left = new FormAttachment(25 * count, 0);
				formData.right = new FormAttachment(25 * (count + 1), 0);
				comp.setLayoutData(formData);
			} else if (count >= 4) {
				FormData formData = new FormData();
				Control neighbor = costtypesComposite.getChildren()[count - 4];
				formData.top = new FormAttachment(neighbor, 0);
				formData.left = new FormAttachment(neighbor, 0, SWT.LEFT);
				formData.right = new FormAttachment(neighbor, 0, SWT.RIGHT);
				comp.setLayoutData(formData);
			}
			count++;
		}
		ScrolledComposite sc = (ScrolledComposite) costtypesComposite.getParent();
		sc.setMinSize(costtypesComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		costtypesComposite.layout();
		sc.getParent().layout();
	}

	private void fillFields(LocalDate date) {
		if (date != null) {
			logic = new LogicMonatUE(date);
			monthText.setSelection(Tools.toDate(date));
			fillSummaryGroup();
			fillAccountsGroup(logic.getAccounts(date));
			fillCosttypesGroup(logic.getCosttypes());
			if (date.isBefore(Tools.getFirstDate())) {
				previousMonthButton.setEnabled(false);
			} else {
				previousMonthButton.setEnabled(true);
			}
			if (date.equals(Tools.getLastMonth())) {
				nextMonthButton.setEnabled(false);
			} else {
				nextMonthButton.setEnabled(true);
			}
		}
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
		if (se.getSource().equals(previousMonthButton)) {
			if (monthText.getSelection() != null) {
				fillFields(Tools.toLocalDate(monthText.getSelection()).minusMonths(1));
			}
		} else if (se.getSource().equals(nextMonthButton)) {
			if (monthText.getSelection() != null) {
				fillFields(Tools.toLocalDate(monthText.getSelection()).plusMonths(1));
			}
		} else if (se.getSource().equals(monthText)) {
			if (monthText.getSelection() != null) {
				fillFields(Tools.toLocalDate(monthText.getSelection()).withDayOfMonth(1));
			}
		}
	}

	@Override
	public void activate() {
		// nothing to do
	}

	@Override
	public void deactivate() {
		// nothing to do
	}

	@Override
	public void update() {
		fillFields(Tools.toLocalDate(monthText.getSelection()));
		setToBeUpdated(false);
	}

}