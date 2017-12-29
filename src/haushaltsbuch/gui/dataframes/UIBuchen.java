package haushaltsbuch.gui.dataframes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import haushaltsbuch.bean.Benutzer;
import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SaveBuchung;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dialogs.UIBuchungAendern;
import haushaltsbuch.logic.LogicBuchen;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.AccountsCombo;
import haushaltsbuch.widgets.BookingsSorter;
import haushaltsbuch.widgets.BookingsTable;
import haushaltsbuch.widgets.CostTypesCombo;

public class UIBuchen extends UIAbstractDataFrame
		implements SelectionListener, IDoubleClickListener, ISelectionChangedListener {

	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
			LogicMain.instance().getUser().getFormat());
	private LogicBuchen logic = LogicBuchen.instance();

	private CDateTime dateText;
	private Text bookingText;
	private FormattedText amountText;
	private CostTypesCombo koartCombo;
	private AccountsCombo fromAccountCombo, toAccountCombo;
	private Label bookMonthLabel, accountOneLabel, accountTwoLabel, accountThreeLabel, accountOneData, accountTwoData,
			accountThreeData, disposableAmountData, offlimitAmountData, totalAssetsData, totalRevenuesData,
			totalExpensesData, totalSavingsData, messageLabel;
	private Button abortButton, saveButton, deleteButton, editButton;
	private Link sortLink;
	private BookingsTable bookingsTable;

	private Control focusedControl;

	private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
	private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

	public UIBuchen(UIDataFrame parent, Integer style) {
		super(parent, style);

		GridLayout layout = new GridLayout(3, false);
		this.setLayout(layout);

		bookMonthLabel = new Label(this, SWT.RIGHT);
		GridData bookMonthGD = new GridData(GridData.FILL_HORIZONTAL);
		bookMonthGD.horizontalSpan = 3;
		bookMonthLabel.setLayoutData(bookMonthGD);

		Group summaryGroup = new Group(this, SWT.LEFT);
		GridData summaryGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		summaryGroupGD.horizontalSpan = 3;
		summaryGroup.setLayoutData(summaryGroupGD);
		summaryGroup.setText(res.getString("Summary"));
		buildSummaryGroup(summaryGroup);

		Group bookingGroup = new Group(this, SWT.LEFT);
		GridData bookingGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		bookingGroupGD.horizontalSpan = 3;
		bookingGroup.setLayoutData(bookingGroupGD);
		bookingGroup.setText(res.getString("Booking"));
		buildBookingGroup(bookingGroup);

		messageLabel = new Label(this, SWT.LEFT);
		messageLabel.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
		messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));

		abortButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(abortButton, 1);
		abortButton.setText(res.getString("Abort"));
		abortButton.addSelectionListener(this);

		saveButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(saveButton, 1);
		saveButton.setText(res.getString("Save"));
		saveButton.addSelectionListener(this);

		bookingsTable = new BookingsTable(this, 3);
		bookingsTable.getTableViewer().addSelectionChangedListener(this);
		bookingsTable.getTableViewer().addDoubleClickListener(this);

		sortLink = new Link(this, SWT.NONE);
		sortLink.setText(res.getString("SortInputDate"));
		sortLink.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
		sortLink.addSelectionListener(this);

		editButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(editButton, 1);
		editButton.setText(res.getString("Edit"));
		editButton.addSelectionListener(this);
		editButton.setEnabled(false);

		deleteButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(deleteButton, 1);
		deleteButton.setText(res.getString("Delete"));
		deleteButton.addSelectionListener(this);
		deleteButton.setEnabled(false);

		dateText.setFocus();

		fillFields();

		this.layout();
	}

	private void buildSummaryGroup(Group group) {
		GridLayout layout = new GridLayout(8, false);
		group.setLayout(layout);

		accountOneLabel = new Label(group, SWT.LEFT);
		accountOneData = new Label(group, SWT.RIGHT);
		accountOneData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(accountOneData);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		accountTwoLabel = new Label(group, SWT.LEFT);
		accountTwoData = new Label(group, SWT.RIGHT);
		accountTwoData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(accountTwoData);

		new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		accountThreeLabel = new Label(group, SWT.LEFT);
		accountThreeData = new Label(group, SWT.RIGHT);
		accountThreeData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GUITools.setBoldFont(accountThreeData);

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

	private void buildBookingGroup(Group group) {
		GridLayout layout = new GridLayout(6, false);
		group.setLayout(layout);

		Label dateLabel = new Label(group, SWT.LEFT);
		dateLabel.setText(res.getString("Date"));

		Label bookingLabel = new Label(group, SWT.LEFT);
		bookingLabel.setText(res.getString("BookingText"));

		Label amountLabel = new Label(group, SWT.RIGHT);
		amountLabel.setText(res.getString("Amount"));

		Label koartLabel = new Label(group, SWT.RIGHT);
		koartLabel.setText(res.getString("Costtype"));

		Label fromAccountLabel = new Label(group, SWT.RIGHT);
		fromAccountLabel.setText(res.getString("FromAccount"));

		Label toAccountLabel = new Label(group, SWT.RIGHT);
		toAccountLabel.setText(res.getString("ToAccount"));

		dateText = new CDateTime(group, CDT.BORDER | CDT.DROP_DOWN | CDT.BUTTON_RIGHT | CDT.COMPACT);
		dateText.setLocale(LogicMain.instance().getUser().getFormat());
		dateText.setFormat(CDT.DATE_MEDIUM);
		dateText.setSelection(Tools.toDate(Tools.getLastDate()));

		bookingText = new Text(group, SWT.LEFT | SWT.BORDER);
		bookingText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bookingText.setTextLimit(52);

		amountText = new FormattedText(group, SWT.RIGHT | SWT.BORDER);
		amountText.getControl().setLayoutData(new GridData(95, SWT.DEFAULT));
		NumberFormatter formatter = new NumberFormatter("#,###,###.00", LogicMain.instance().getUser().getFormat());
		formatter.setFixedLengths(true, true);
		formatter.setDecimalSeparatorAlwaysShown(true);
		amountText.setFormatter(formatter);
		amountText.setValue(0);

		koartCombo = new CostTypesCombo(group, SWT.READ_ONLY | SWT.RIGHT | SWT.BORDER);
		koartCombo.update(logic.getKoartList());

		fromAccountCombo = new AccountsCombo(group, SWT.READ_ONLY | SWT.RIGHT | SWT.BORDER);
		fromAccountCombo.update(KontoDAO.instance().getAllValid());

		toAccountCombo = new AccountsCombo(group, SWT.READ_ONLY | SWT.RIGHT | SWT.BORDER);
		toAccountCombo.update(KontoDAO.instance().getAllValid());
	}

	@Override
	public void activate() {
		this.getShell().setDefaultButton(saveButton);
		if (focusedControl == null)
			dateText.setFocus();
		else
			focusedControl.setFocus();
	}

	@Override
	public void update() {
		bookingsTable.update(logic.getMonthBookings());
		koartCombo.update(logic.getKoartList());
		fromAccountCombo.update(KontoDAO.instance().getAllValid());
		toAccountCombo.update(KontoDAO.instance().getAllValid());
		setToBeUpdated(false);
	}

	public void widgetDefaultSelected(SelectionEvent se) {
	}

	public void widgetSelected(SelectionEvent se) {
		if (se.getSource().equals(abortButton)) {
			dateText.setSelection(Tools.toDate(Tools.getLastDate()));
			clear();
		} else if (se.getSource().equals(saveButton)) {
			save();
			bookingsTable.layout();
		} else if (se.getSource().equals(sortLink)) {
			bookingsTable.setSorting(BookingsSorter.EINDAT_DOWN);
		} else if (se.getSource().equals(editButton)) {
			edit();
		} else if (se.getSource().equals(deleteButton)) {
			delete();
			bookingsTable.layout();
		}
	}

	private void fillFields() {
		bookMonthLabel.setText(res.getString("BookMonth") + ": " + Tools.printMonthDate(Tools.getLastMonth()));
		Benutzer user = LogicMain.instance().getUser();
		if (user.getKonto1() != null) {
			accountOneLabel.setVisible(true);
			accountOneData.setVisible(true);
			accountOneLabel.setText(user.getKonto1().getKuerzel());
			accountOneData.setText(Tools.printBigDecimal(user.getKonto1().getSaldo()) + " €");
			if (user.getKonto1().getSaldo().compareTo(new BigDecimal(0)) < 0)
				accountOneData.setForeground(red);
			else
				accountOneData.setForeground(black);
		} else {
			accountOneLabel.setVisible(false);
			accountOneData.setVisible(false);
		}
		if (user.getKonto2() != null) {
			accountTwoLabel.setVisible(true);
			accountTwoData.setVisible(true);
			accountTwoLabel.setText(user.getKonto2().getKuerzel());
			accountTwoData.setText(Tools.printBigDecimal(user.getKonto2().getSaldo()) + " €");
			if (user.getKonto2().getSaldo().compareTo(new BigDecimal(0)) < 0)
				accountTwoData.setForeground(red);
			else
				accountTwoData.setForeground(black);
		} else {
			accountTwoLabel.setVisible(false);
			accountTwoData.setVisible(false);
		}
		if (user.getKonto3() != null) {
			accountThreeLabel.setVisible(true);
			accountThreeData.setVisible(true);
			accountThreeLabel.setText(user.getKonto3().getKuerzel());
			accountThreeData.setText(Tools.printBigDecimal(user.getKonto3().getSaldo()) + " €");
			if (user.getKonto3().getSaldo().compareTo(new BigDecimal(0)) < 0)
				accountThreeData.setForeground(red);
			else
				accountThreeData.setForeground(black);
		} else {
			accountThreeLabel.setVisible(false);
			accountThreeData.setVisible(false);
		}
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

		if (logic.getTotalSavings().compareTo(new BigDecimal(0)) < 0) {
			totalSavingsData.setForeground(red);
		} else if (logic.getTotalSavings().compareTo(new BigDecimal(0)) == 0) {
			totalSavingsData.setForeground(black);
		} else {
			totalSavingsData.setForeground(green);
		}
		bookingsTable.update(logic.getMonthBookings());
	}

	private Buchung readFields() throws RuntimeException {
		Buchung b = new Buchung();
		if (dateText.getSelection() != null) {
			b.setBuchdat(Tools.toLocalDate(dateText.getSelection()));
		} else
			throw new RuntimeException("Das Datum darf nicht leer sein!");
		if (!bookingText.getText().equals("")) {
			b.setBuchtext(bookingText.getText());
		} else
			throw new RuntimeException("Der Buchungstext darf nicht leer sein!");
		if (amountText.getValue() instanceof Integer) {
			if ((Integer) amountText.getValue() > 0) {
				b.setBuchbetr(new BigDecimal((Integer) amountText.getValue()));
			} else
				throw new RuntimeException("Der Betrag muss größer als 0 sein!");
		} else if (amountText.getValue() instanceof Long) {
			if ((Long) amountText.getValue() > 0) {
				b.setBuchbetr(new BigDecimal((Long) amountText.getValue()));
			} else
				throw new RuntimeException("Der Betrag muss größer als 0 sein!");
		} else if (amountText.getValue() instanceof Double) {
			if ((Double) amountText.getValue() > 0) {
				b.setBuchbetr(new BigDecimal((Double) amountText.getValue()));
			} else
				throw new RuntimeException("Der Betrag muss größer als 0 sein!");
		}
		b.setKoart(koartCombo.getSelectedCostType());
		b.setKontovon(fromAccountCombo.getSelectedAccount());
		b.setKontonach(toAccountCombo.getSelectedAccount());
		return b;
	}

	private void clear() {
		bookingText.setText("");
		amountText.setValue(0);
		amountText.getControl().setSelection(0);
		koartCombo.getCombo().select(0);
		fromAccountCombo.getCombo().select(0);
		toAccountCombo.getCombo().select(0);
		messageLabel.setText("");
		dateText.setFocus();
	}

	private void save() {
		try {
			SaveBuchung sb = logic.save(readFields());
			if (sb.getSuccess()) {
				clear();
				fillFields();
				if (sb.getYearEnds().size() > 0) {
					MessageBox mb = new MessageBox(this.getShell(), SWT.OK | SWT.ICON_INFORMATION | SWT.SHEET);
					mb.setMessage(printYearEnds(sb.getYearEnds()));
					mb.open();
				}
				if (sb.getMonthEnds().size() > 0) {
					MessageBox mb = new MessageBox(this.getShell(), SWT.OK | SWT.ICON_INFORMATION | SWT.SHEET);
					mb.setMessage(printMonthEnds(sb.getMonthEnds()));
					mb.open();
				}
				((UIDataFrame) this.getParent()).setUpdates(true);
				setToBeUpdated(false);
			} else
				messageLabel.setText("Das Speichern ist fehlgeschlagen!");
		} catch (RuntimeException re) {
			messageLabel.setText(re.getMessage());
		}
	}

	/**
	 * Opens the dialog to edit the booking text of the selected booking. Updates
	 * the bookingsTable then afterwards.
	 */
	private void edit() {
		Buchung b = bookingsTable.getSelectedBooking();
		UIBuchungAendern editBooking = new UIBuchungAendern(this.getShell());
		if (editBooking.open(b) == IDialogConstants.OK_ID) {
			bookingsTable.update(logic.getMonthBookings());
			((UIDataFrame) this.getParent()).setUpdates(true);
			setToBeUpdated(false);
		}
	}

	private void delete() {
		try {
			if (logic.delete(bookingsTable.getSelectedBooking())) {
				update();
				fillFields();
				messageLabel.setText("");
				bookingsTable.clearSelection();
				bookingsTable.getTable().setFocus();
				((UIDataFrame) this.getParent()).setUpdates(true);
				setToBeUpdated(false);
			} else
				messageLabel.setText("Das Löschen ist fehlgeschlagen!");
		} catch (RuntimeException re) {
			messageLabel.setText(re.getMessage());
		}
	}

	/**
	 * Catches the double clicks on a row of the table and calls the private edit
	 * method.
	 */
	public void doubleClick(DoubleClickEvent dce) {
		edit();
	}

	/**
	 * Catches the event when the selected row of the table is changed.
	 * Enables/diables the edit button then.
	 */
	public void selectionChanged(SelectionChangedEvent sce) {
		if (bookingsTable.getSelectedBooking() != null) {
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

	private String printYearEnds(List<LocalDate> dates) {
		StringBuilder sb = new StringBuilder();
		if (dates.size() > 1) {
			sb.append("Der Jahresabschluss für folgende Jahre ");
		} else {
			sb.append("Der Jahresabschluss für das folgende Jahr ");
		}
		sb.append("wurde erfolgreich durchgeführt:\n\n");
		for (LocalDate date : dates) {
			sb.append(Tools.printYearDate(date) + "\n");
		}
		return sb.toString();
	}

	private String printMonthEnds(List<LocalDate> dates) {
		StringBuilder sb = new StringBuilder();
		if (dates.size() > 1) {
			sb.append("Der Monatsabschluss für folgende Monate ");
		} else {
			sb.append("Der Monatsabschluss für das folgende Monat ");
		}
		sb.append("wurde erfolgreich durchgeführt:\n\n");
		for (LocalDate date : dates) {
			sb.append(Tools.printMonthDate(date) + "\n");
		}
		return sb.toString();
	}

	@Override
	public void deactivate() {
		focusedControl = this.getShell().getDisplay().getFocusControl();
	}

}