package haushaltsbuch.gui.dataframes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Vector;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dialogs.UIBuchungAendern;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.LogicSuchen;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.AccountsCombo;
import haushaltsbuch.widgets.BookingsTable;
import haushaltsbuch.widgets.CostTypesCombo;

/**
 * This class displays the search screen.
 * 
 * You can search for bookings with the bookingtext, the amount or other values.
 * 
 * @author pk
 *
 */
public class UISuchen extends UIAbstractDataFrame
		implements SelectionListener, IDoubleClickListener, ISelectionChangedListener {

	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
			LogicMain.instance().getUser().getFormat());

	private Text bookText;
	private FormattedText amountText;
	private CDateTime fromDateText, toDateText;
	private AccountsCombo fromAccountCombo, toAccountCombo;
	private CostTypesCombo koartCombo;
	private Combo amountCombo;
	private Button clearButton, searchButton, editButton;
	private Label sumRevenueData, sumExpenseData, sumTotalData, messages;
	private Group resultGroup;
	private BookingsTable resultTable;

	private LogicSuchen logic = LogicSuchen.instance();

	private SearchBuchung suchBuchung;

	/**
	 * Constructor: Since UISuchen extends Composite the super constructor is called
	 * here with the given arguments. Builds the visible user interface. Uses
	 * GridLayout with 3 columns.
	 * 
	 * @param Composite
	 * @param Integer
	 */
	public UISuchen(UIDataFrame parent, Integer style) {
		super(parent, style);

		GridLayout layout = new GridLayout(3, false);
		this.setLayout(layout);

		// The searchGroup, where you can enter your search values, is declared
		Group searchGroup = new Group(this, SWT.LEFT);
		GridData searchGroupGD = new GridData(GridData.FILL_HORIZONTAL);
		searchGroupGD.horizontalSpan = 3;
		searchGroup.setLayoutData(searchGroupGD);
		searchGroup.setText(res.getString("SearchTerms"));
		// Calls the method to fill this group with the appropiate widgets.
		buildSearchGroup(searchGroup);

		// A Label for some (error)messages to the user
		messages = new Label(this, SWT.LEFT);
		messages.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		clearButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(clearButton, 1);
		clearButton.setText(res.getString("Clear"));
		clearButton.addSelectionListener(this);

		searchButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(searchButton, 1);
		searchButton.setText(res.getString("Search"));
		searchButton.addSelectionListener(this);

		// The resultGroup, where the result of your search is being displayed, is
		// declared here.
		resultGroup = new Group(this, SWT.LEFT);
		GridData resultGroupGD = new GridData(GridData.FILL_BOTH);
		resultGroupGD.horizontalSpan = 3;
		resultGroup.setLayoutData(resultGroupGD);
		resultGroup.setText(res.getString("SearchResults"));
		// Calls the method to fill this group with the appropriate widgets.
		buildResultGroup(resultGroup);

		editButton = new Button(this, SWT.PUSH | SWT.CENTER);
		GUITools.setButtonLayout(editButton, 3);
		editButton.setText(res.getString("Edit"));
		editButton.addSelectionListener(this);
		editButton.setEnabled(false);

		fillFields();
	}

	/**
	 * Builds the appropriate widgets for the search group. A GridLayout is being
	 * used for this group having 11 columns.
	 * 
	 * @param Group
	 */
	private void buildSearchGroup(Group group) {
		GridLayout layout = new GridLayout(11, false);
		group.setLayout(layout);

		GridData labelGD = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		GridData fieldGD = new GridData(GridData.FILL_HORIZONTAL);
		fieldGD.verticalAlignment = GridData.CENTER;
		fieldGD.horizontalSpan = 2;
		GridData fillerGD = new GridData(GridData.FILL_HORIZONTAL);
		fillerGD.verticalAlignment = GridData.CENTER;

		Label bookLabel = new Label(group, SWT.LEFT);
		bookLabel.setLayoutData(labelGD);
		bookLabel.setText(res.getString("BookingText"));

		bookText = new Text(group, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
		GridData bookTextGD = new GridData(GridData.FILL_HORIZONTAL);
		bookTextGD.horizontalSpan = 10;
		bookText.setLayoutData(bookTextGD);

		Label koartLabel = new Label(group, SWT.LEFT);
		koartLabel.setLayoutData(labelGD);
		koartLabel.setText(res.getString("Costtype"));

		koartCombo = new CostTypesCombo(group, SWT.LEFT | SWT.READ_ONLY | SWT.BORDER);
		koartCombo.getCombo().setLayoutData(fieldGD);

		new Label(group, SWT.LEFT).setLayoutData(fillerGD);

		Label fromAccountLabel = new Label(group, SWT.LEFT);
		fromAccountLabel.setLayoutData(labelGD);
		fromAccountLabel.setText(res.getString("FromAccount"));

		fromAccountCombo = new AccountsCombo(group, SWT.LEFT | SWT.READ_ONLY | SWT.BORDER);
		fromAccountCombo.getCombo().setLayoutData(fieldGD);

		new Label(group, SWT.LEFT).setLayoutData(fillerGD);

		Label toAccountLabel = new Label(group, SWT.LEFT);
		toAccountLabel.setLayoutData(labelGD);
		toAccountLabel.setText(res.getString("ToAccount"));

		toAccountCombo = new AccountsCombo(group, SWT.LEFT | SWT.READ_ONLY | SWT.BORDER);
		toAccountCombo.getCombo().setLayoutData(fieldGD);

		Label fromDateLabel = new Label(group, SWT.LEFT);
		fromDateLabel.setLayoutData(labelGD);
		fromDateLabel.setText(res.getString("FromDate"));

		fromDateText = new CDateTime(group, CDT.BORDER | CDT.DROP_DOWN | CDT.BUTTON_RIGHT | CDT.COMPACT);
		fromDateText.setLocale(LogicMain.instance().getUser().getFormat());
		fromDateText.setFormat(CDT.DATE_MEDIUM);
		fromDateText.setLayoutData(fieldGD);

		new Label(group, SWT.LEFT).setLayoutData(fillerGD);

		Label toDateLabel = new Label(group, SWT.LEFT);
		toDateLabel.setLayoutData(labelGD);
		toDateLabel.setText(res.getString("ToDate"));

		toDateText = new CDateTime(group, CDT.BORDER | CDT.DROP_DOWN | CDT.BUTTON_RIGHT | CDT.COMPACT);
		toDateText.setLocale(LogicMain.instance().getUser().getFormat());
		toDateText.setFormat(CDT.DATE_MEDIUM);
		toDateText.setLayoutData(fieldGD);

		new Label(group, SWT.LEFT).setLayoutData(fillerGD);

		Label amountLabel = new Label(group, SWT.LEFT);
		amountLabel.setLayoutData(labelGD);
		amountLabel.setText(res.getString("Amount"));

		amountCombo = new Combo(group, SWT.LEFT | SWT.READ_ONLY | SWT.BORDER);
		// This operators are available for the search after the amount of the booking.
		String[] operators = { "<", "<=", "=", ">=", ">" };
		amountCombo.setItems(operators);
		// The default operator is ">=".
		amountCombo.select(3);

		amountText = new FormattedText(group, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		amountText.getControl().setLayoutData(fillerGD);
		NumberFormatter formatter = new NumberFormatter("#,###,###.00", LogicMain.instance().getUser().getFormat());
		formatter.setFixedLengths(true, true);
		formatter.setDecimalSeparatorAlwaysShown(true);
		amountText.setFormatter(formatter);
		amountText.setValue(0);
	}

	/**
	 * Builds the appropriate widgets for the result group. A GridLayout is being
	 * used for this group having 7 columns.
	 * 
	 * @param Group
	 */
	private void buildResultGroup(Group group) {
		GridLayout layout = new GridLayout(7, false);
		group.setLayout(layout);

		GridData dataGD = new GridData(GridData.FILL_HORIZONTAL);

		new Label(group, SWT.LEFT);

		Label sumRevenueText = new Label(group, SWT.LEFT);
		sumRevenueText.setText(res.getString("Sum") + " " + res.getString("TotalRevenues") + ":");
		sumRevenueData = new Label(group, SWT.LEFT);
		sumRevenueData.setLayoutData(dataGD);
		sumRevenueData.setText("0,00 €");

		Label sumExpenseText = new Label(group, SWT.LEFT);
		sumExpenseText.setText(res.getString("Sum") + " " + res.getString("TotalExpenses") + ":");
		sumExpenseData = new Label(group, SWT.LEFT);
		sumExpenseData.setLayoutData(dataGD);
		sumExpenseData.setText("0,00 €");

		Label sumTotalText = new Label(group, SWT.LEFT);
		sumTotalText.setText(res.getString("Sum") + " " + res.getString("TotalSavings") + ":");
		sumTotalData = new Label(group, SWT.LEFT);
		sumTotalData.setText("0,00 €");
		sumTotalData.setLayoutData(dataGD);

		resultTable = new BookingsTable(group, 7);
		resultTable.getTableViewer().addSelectionChangedListener(this);
		resultTable.getTableViewer().addDoubleClickListener(this);
	}

	private void fillFields() {
		koartCombo.update(logic.getKoartList());
		fromAccountCombo.update(logic.getKontenList());
		toAccountCombo.update(logic.getKontenList());
		fromDateText.setSelection(Tools.toDate(Tools.getFirstDate()));
		toDateText.setSelection(Tools.toDate(Tools.getLastDate()));
	}

	/**
	 * Reads all the textfields and combos and enters the value into a "SuchBuchung"
	 * object. If there are no data inputs a empty SuchBuchung is returned.
	 * 
	 * Throws an exception if the data in a field isn't appropriate.
	 *
	 * @return SuchBuchung
	 * @throws Exception
	 */
	private SearchBuchung readFields() throws RuntimeException {
		suchBuchung = new SearchBuchung();
		if (!bookText.getText().equals("")) {
			// Replaces the common wildcard character * with the sql one %
			String s = bookText.getText().replaceAll("\\*", "%");
			suchBuchung.setBookText(s);
		}
		if (koartCombo.getCombo().getSelectionIndex() > 0) {
			suchBuchung.setKoart(koartCombo.getSelectedCostType());
		}
		if (fromAccountCombo.getCombo().getSelectionIndex() > 0) {
			suchBuchung.setFromAccount(fromAccountCombo.getSelectedAccount());
		}
		if (toAccountCombo.getCombo().getSelectionIndex() > 0) {
			suchBuchung.setToAccount(toAccountCombo.getSelectedAccount());
		}
		if ((fromAccountCombo.getCombo().getSelectionIndex() > 0)
				&& (toAccountCombo.getCombo().getSelectionIndex() > 0)) {
			suchBuchung.setSameAccount(
					fromAccountCombo.getSelectedAccount().getKonto() == toAccountCombo.getSelectedAccount().getKonto());
		}

		LocalDate fromDate = null;
		if (fromDateText.getSelection() != null) {
			fromDate = Tools.toLocalDate(fromDateText.getSelection());
			suchBuchung.setFromDate(fromDate);
		}

		LocalDate toDate = null;
		if (toDateText.getSelection() != null) {
			toDate = Tools.toLocalDate(toDateText.getSelection());
			suchBuchung.setToDate(toDate);
		}

		if (fromDate != null && toDate != null) {
			if (fromDate.isAfter(toDate)) {
				throw new RuntimeException("Von-Datum ist jünger als Bis-Datum!");
			}
		}

		suchBuchung.setOperator(amountCombo.getText());
		if (amountText.getValue() instanceof Integer) {
			if ((Integer) amountText.getValue() > 0) {
				suchBuchung.setAmount(new BigDecimal((Integer) amountText.getValue()));
			}
		} else if (amountText.getValue() instanceof Long) {
			if ((Long) amountText.getValue() > 0) {
				suchBuchung.setAmount(new BigDecimal((Long) amountText.getValue()));
			}
		} else if (amountText.getValue() instanceof Double) {
			if ((Double) amountText.getValue() > 0) {
				suchBuchung.setAmount(new BigDecimal((Double) amountText.getValue()));
			}
		}

		return suchBuchung;
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
		// If the search button was clicked it calls the search method
		if (se.getSource().equals(searchButton)) {
			search();
		}
		// If the clear button was clicked... and so on
		else if (se.getSource().equals(clearButton)) {
			clear();
			// The focus goes back to the booking text field
			bookText.setFocus();
		} else if (se.getSource().equals(editButton)) {
			edit();
		}
	}

	/**
	 * Catches the double clicks on a row of the table and calls the private edit
	 * method.
	 */
	public void doubleClick(DoubleClickEvent arg0) {
		edit();
	}

	/**
	 * Catches the event when the selected row of the table is changed.
	 * Enables/diables the edit button then.
	 */
	public void selectionChanged(SelectionChangedEvent arg0) {
		if (resultTable.getSelectedBooking() != null) {
			editButton.setEnabled(true);
		} else
			editButton.setEnabled(false);
	}

	/**
	 * Refreshes the search if one was executed already in case a booking was
	 * changed somewhere else.
	 */
	@Override
	public void update() {
		if (this.suchBuchung != null) {
			search(suchBuchung);
		}
		koartCombo.update(logic.getKoartList());
		fromAccountCombo.update(logic.getKontenList());
		toAccountCombo.update(logic.getKontenList());
		setToBeUpdated(false);
	}

	/**
	 * Makes this dataframe active. sets the focus to the booking text field and
	 * sets the application's default button to the search button.
	 */
	@Override
	public void activate() {
		bookText.setFocus();
		this.getShell().setDefaultButton(searchButton);
	}

	/**
	 * Called from the fastsearch. Clears the old search entries, enters the
	 * searchtext into the booking text field and searches.
	 * 
	 * @param String
	 */
	public void fastSearch(String s) {
		clear();
		bookText.setText(s);
		search();
	}

	/**
	 * Searches for the bookings, read the fields first.
	 */
	private void search() {
		try {
			search(readFields());
			resultTable.clearSelection();
		} catch (Exception e) {
			messages.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
			messages.setText(e.getMessage());
		}
	}

	/**
	 * Searches for the bookings, doesn't read the fields first. Uses the given
	 * SuchBuchung object.
	 * 
	 * @param SearchBuchung
	 */
	private void search(SearchBuchung sb) {
		try {
			Vector<Buchung> data = logic.search(sb);
			if (data.size() < 1) {
				messages.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_BLACK));
				messages.setText("Es konnten keine Buchungen gefunden werden.");
			} else
				messages.setText("");
			resultTable.update(data);

			sumRevenueData.setText(Tools.printBigDecimal(logic.getRevenueSum()) + " €");
			sumExpenseData.setText(Tools.printBigDecimal(logic.getExpenseSum()) + " €");
			sumTotalData.setText(Tools.printBigDecimal(logic.getTotalSum()) + " €");
		} catch (Exception e) {
			messages.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
			messages.setText(e.getMessage());
		} finally {
			resultTable.layout();
		}
	}

	/**
	 * Clears all the searchfields.
	 */
	private void clear() {
		bookText.setText("");
		koartCombo.getCombo().select(0);
		fromAccountCombo.getCombo().select(0);
		toAccountCombo.getCombo().select(0);
		fromDateText.setSelection(Tools.toDate(Tools.getFirstDate()));
		toDateText.setSelection(Tools.toDate(Tools.getLastDate()));
		amountCombo.select(3);
		amountText.setValue(0);
		messages.setText("");
	}

	/**
	 * Opens the dialog to edit the booking text of the selected booking. Updates
	 * the resultgroup then afterwards.
	 */
	private void edit() {
		Buchung b = resultTable.getSelectedBooking();
		UIBuchungAendern editBooking = new UIBuchungAendern(this.getShell());
		if (editBooking.open(b) == IDialogConstants.OK_ID) {
			search(suchBuchung);
			((UIDataFrame) this.getParent()).setUpdates(true);
			setToBeUpdated(false);
		}
	}

	@Override
	public void deactivate() {
		// nothing to do
	}

}