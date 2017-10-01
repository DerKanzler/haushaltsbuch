package haushaltsbuch.gui.dataframes.admin;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.gui.dataframes.admin.composites.UIKoartgrpComposite;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.admin.LogicKoartgrpAdmin;

import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This class displays the search screen.
 * 
 * You can search for bookings with the bookingtext, the amount or other values.
 * 
 * @author pk
 *
 */
public class UIKoartgrpAdmin extends UIAbstractAdmin implements SelectionListener {
	
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private ScrolledComposite sComp;
	private Composite content;
	
	private Button addButton;
	
	private LogicKoartgrpAdmin logic = LogicKoartgrpAdmin.instance();
	private Vector<UIKoartgrpComposite> costTypeGroupsComposites = new Vector<UIKoartgrpComposite>();
	
	/**
	 * Constructor: Since UIKonto extends Composite the super constructor is called here with the given arguments.
	 * Builds the visible user interface. Uses GridLayout with 3 columns.
	 * 
	 * @param Composite
	 * @param Integer
	 */
	public UIKoartgrpAdmin(Composite parent, Integer style) {
		super(parent, style);

		GridLayout layout = new GridLayout(3, false);
		this.setLayout(layout);
		
		sComp = new ScrolledComposite(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		sComp.setLayoutData(gd);
		sComp.setExpandHorizontal(true);
	    sComp.setExpandVertical(true);
	    content = new Composite(sComp, SWT.NONE);
	    sComp.setContent(content);
	    
	    buildCostTypesList();
    	resize();
		
		new Label(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		addButton = new Button(this, SWT.NONE);
		addButton.setText(res.getString("AddCosttypegroup"));
		addButton.addSelectionListener(this);
	}
	
	public void lock() {
		for (UIKoartgrpComposite ctgc: costTypeGroupsComposites) {
			ctgc.lock();
		}
		this.getShell().setModified(true);
		addButton.setEnabled(false);
	}
	
	public void unlock() {
		UIKoartgrpComposite delCTGC = null;
		for (UIKoartgrpComposite ctgc: costTypeGroupsComposites) {
			ctgc.unlock();
			if (ctgc.isUsed()) {
				if (ctgc.isNew()) delCTGC = ctgc;
				else ctgc.discard();
			}
		}
		if (delCTGC != null) remove(delCTGC);
		this.getShell().setModified(false);
		addButton.setEnabled(true);
	}
	
	public void remove(Composite ctgc) {
		costTypeGroupsComposites.remove(ctgc);
		ctgc.dispose();
		resize();
		sComp.setOrigin(0, 0);
	}
	
	public void resize() {
		sComp.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void update() {
	    content = new Composite(sComp, SWT.NONE);		    
	    costTypeGroupsComposites = new Vector<UIKoartgrpComposite>();
	    
	    buildCostTypesList();
	    sComp.setContent(content);
	    resize();

	    addButton.setEnabled(true);
	}

	private void buildCostTypesList() {
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		content.setLayout(layout);
		
		for (Kostenartgruppe koartgrp: logic.getAll()) {
			UIKoartgrpComposite koartgrpComposite = new UIKoartgrpComposite(content, SWT.NONE);
			koartgrpComposite.buildUI(koartgrp);
			koartgrpComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			costTypeGroupsComposites.add(koartgrpComposite);
		}
	}
	
	/**
	 * Does nothing.
	 */
	public void widgetDefaultSelected(SelectionEvent se) {}

	/**
	 * Catches the SelectionEvent when a button is clicked.
	 */
	public void widgetSelected(SelectionEvent se) {
		if (se.getSource().equals(addButton)) {
			UIKoartgrpComposite koartgrpComposite = new UIKoartgrpComposite(content, SWT.NONE);
			koartgrpComposite.buildUI(null);
			koartgrpComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			costTypeGroupsComposites.add(koartgrpComposite);
			sComp.setContent(content);
			resize();
			sComp.setOrigin(koartgrpComposite.getLocation());		
		}
	}
	
}