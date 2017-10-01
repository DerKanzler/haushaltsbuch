package haushaltsbuch.gui;

import haushaltsbuch.gui.dataframes.UIAbstractDataFrame;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * This is the main window of the application.
 * 
 * It displays today's date, contains the fastsearch field, the
 * control frame and the dataframe.
 * 
 * @author pk
 *
 */
public class UIMainFrame extends Composite implements SelectionListener, FocusListener {

	private Text searchText;
	
	private Button defaultButton;
	private UIHaushaltsbuch ui;
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private UIDataFrame dataFrame;
	private UIControlFrame controlFrame;
	
	/**
	 * Constructor: just reads the correct language bundle for the system's locale.
	 * 
	 * Builds the user interface. It uses the GridLayout with 3 columns.
	 * Today' date and the dataframes span over 2 columns, controlframe 
	 * and the fast search textfield only one.
	 * 
	 *  Builds the user interface. It uses the GridLayout with 3 columns.
	 * Today' date and the dataframes span over 2 columns, controlframe 
	 * and the fast search textfield only one.
	 */
	public UIMainFrame(Composite parent, Integer style, UIHaushaltsbuch ui) {
		super(parent, style);
		this.ui = ui;
		
		GridLayout layout;
		if (GUITools.isMac()) {
			layout = new GridLayout(3, false);
		}
		else layout = new GridLayout(4, false);
		this.setLayout(layout);

		Label dateText = new Label(this, SWT.LEFT);
		GridData dateTextGD = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END);
		dateTextGD.horizontalSpan = 2;
		dateText.setLayoutData(dateTextGD);
		dateText.setText(Tools.printTodayLong());
		
		if (!GUITools.isMac()) {
			Label searchLabel = new Label(this, SWT.LEFT);
			searchLabel.setText(res.getString("QuickSearch"));
		}
		
		searchText = new Text(this, SWT.LEFT | SWT.SEARCH | SWT.ICON_CANCEL | SWT.ICON_SEARCH);
		GridData searchTextGD = new GridData(GridData.HORIZONTAL_ALIGN_END);
		searchTextGD.widthHint = 200;
		searchText.setLayoutData(searchTextGD);
		searchText.addFocusListener(this);
		searchText.addSelectionListener(this);

		controlFrame = new UIControlFrame(this, SWT.LEFT | SWT.BORDER);
		GridData controlFrameGD = new GridData(GridData.BEGINNING | GridData.CENTER | GridData.FILL_VERTICAL);
		controlFrameGD.horizontalSpan = 1;
		controlFrame.setLayoutData(controlFrameGD);
		controlFrame.buildUI();
		
		dataFrame = new UIDataFrame(this, SWT.BORDER);
		GridData dataFrameGD = new GridData(GridData.BEGINNING | GridData.CENTER | GridData.FILL_BOTH);
		if (GUITools.isMac()) {
			dataFrameGD.horizontalSpan = 2;
		}
		else dataFrameGD.horizontalSpan = 3;
		dataFrame.setLayoutData(dataFrameGD);
	}
	
	/**
	 * Does nothing.
	 */
	public void widgetSelected(SelectionEvent se) {}
	
	/**
	 * Catches the event for the fastsearch textfield
	 */
	public void widgetDefaultSelected(SelectionEvent se) {
		// if the focus is on the fastsearch textfield and the 'Enter'/'Return' key
		// was pressed, dataframe suchen is acvtivated.
		if (se.getSource().equals(searchText) && se.detail != SWT.CANCEL) {
			if (ui.checkState()) {
				defaultButton = null;
				StackLayout sl = (StackLayout)getDataFrame().getLayout();
				UIAbstractDataFrame adf = (UIAbstractDataFrame)sl.topControl;
				if (adf != null) adf.deactivate();
				
				String s = searchText.getText();
				// If the searchstring doesn't contain a wildcard it is appended here.
				if (!s.contains("%") && !s.contains("*")) {
					s = new String("*").concat(s).concat("*");
				}
				// Selects the search dataframe.
				this.getControlFrame().selectSuchen();
				this.getDataFrame().activateSuchen(s);
				// Blanks out the fastsearch text.
				searchText.setText("");
			}
		}
	}

	/**
	 * Catches the FocusEvents when a widget gets the focus
	 */
	public void focusGained(FocusEvent fe) {
		// If the fastsearch textfield gains the focus
		if (fe.getSource().equals(searchText)) {
			// If the default button is set, it is stored in defaultButton
			// and then it is set to null, so that the key event triggered
			// in the fastsearch textfiled can reach the keyReleased() method.
			if (this.getShell().getDefaultButton() != null) {
				defaultButton = this.getShell().getDefaultButton();
				this.getShell().setDefaultButton(null);
			}
		}	
	}

	/**
	 * Catches the FocusEvents when a widget loses the focus
	 */
	public void focusLost(FocusEvent fe) {
		// Sets the default button to the button stored in defaultButton;
		if (defaultButton != null && !defaultButton.isDisposed()) {
			this.getShell().setDefaultButton(defaultButton);
		}
	}
	
	/**
	 * Returns the dataframe.
	 * 
	 * @return UIDataFrame
	 */
	public UIDataFrame getDataFrame() {
		return this.dataFrame;
	}
	
	/**
	 * Returns the controlframe.
	 * 
	 * @return UIControlFrame
	 */
	public UIControlFrame getControlFrame() {
		return this.controlFrame;
	}
	
}