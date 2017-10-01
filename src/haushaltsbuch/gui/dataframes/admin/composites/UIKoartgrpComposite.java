package haushaltsbuch.gui.dataframes.admin.composites;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.gui.UIDataFrame;
import haushaltsbuch.gui.dataframes.admin.UIKoartgrpAdmin;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.admin.composites.LogicKoartgrpComposite;
import haushaltsbuch.util.GUITools;

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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UIKoartgrpComposite extends UIAbstractComposite implements SelectionListener {
	
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private UIKoartgrpAdmin uiKoartgrp;
	private Kostenartgruppe koartgrp;
	private LogicKoartgrpComposite logic = new LogicKoartgrpComposite();
	
	private StackLayout layout;
	
	private Label koartgrpbezTitle, koartgrpkatTitle, koartgrpartTitle, messageLabel;
	private Text koartgrpbezText;
	private Combo koartgrpkatCombo, koartgrpartCombo;
	private Button editButton, abortButton, saveButton;
	
	private Boolean inUse = false;
	private Boolean isNew = false;	
	
	public UIKoartgrpComposite(Composite parent, int style) {
		super(parent, style);
		this.uiKoartgrp = (UIKoartgrpAdmin)this.getParent().getParent().getParent();
		
		layout = new StackLayout();
		this.setLayout(layout);
	}
	
	public void buildUI(Kostenartgruppe koartgrp) {		
		if (koartgrp != null) {
			this.koartgrp = koartgrp;
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
		
		koartgrpbezTitle = new Label(viewComposite, SWT.NONE);
		GUITools.setBoldFont(koartgrpbezTitle);
		FormData koartgrpbezTitleFD = new FormData();
		koartgrpbezTitleFD.top = new FormAttachment(0, 0);
		koartgrpbezTitleFD.left = new FormAttachment(0, 0);
		koartgrpbezTitle.setLayoutData(koartgrpbezTitleFD);
		
		editButton = new Button(viewComposite, SWT.FLAT);
		editButton.setImage(new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/edit.png")));
		FormData editButtonFD = new FormData();
		editButtonFD.top = new FormAttachment(0, 0);
		editButtonFD.right = new FormAttachment(100, 0);
		editButton.setLayoutData(editButtonFD);		
		editButton.addSelectionListener(this);
		
		Label koartgrpkatLabel = new Label(viewComposite, SWT.NONE);
		FormData koartgrpkatLabelFD = new FormData();
		koartgrpkatLabelFD.top = new FormAttachment(editButton, 0);
		koartgrpkatLabelFD.left = new FormAttachment(koartgrpbezTitle, 0, SWT.LEFT);
		koartgrpkatLabel.setLayoutData(koartgrpkatLabelFD);
		koartgrpkatLabel.setText(res.getString("Category"));
		
		koartgrpkatTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(koartgrpkatTitle);
		FormData koartgrpkatTitleFD = new FormData();
		koartgrpkatTitleFD.top = new FormAttachment(editButton, 0);
		koartgrpkatTitleFD.right = new FormAttachment(33, 0);
		koartgrpkatTitle.setLayoutData(koartgrpkatTitleFD);
		
		Label koartgrpartLabel = new Label(viewComposite, SWT.NONE);
		FormData koartgrpartLabelFD = new FormData();
		koartgrpartLabelFD.top = new FormAttachment(editButton, 0);
		koartgrpartLabelFD.left = new FormAttachment(67, 0);
		koartgrpartLabel.setLayoutData(koartgrpartLabelFD);
		koartgrpartLabel.setText(res.getString("Type"));
		
		koartgrpartTitle = new Label(viewComposite, SWT.RIGHT);
		GUITools.setBoldFont(koartgrpartTitle);
		FormData koartgrpartTitleFD = new FormData();
		koartgrpartTitleFD.top = new FormAttachment(editButton, 0);
		koartgrpartTitleFD.right = new FormAttachment(100, 0);
		koartgrpartTitle.setLayoutData(koartgrpartTitleFD);

		Label separator = new Label(viewComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData separatorFD = new FormData();
		separatorFD.top = new FormAttachment(koartgrpkatLabel, 0);
		separatorFD.left = new FormAttachment(0, 0);
		separatorFD.right = new FormAttachment(100, 0);
		separator.setLayoutData(separatorFD);
	}
	
	private void buildEditComposite() {
		editComposite = new Composite(this, SWT.NONE);
		GridLayout layout = new GridLayout(5, false);
		editComposite.setLayout(layout);
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Title"));		
		koartgrpbezText = new Text(editComposite, SWT.SINGLE | SWT.BORDER);
		koartgrpbezText.setTextLimit(50);
		GridData koartgrpbezGD = new GridData(GridData.FILL_HORIZONTAL);
		koartgrpbezGD.horizontalSpan = 4;
		koartgrpbezText.setLayoutData(koartgrpbezGD);
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Category"));
		if (isNew()) {
			koartgrpkatCombo = new Combo(editComposite, SWT.READ_ONLY);
			koartgrpkatCombo.setItems(logic.fillKoartgrpkatCombo());
			koartgrpkatCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		else {
			koartgrpkatTitle = new Label(editComposite, SWT.RIGHT);
			koartgrpkatTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		new Label(editComposite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(editComposite, SWT.NONE).setText(res.getString("Type"));
		koartgrpartCombo = new Combo(editComposite, SWT.READ_ONLY);
		koartgrpartCombo.setItems(logic.fillKoartgrpartCombo());
		koartgrpartCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
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
		if (!isNew()) {
			if (inUse) {
				koartgrpbezText.setText(koartgrp.getKoartgrpbez());
				koartgrpartCombo.select(logic.setKoartgrpartSelection(koartgrp));
			}
			else {
				koartgrpbezTitle.setText(koartgrp.getKoartgrpbez());
				koartgrpartTitle.setText(logic.getKoartgrpart(koartgrp));
			}
			koartgrpkatTitle.setText(logic.getKoartgrpkat(koartgrp));
		}
	}
	
	private void readFields() throws RuntimeException {
		if (isNew()) {
			koartgrp = new Kostenartgruppe();
			if (logic.getKoartgrpkatSelection(koartgrpkatCombo.getSelectionIndex()) != null) {
				koartgrp.setKoartgrpkat(logic.getKoartgrpkatSelection(koartgrpkatCombo.getSelectionIndex()));
			}
			else throw new RuntimeException("Es muss eine Kategorie gewählt werden!");
		}
		if (!koartgrpbezText.getText().equals("")) {
			koartgrp.setKoartgrpbez(koartgrpbezText.getText());
		}
		else throw new RuntimeException("Kostenartgruppenbezeichnung darf nicht leer sein!");
		if (logic.getKoartgrpartSelection(koartgrpartCombo.getSelectionIndex()) != null) {
			koartgrp.setKoartgrpart(logic.getKoartgrpartSelection(koartgrpartCombo.getSelectionIndex()));
		}
		else throw new RuntimeException("Kostenartgruppenart darf nicht leer sein!");
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
		uiKoartgrp.resize();
		ScrolledComposite sComp = (ScrolledComposite)this.getParent().getParent();
		sComp.setOrigin(this.getLocation());
	}
	
	private void edit() {
		setUsed(true);
		uiKoartgrp.lock();
		uiKoartgrp.resize();
		setBackgroundColor(GUITools.getNonSelectionColor());
		
		koartgrpbezText.setFocus();
		this.getShell().setDefaultButton(saveButton);
	}

	private void abort() {
		uiKoartgrp.unlock();
	}
	
	private void save() {
		try {
			readFields();
			if (logic.save(koartgrp)) {
				setNew(false);
				setUsed(false);
				uiKoartgrp.unlock();
				uiKoartgrp.update();
				((UIDataFrame)uiKoartgrp.getParent().getParent().getParent()).setUpdates(true);
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