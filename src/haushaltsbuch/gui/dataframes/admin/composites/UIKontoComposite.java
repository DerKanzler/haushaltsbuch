package haushaltsbuch.gui.dataframes.admin.composites;

import haushaltsbuch.bean.Konto;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dataframes.admin.UIKontoAdmin;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.admin.composites.LogicKontoComposite;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ResourceBundle;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UIKontoComposite extends UIAbstractComposite implements SelectionListener {
	
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private UIKontoAdmin uiKonto;
	private Konto konto;
	private LogicKontoComposite logic = new LogicKontoComposite();
	
	private StackLayout layout;
	
	private Label ktobezTitle, kuerzelTitle, abldatTitle, ktotypTitle, kzogTitle, saldoTitle, jreinsaldoTitle, messageLabel;
	private Text ktobezText, kuerzelText;
	private CDateTime abldatText;
	private Combo ktotypCombo, kzogCombo;
	private Button editButton, abortButton, saveButton;
	
	private Boolean inUse = false;
	private Boolean isNew = false;
	
	public UIKontoComposite(Composite parent, int style) {
		super(parent, style);
		this.uiKonto = (UIKontoAdmin)this.getParent().getParent().getParent();
		
		layout = new StackLayout();
		this.setLayout(layout);
	}
	
	public void buildUI(Konto konto) {		
		if (konto != null) {
			buildViewComposite();
			
			this.konto = konto;
			fillFields();
			layout.topControl = viewComposite;
		}
		else {
			setNew(true);
			edit();
		}
	}
	
	private void buildViewComposite() {
		viewComposite = new Composite(this, SWT.NONE);
		FormLayout layout = new FormLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		layout.spacing = 5;
		viewComposite.setLayout(layout);
		
		ktobezTitle = new Label(viewComposite, SWT.LEFT);
		GUITools.setBoldFont(ktobezTitle);
		FormData ktobezTitleFD = new FormData();
		ktobezTitleFD.top = new FormAttachment(0, 0);
		ktobezTitleFD.left = new FormAttachment(0, 0);
		ktobezTitle.setLayoutData(ktobezTitleFD);
		
		editButton = new Button(viewComposite, SWT.FLAT);
		editButton.setImage(new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/edit.png")));
		FormData editButtonFD = new FormData();
		editButtonFD.top = new FormAttachment(0, 0);
		editButtonFD.right = new FormAttachment(100, 0);
		editButton.setLayoutData(editButtonFD);		
		editButton.addSelectionListener(this);

		Label kuerzelLabel = new Label(viewComposite, SWT.NONE);
		FormData kuerzelLabelFD = new FormData();
		kuerzelLabelFD.top = new FormAttachment(editButton, 0);
		kuerzelLabelFD.left = new FormAttachment(ktobezTitle, 0, SWT.LEFT);
		kuerzelLabel.setLayoutData(kuerzelLabelFD);
		kuerzelLabel.setText(res.getString("ShortTitle"));	
		
		kuerzelTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(kuerzelTitle);
		FormData kuerzelTitleFD = new FormData();
		kuerzelTitleFD.top = new FormAttachment(editButton, 0);
		kuerzelTitleFD.right = new FormAttachment(26, 0);
		kuerzelTitle.setLayoutData(kuerzelTitleFD);
		
		Label ktotypLabel = new Label(viewComposite, SWT.NONE);
		FormData ktotypLabelFD = new FormData();
		ktotypLabelFD.top = new FormAttachment(editButton, 0);
		ktotypLabelFD.left = new FormAttachment(37, 0);
		ktotypLabel.setLayoutData(ktotypLabelFD);
		ktotypLabel.setText(res.getString("Type"));		
		
		ktotypTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(ktotypTitle);
		FormData ktotypTitleFD = new FormData();
		ktotypTitleFD.top = new FormAttachment(editButton, 0);
		ktotypTitleFD.right = new FormAttachment(63, 0);
		ktotypTitle.setLayoutData(ktotypTitleFD);
		
		Label kzogLabel = new Label(viewComposite, SWT.NONE);
		FormData kzogLabelFD = new FormData();
		kzogLabelFD.top = new FormAttachment(editButton, 0);
		kzogLabelFD.left = new FormAttachment(74, 0);
		kzogLabel.setLayoutData(kzogLabelFD);
		kzogLabel.setText(res.getString("Status"));
		
		kzogTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(kzogTitle);
		FormData kzogTitleFD = new FormData();
		kzogTitleFD.top = new FormAttachment(editButton, 0);
		kzogTitleFD.right = new FormAttachment(100, 0);
		kzogTitle.setLayoutData(kzogTitleFD);

		Label abldatLabel = new Label(viewComposite, SWT.NONE);
		FormData abldatLabelFD = new FormData();
		abldatLabelFD.top = new FormAttachment(kuerzelLabel, 0);
		abldatLabelFD.left = new FormAttachment(0, 0);
		abldatLabel.setLayoutData(abldatLabelFD);
		abldatLabel.setText(res.getString("ClosingDate"));
		
		abldatTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(abldatTitle);
		FormData abldatTitleFD = new FormData();
		abldatTitleFD.top = new FormAttachment(kuerzelLabel, 0);
		abldatTitleFD.right = new FormAttachment(26, 0);
		abldatTitle.setLayoutData(abldatTitleFD);
		
		Label jreinsaldoLabel = new Label(viewComposite, SWT.NONE);
		FormData jreinsaldoLabelFD = new FormData();
		jreinsaldoLabelFD.top = new FormAttachment(kuerzelLabel, 0);
		jreinsaldoLabelFD.left = new FormAttachment(37, 0);
		jreinsaldoLabel.setLayoutData(jreinsaldoLabelFD);
		jreinsaldoLabel.setText(res.getString("BeginningYear"));
		
		jreinsaldoTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(jreinsaldoTitle);
		FormData jreinsaldoTitleFD = new FormData();
		jreinsaldoTitleFD.top = new FormAttachment(kuerzelLabel, 0);
		jreinsaldoTitleFD.right = new FormAttachment(63, 0);
		jreinsaldoTitle.setLayoutData(jreinsaldoTitleFD);
		
		Label saldoLabel = new Label(viewComposite, SWT.NONE);
		FormData saldoLabelFD = new FormData();
		saldoLabelFD.top = new FormAttachment(kuerzelLabel, 0);
		saldoLabelFD.left = new FormAttachment(74, 0);
		saldoLabel.setLayoutData(saldoLabelFD);
		saldoLabel.setText(res.getString("Balance"));
		
		saldoTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(saldoTitle);
		FormData saldoTitleFD = new FormData();
		saldoTitleFD.top = new FormAttachment(kuerzelLabel, 0);
		saldoTitleFD.right = new FormAttachment(100, 0);
		saldoTitle.setLayoutData(saldoTitleFD);

		Label separator = new Label(viewComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData separatorFD = new FormData();
		separatorFD.top = new FormAttachment(abldatLabel, 0);
		separatorFD.left = new FormAttachment(0, 0);
		separatorFD.right = new FormAttachment(100, 0);
		separator.setLayoutData(separatorFD);
	}
	
	private void buildEditComposite() {
		editComposite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(8, false);
		editComposite.setLayout(layout);
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Title"));
		ktobezText = new Text(editComposite, SWT.SINGLE | SWT.BORDER);
		ktobezText.setTextLimit(50);
		GridData gl = new GridData(GridData.FILL_HORIZONTAL);
		gl.horizontalSpan = 7;
		ktobezText.setLayoutData(gl);

		new Label(editComposite, SWT.NONE).setText(res.getString("ShortTitle"));		
		kuerzelText = new Text(editComposite, SWT.SINGLE | SWT.BORDER);
		kuerzelText.setTextLimit(5);
		kuerzelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Type"));
		ktotypCombo = new Combo(editComposite, SWT.READ_ONLY);
		ktotypCombo.setItems(logic.fillKtotypCombo());
		ktotypCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Status"));
		kzogCombo = new Combo(editComposite, SWT.READ_ONLY);
		kzogCombo.setItems(logic.fillKzogCombo());
		kzogCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(editComposite, SWT.NONE).setText(res.getString("ClosingDate"));
		abldatText = new CDateTime(editComposite, CDT.BORDER | CDT.DROP_DOWN | CDT.BUTTON_RIGHT | CDT.COMPACT);
		abldatText.setLocale(LogicMain.instance().getUser().getFormat());
		abldatText.setFormat(CDT.DATE_MEDIUM);
		abldatText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		if (!isNew()) {
			new Label(editComposite, SWT.NONE).setText(res.getString("BeginningYear"));
			jreinsaldoTitle = new Label(editComposite, SWT.RIGHT);
			jreinsaldoTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			new Label(editComposite, SWT.NONE).setText(res.getString("Balance"));
			saldoTitle = new Label(editComposite, SWT.RIGHT);
			saldoTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		
		Composite c = new Composite(editComposite, SWT.NONE);
		GridData cgd = new GridData(GridData.FILL_HORIZONTAL);
		cgd.horizontalSpan = 8;
		c.setLayoutData(cgd);
		
		c.setLayout(new GridLayout(3, false));
		
		messageLabel = new Label(c, SWT.LEFT);
		messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		abortButton = new Button(c, SWT.PUSH);
		GUITools.setButtonLayout(abortButton, 1);
		abortButton.setText(res.getString("Abort"));
		abortButton.addSelectionListener(this);

		saveButton = new Button(c, SWT.PUSH);
		GUITools.setButtonLayout(saveButton, 1);
		saveButton.setText(res.getString("Save"));
		saveButton.addSelectionListener(this);
	}
	
	private void fillFields() {
		if (!isNew) {
			if (inUse) {
				ktobezText.setText(konto.getKtobez());
				kuerzelText.setText(konto.getKuerzel());
				ktotypCombo.select(logic.setKtotypSelection(konto));
				abldatText.setSelection(konto.getAbldat());
				kzogCombo.select(logic.setKzogSelection(konto));
			}
			else {
				ktobezTitle.setText(konto.getKtobez());
				kuerzelTitle.setText(konto.getKuerzel());
				ktotypTitle.setText(logic.getKtotyp(konto));
				abldatTitle.setText(Tools.printDate(konto.getAbldat()));
				kzogTitle.setText(logic.getKzog(konto));
			}
			saldoTitle.setText(Tools.printBigDecimal(konto.getSaldo()) + " €");
			jreinsaldoTitle.setText(Tools.printBigDecimal(konto.getJreinsaldo()) + " €");
		}
	}
	
	private void readFields() throws RuntimeException {
		if (isNew()) {
			konto = new Konto();
			konto.setJreinsaldo(new BigDecimal(0));
			konto.setSaldo(new BigDecimal(0));
			konto.setBuchdat(Tools.getLastMonth());
		}
		if (!ktobezText.getText().equals("")) {
			konto.setKtobez(ktobezText.getText());
		}
		else throw new RuntimeException(res.getString("EmptyTitle"));
		if (!kuerzelText.getText().equals("")) {
			konto.setKuerzel(kuerzelText.getText());
		}
		else throw new RuntimeException(res.getString("EmptyShortTitle"));
		if (logic.getKtotypSelection(ktotypCombo.getSelectionIndex()) != null) {
			konto.setKtotyp(logic.getKtotypSelection(ktotypCombo.getSelectionIndex()));
		}
		else throw new RuntimeException(res.getString("EmptyType"));
		if (logic.getKzogSelection(kzogCombo.getSelectionIndex()) != null) {
			konto.setKzog(logic.getKzogSelection(kzogCombo.getSelectionIndex()));
		}
		else throw new RuntimeException(res.getString("EmptyStatus"));
		if (abldatText.getSelection() != null) {
			if (abldatText.getSelection().after(new Date()) || !isNew()) {
				konto.setAbldat(abldatText.getSelection());
			}
			else throw new RuntimeException(res.getString("PastClosingDate"));
		}
		else throw new RuntimeException(res.getString("EmptyClosingDate"));
	}
	
	public Boolean isNew() {
		return isNew;
	}
	
	public void setNew(Boolean b) {
		this.isNew = b;
	}
	
	public Boolean isUsed() {
		return inUse;
	}
	
	public void setUsed(Boolean b) {
		inUse = b;
		
		if (isUsed()) {
			if (editComposite == null || editComposite.isDisposed()) {
				buildEditComposite();
			}
			fillFields();
			layout.topControl = editComposite;
			this.layout();
		}
		else {
			if (viewComposite == null || viewComposite.isDisposed()) {
				buildViewComposite();
			}
			fillFields();
			layout.topControl = viewComposite;
			editComposite.dispose();
			this.layout();
		}
	}
	
	public void lock() {
		if (!isNew()) {
			editButton.setEnabled(false);
		}		
	}
	
	public void unlock() {
		if (!isNew()) {
			editButton.setEnabled(true);
		}		
	}
	
	public void discard() {
		setUsed(false);
		uiKonto.resize();
		ScrolledComposite sComp = (ScrolledComposite)this.getParent().getParent();
		sComp.setOrigin(this.getLocation());
	}
	
	private void edit() {
		setUsed(true);
		uiKonto.lock();
		uiKonto.resize();
		setBackgroundColor(GUITools.getNonSelectionColor());
		
		ktobezText.setFocus();
		this.getShell().setDefaultButton(saveButton);
	}

	private void abort() {
		uiKonto.unlock();
	}
	
	private void save() {
		try {
			readFields();
			if (logic.save(konto)) {
				isNew = false;
				setUsed(false);
				uiKonto.unlock();
				uiKonto.update();
				((UIDataFrame)uiKonto.getParent().getParent().getParent()).setUpdates(true);
			}
		}
		catch (RuntimeException re) {
			messageLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			messageLabel.setText(re.getMessage());
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent se) {}

	public void widgetSelected(SelectionEvent se) {
		if (se.getSource().equals(editButton)) {
			edit();
		}
		else if (se.getSource().equals(abortButton)) {
			abort();
		}
		else if (se.getSource().equals(saveButton)) {
			save();
		}
		if (!this.isDisposed()) {
			ScrolledComposite sComp = (ScrolledComposite)this.getParent().getParent();
			sComp.setOrigin(this.getLocation());
		}
	}
	
}