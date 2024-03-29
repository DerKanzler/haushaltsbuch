package at.kanzler.haushaltsbuch.gui.dataframes.admin;

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

import at.kanzler.haushaltsbuch.bean.Konto;
import at.kanzler.haushaltsbuch.gui.dataframes.admin.composites.UIKontoComposite;
import at.kanzler.haushaltsbuch.logic.LogicMain;
import at.kanzler.haushaltsbuch.logic.admin.LogicKontoAdmin;

/**
 * This class displays the "Konten verwalten" screen.
 *
 * You can edit and add accounts here.
 *
 * @author pk
 *
 */
public class UIKontoAdmin extends UIAbstractAdmin implements SelectionListener {

    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    private ScrolledComposite sComp;
    private Composite content;

    private Button addButton;

    private LogicKontoAdmin logic = LogicKontoAdmin.instance();
    private Vector<UIKontoComposite> accountsComposites = new Vector<>();

    /**
     * Constructor: Since UIKonto extends Composite the super constructor is called here with the given arguments.
     * Builds the visible user interface. Uses GridLayout with 3 columns.
     *
     * @param Composite
     * @param Integer
     */
    public UIKontoAdmin(Composite parent, Integer style) {
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

        buildAccountsList();
        sComp.setContent(content);
        resize();

        new Label(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        addButton = new Button(this, SWT.NONE);
        addButton.setText(res.getString("AddAccount"));
        addButton.addSelectionListener(this);
    }

    @Override
    public void lock() {
        for (UIKontoComposite ac : accountsComposites) {
            ac.lock();
        }
        this.getShell().setModified(true);
        addButton.setEnabled(false);
    }

    @Override
    public void unlock() {
        UIKontoComposite delAC = null;
        for (UIKontoComposite ac : accountsComposites) {
            ac.unlock();
            if (ac.isUsed()) {
                if (ac.isNew()) {
                    delAC = ac;
                } else {
                    ac.discard();
                }
            }
        }
        if (delAC != null) {
            remove(delAC);
        }
        this.getShell().setModified(false);
        addButton.setEnabled(true);
    }

    @Override
    public void remove(Composite ac) {
        accountsComposites.remove(ac);
        ac.dispose();
        resize();
        sComp.setOrigin(0, 0);
    }

    @Override
    public void resize() {
        sComp.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    @Override
    public void update() {
        content = new Composite(sComp, SWT.NONE);
        accountsComposites = new Vector<>();

        buildAccountsList();
        sComp.setContent(content);
        resize();

        addButton.setEnabled(true);
    }

    private void buildAccountsList() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        content.setLayout(layout);

        Vector<Konto> konten = logic.getAll();
        for (Konto k : konten) {
            UIKontoComposite uiKontoComposite = new UIKontoComposite(content, SWT.NONE);
            uiKontoComposite.buildUI(k);
            uiKontoComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            accountsComposites.add(uiKontoComposite);
        }
    }

    /**
     * Does nothing.
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent se) {}

    /**
     * Catches the SelectionEvent when a button is clicked.
     */
    @Override
    public void widgetSelected(SelectionEvent se) {
        if (se.getSource().equals(addButton)) {
            UIKontoComposite uiKontoComposite = new UIKontoComposite(content, SWT.NONE);
            uiKontoComposite.buildUI(null);
            uiKontoComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            accountsComposites.add(uiKontoComposite);
            sComp.setContent(content);
            resize();
            sComp.setOrigin(uiKontoComposite.getLocation());
        }
    }

}
