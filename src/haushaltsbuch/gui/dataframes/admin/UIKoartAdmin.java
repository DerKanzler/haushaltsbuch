package haushaltsbuch.gui.dataframes.admin;

import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.gui.dataframes.admin.composites.UIKoartComposite;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.admin.LogicKoartAdmin;

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
 * This class displays the "Kostenart verwalten" screen.
 * 
 * You can edit and add "Kostenarten" here.
 * 
 * @author pk
 *
 */
public class UIKoartAdmin extends UIAbstractAdmin implements SelectionListener {
	
	private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings", LogicMain.instance().getUser().getFormat());
	
	private Button addButton;
	
	private ScrolledComposite sComp;
	private Composite content;
	
	private LogicKoartAdmin logic = LogicKoartAdmin.instance();
	private Vector<UIKoartComposite> costTypesComposites = new Vector<UIKoartComposite>();
	
	/**
	 * Constructor: Since UIKonto extends Composite the super constructor is called here with the given arguments.
	 * Builds the visible user interface. Uses GridLayout with 3 columns.
	 * 
	 * @param Composite
	 * @param Integer
	 */
	public UIKoartAdmin(Composite parent, Integer style) {
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
		addButton.setText(res.getString("AddCosttype"));
		addButton.addSelectionListener(this);
	}
	
	public void lock() {
		for (UIKoartComposite ctc: costTypesComposites) {
			ctc.lock();
		}
		this.getShell().setModified(true);
		addButton.setEnabled(false);
	}
	
	public void unlock() {
		try {
		UIKoartComposite delCTC = null;
		for (UIKoartComposite ctc: costTypesComposites) {
			ctc.unlock();
			if (ctc.isUsed()) {
				if (ctc.isNew()) delCTC = ctc;
				else ctc.discard();
			}
		}
		if (delCTC != null) remove(delCTC);
		this.getShell().setModified(false);
		addButton.setEnabled(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void remove(Composite ctc) {
		costTypesComposites.remove(ctc);
		ctc.dispose();
		resize();
		sComp.setOrigin(0, 0);		
	}
	
	public void resize() {
		sComp.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void update() {
	    content = new Composite(sComp, SWT.NONE);		    
	    costTypesComposites = new Vector<UIKoartComposite>();
	    
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
		
		for (Kostenart koart: logic.getAll()) {
			UIKoartComposite koartComposite = new UIKoartComposite(content, SWT.NONE);
			koartComposite.buildUI(koart);
			koartComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			costTypesComposites.add(koartComposite);
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
			UIKoartComposite koartComposite = new UIKoartComposite(content, SWT.NONE);
			koartComposite.buildUI(null);
			koartComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			costTypesComposites.add(koartComposite);
			sComp.setContent(content);
			resize();
			sComp.setOrigin(koartComposite.getLocation());		
		}
	}
	
}