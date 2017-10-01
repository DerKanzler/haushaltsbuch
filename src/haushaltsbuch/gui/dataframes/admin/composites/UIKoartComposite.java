package haushaltsbuch.gui.dataframes.admin.composites;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dataframes.admin.UIKoartAdmin;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.admin.composites.LogicKoartComposite;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.CostTypeGroupsCombo;

import java.math.BigDecimal;
import java.util.ResourceBundle;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UIKoartComposite extends UIAbstractComposite implements SelectionListener {
	
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private UIKoartAdmin uiKoart;
	private Kostenart koart;
	private LogicKoartComposite logic = new LogicKoartComposite();
	
	private StackLayout layout;
	
	private Label koartkubezTitle, koartbezTitle, saldoTitle, koartgrpbezTitle, koartgrpkatTitle, koartgrpartTitle, messageLabel;
	private Text koartkubezText, koartbezText;
	private CostTypeGroupsCombo koartgrpbezCombo;
	private Button editButton, abortButton, saveButton;
	
	private Boolean inUse = false;
	private Boolean isNew = false;
	
	public UIKoartComposite(Composite parent, int style) {
		super(parent, style);
		this.uiKoart = (UIKoartAdmin)this.getParent().getParent().getParent();
		
		layout = new StackLayout();
		this.setLayout(layout);
	}
	
	public void buildUI(Kostenart koart) {		
		if (koart != null) {
			this.koart = koart;
			buildViewComposite();
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
		
		koartbezTitle = new Label(viewComposite, SWT.LEFT);
		GUITools.setBoldFont(koartbezTitle);
		FormData koartbezTitleFD = new FormData();
		koartbezTitleFD.top = new FormAttachment(0, 0);
		koartbezTitleFD.left = new FormAttachment(0, 0);
		koartbezTitle.setLayoutData(koartbezTitleFD);
		
		editButton = new Button(viewComposite, SWT.FLAT);
		editButton.setImage(new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/edit.png")));
		FormData editButtonFD = new FormData();
		editButtonFD.top = new FormAttachment(0, 0);
		editButtonFD.right = new FormAttachment(100, 0);
		editButton.setLayoutData(editButtonFD);		
		editButton.addSelectionListener(this);
		
		Label koartkubezLabel = new Label(viewComposite, SWT.LEFT);
		FormData koartkubezLabelFD = new FormData();
		koartkubezLabelFD.top = new FormAttachment(editButton, 0);
		koartkubezLabelFD.left = new FormAttachment(koartbezTitle, 0, SWT.LEFT);
		koartkubezLabel.setLayoutData(koartkubezLabelFD);
		koartkubezLabel.setText(res.getString("ShortTitle"));
		
		koartkubezTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(koartkubezTitle);
		FormData koartkubezTitleFD = new FormData();
		koartkubezTitleFD.top = new FormAttachment(editButton, 0);
		koartkubezTitleFD.right = new FormAttachment(22, 0);
		koartkubezTitle.setLayoutData(koartkubezTitleFD);
		
		Label koartgrpbezLabel = new Label(viewComposite, SWT.LEFT);
		FormData koartgrpbezLabelFD = new FormData();
		koartgrpbezLabelFD.top = new FormAttachment(editButton, 0);
		koartgrpbezLabelFD.left = new FormAttachment(33, 0);
		koartgrpbezLabel.setLayoutData(koartgrpbezLabelFD);
		koartgrpbezLabel.setText(res.getString("Costtypegroup"));
		
		koartgrpbezTitle = new Label(viewComposite, SWT.LEFT);
		GUITools.setBoldFont(koartgrpbezTitle);
		FormData koartgrpbezTitleFD = new FormData();
		koartgrpbezTitleFD.top = new FormAttachment(editButton, 0);
		koartgrpbezTitleFD.left = new FormAttachment(46, 0);
		koartgrpbezTitleFD.right = new FormAttachment(100, 0);
		koartgrpbezTitle.setLayoutData(koartgrpbezTitleFD);
		
		Label saldoLabel = new Label(viewComposite, SWT.LEFT);
		FormData saldoLabelFD = new FormData();
		saldoLabelFD.top = new FormAttachment(koartkubezLabel, 0);
		saldoLabelFD.left = new FormAttachment(0, 0);
		saldoLabel.setLayoutData(saldoLabelFD);
		saldoLabel.setText(res.getString("Balance"));
		
		saldoTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(saldoTitle);
		FormData saldoTitleFD = new FormData();
		saldoTitleFD.top = new FormAttachment(koartkubezLabel, 0);
		saldoTitleFD.right = new FormAttachment(22, 0);
		saldoTitle.setLayoutData(saldoTitleFD);
		
		Label koartgrpkatLabel = new Label(viewComposite, SWT.LEFT);
		FormData koartgrpkatLabelFD = new FormData();
		koartgrpkatLabelFD.top = new FormAttachment(koartkubezLabel, 0);
		koartgrpkatLabelFD.left = new FormAttachment(33, 0);
		koartgrpkatLabel.setLayoutData(koartgrpkatLabelFD);
		koartgrpkatLabel.setText(res.getString("Category"));
		
		koartgrpkatTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(koartgrpkatTitle);
		FormData koartgrpkatTitleFD = new FormData();
		koartgrpkatTitleFD.top = new FormAttachment(koartkubezLabel, 0);
		koartgrpkatTitleFD.left = new FormAttachment(46, 0);
		koartgrpkatTitle.setLayoutData(koartgrpkatTitleFD);
		
		Label koartgrpartLabel = new Label(viewComposite, SWT.LEFT);
		FormData koartgrpartLabelFD = new FormData();
		koartgrpartLabelFD.top = new FormAttachment(koartkubezLabel, 0);
		koartgrpartLabelFD.left = new FormAttachment(78, 0);
		koartgrpartLabel.setLayoutData(koartgrpartLabelFD);
		koartgrpartLabel.setText(res.getString("Type"));
		
		koartgrpartTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(koartgrpartTitle);
		FormData koartgrpartTitleFD = new FormData();
		koartgrpartTitleFD.top = new FormAttachment(koartkubezLabel, 0);
		koartgrpartTitleFD.right = new FormAttachment(100, 0);
		koartgrpartTitle.setLayoutData(koartgrpartTitleFD);
		
		Label separator = new Label(viewComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData separatorFD = new FormData();
		separatorFD.top = new FormAttachment(saldoLabel, 0);
		separatorFD.left = new FormAttachment(0, 0);
		separatorFD.right = new FormAttachment(100, 0);
		separator.setLayoutData(separatorFD);
	}
	
	private void buildEditComposite() {
		editComposite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(8, false);
		editComposite.setLayout(layout);
		
		new Label(editComposite, SWT.LEFT).setText(res.getString("Title"));
		koartbezText = new Text(editComposite, SWT.SINGLE | SWT.BORDER);		
		koartbezText.setTextLimit(50);
		GridData koartbezTextGD = new GridData(GridData.FILL_HORIZONTAL);
		koartbezTextGD.horizontalSpan = 7;
		koartbezText.setLayoutData(koartbezTextGD);
		
		new Label(editComposite, SWT.LEFT).setText(res.getString("ShortTitle"));		
		koartkubezText = new Text(editComposite, SWT.SINGLE | SWT.BORDER);
		koartkubezText.setTextLimit(10);
		koartkubezText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(editComposite, SWT.LEFT).setText(res.getString("Costtypegroup"));
		GridData koartgrpbezGD = new GridData(GridData.FILL_HORIZONTAL);
		koartgrpbezGD.horizontalSpan = 4;
		koartgrpbezCombo = new CostTypeGroupsCombo(editComposite, SWT.READ_ONLY);
		koartgrpbezCombo.getCombo().setLayoutData(koartgrpbezGD);
		koartgrpbezCombo.getCombo().addSelectionListener(this);
		
		if (!isNew()) {
			new Label(editComposite, SWT.LEFT).setText(res.getString("Balance"));
			saldoTitle = new Label(editComposite, SWT.RIGHT);
			saldoTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		else {
			Label filler = new Label(editComposite, SWT.NONE);
			GridData fillerGD = new GridData(GridData.FILL_HORIZONTAL);
			fillerGD.horizontalSpan = 3;
			filler.setLayoutData(fillerGD);
		}
		
		new Label(editComposite, SWT.LEFT).setText(res.getString("Category"));
		koartgrpkatTitle = new Label(editComposite, SWT.RIGHT);
		koartgrpkatTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(editComposite, SWT.LEFT).setText(res.getString("Type"));
		koartgrpartTitle = new Label(editComposite, SWT.RIGHT);
		koartgrpartTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
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
				koartbezText.setText(koart.getKoartbez());
				koartkubezText.setText(koart.getKoartkubez());
				koartgrpbezCombo.update(logic.getCorrespondingKoartgrp(koart.getKoartgrp().getKoartgrpkat()));
				koartgrpbezCombo.setSelectedCostTypeGroup(koart.getKoartgrp());
			}
			else {
				koartbezTitle.setText(koart.getKoartbez());
				koartkubezTitle.setText(koart.getKoartkubez());
				koartgrpbezTitle.setText(koart.getKoartgrp().getKoartgrpbez());
			}
			saldoTitle.setText(Tools.printBigDecimal(koart.getKoartsaldo()) + " €");
			koartgrpkatTitle.setText(Tools.getKoartgrpkat(koart.getKoartgrp()));
			koartgrpartTitle.setText(Tools.getKoartgrpart(koart.getKoartgrp()));
		}
		else koartgrpbezCombo.update(logic.getAllKoartgrp());
	}
	
	private void readFields() throws RuntimeException {
		if (isNew()) {
			koart = new Kostenart();
			koart.setKoartsaldo(new BigDecimal(0));
		}
		if (!koartbezText.getText().equals("")) {
			koart.setKoartbez(koartbezText.getText());
		}
		else throw new RuntimeException(res.getString("EmptyCTTitle"));
		if (!koartkubezText.getText().equals("")) {
			koart.setKoartkubez(koartkubezText.getText());
		}
		else throw new RuntimeException(res.getString("EmptyCTShortTitle"));
		if (koartgrpbezCombo.getSelectedCostType() != null) {
			koart.setKoartgrp(koartgrpbezCombo.getSelectedCostType());
		}
		else throw new RuntimeException(res.getString("EmptyCosttypegroup"));
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
		uiKoart.resize();
		ScrolledComposite sComp = (ScrolledComposite)this.getParent().getParent();
		sComp.setOrigin(this.getLocation());
	}
	
	private void edit() {
		setUsed(true);
		uiKoart.lock();
		uiKoart.resize();				
		setBackgroundColor(GUITools.getNonSelectionColor());
		
		koartbezText.setFocus();
		this.getShell().setDefaultButton(saveButton);
	}

	private void abort() {
		uiKoart.unlock();
	}
	
	private void save() {
		try {
			readFields();
			if (logic.save(koart)) {
				setNew(false);
				setUsed(false);
				uiKoart.unlock();
				uiKoart.update();
				((UIDataFrame)uiKoart.getParent().getParent().getParent()).setUpdates(true);
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
		else if (se.getSource().equals(koartgrpbezCombo.getCombo())) {
			Kostenartgruppe koartgrp = koartgrpbezCombo.getSelectedCostType();
			koartgrpkatTitle.setText(Tools.getKoartgrpkat(koartgrp));
			koartgrpartTitle.setText(Tools.getKoartgrpart(koartgrp));
		}
		if (!this.isDisposed()) {
			ScrolledComposite sComp = (ScrolledComposite)this.getParent().getParent();
			sComp.setOrigin(this.getLocation());
		}
			
	}
	
}