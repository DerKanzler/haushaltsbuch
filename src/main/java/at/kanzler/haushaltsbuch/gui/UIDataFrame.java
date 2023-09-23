package at.kanzler.haushaltsbuch.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import at.kanzler.haushaltsbuch.gui.dataframes.UIAbstractDataFrame;
import at.kanzler.haushaltsbuch.gui.dataframes.UIBuchen;
import at.kanzler.haushaltsbuch.gui.dataframes.UIJahrUEKapital;
import at.kanzler.haushaltsbuch.gui.dataframes.UIJahrUEKoart;
import at.kanzler.haushaltsbuch.gui.dataframes.UIMonatUE;
import at.kanzler.haushaltsbuch.gui.dataframes.UISuchen;
import at.kanzler.haushaltsbuch.gui.dataframes.UIVerwaltung;

/**
 * This class is the underlaying layer for all the dataframes.
 * It activates the dataframe which the user wants to see and builds
 * it if it wasn't done before.
 * 
 * @author pk, bl
 *
 */
public class UIDataFrame extends Composite {

    private StackLayout layout;

    private UIBuchen buchenFrame;
    private UISuchen suchenFrame;
    private UIMonatUE monatUEFrame;
    private UIJahrUEKapital jahrUEKapitalFrame;
    private UIJahrUEKoart jahrUEKoartFrame;
    private UIVerwaltung verwaltungFrame;

    /**
     * Constructor: the class extends Composite. So the super constructor is called
     * with the given parameters and a stack layout is set as the layout of the composite/UIDataFrame.
     * 
     * @param Composite
     * @param Integer
     */
    public UIDataFrame(Composite parent, Integer style) {
        super(parent, style);
        layout = new StackLayout();
        this.setLayout(layout);
    }

    /**
     * Activates the booking dataframe. Calls the build methods if necessary.
     */
    public void activateBuchen() {
        if (buchenFrame == null) {
            buchenFrame = new UIBuchen(this, SWT.NONE);
        }
        layout.topControl = buchenFrame;
        if (buchenFrame.hasToBeUpdated())
            buchenFrame.update();
        this.layout();
        buchenFrame.activate();
    }

    /**
     * Activates the search dataframe. Calls the build methods if necessary.
     */
    public void activateSuchen() {
        if (suchenFrame == null) {
            suchenFrame = new UISuchen(this, SWT.NONE);
        }
        layout.topControl = suchenFrame;
        if (suchenFrame.hasToBeUpdated())
            suchenFrame.update();
        this.layout();
        suchenFrame.activate();
    }

    /**
     * Activates the search dataframe if you come from the fastsearch textfield..
     */
    public void activateSuchen(String s) {
        activateSuchen();
        suchenFrame.fastSearch(s);
    }

    /**
     * Activates the month overview dataframe. Calls the build methods if necessary.
     */
    public void activateMonatUE() {
        if (monatUEFrame == null) {
            monatUEFrame = new UIMonatUE(this, SWT.NONE);
        }
        layout.topControl = monatUEFrame;
        if (monatUEFrame.hasToBeUpdated())
            monatUEFrame.update();
        this.layout();
    }

    /**
     * Activates the year overview of the assets dataframe. Calls the build methods if necessary.
     */
    public void activateJahrUEKapital() {
        if (jahrUEKapitalFrame == null) {
            jahrUEKapitalFrame = new UIJahrUEKapital(this, SWT.NONE);
        }
        layout.topControl = jahrUEKapitalFrame;
        if (jahrUEKapitalFrame.hasToBeUpdated())
            jahrUEKapitalFrame.update();
        this.layout();
    }

    /**
     * Activates the year overview of the cost types dataframe. Calls the build methods if necessary.
     */
    public void activateJahrUEKoart() {
        if (jahrUEKoartFrame == null) {
            jahrUEKoartFrame = new UIJahrUEKoart(this, SWT.NONE);
        }
        layout.topControl = jahrUEKoartFrame;
        if (jahrUEKoartFrame.hasToBeUpdated())
            jahrUEKoartFrame.update();
        this.layout();
    }

    /**
     * Activates the administrative dataframe. Calls the build methods if necessary.
     */
    public void activateVerwaltung() {
        if (verwaltungFrame == null) {
            verwaltungFrame = new UIVerwaltung(this, SWT.NONE);
            verwaltungFrame.buildUI();
        }
        layout.topControl = verwaltungFrame;
        if (verwaltungFrame.hasToBeUpdated())
            verwaltungFrame.update();
        this.layout();
    }

    public void deactivateDataFrame() {
        StackLayout sl = (StackLayout) this.getLayout();
        UIAbstractDataFrame adf = (UIAbstractDataFrame) sl.topControl;
        if (adf != null)
            adf.deactivate();
    }

    public void setUpdates(Boolean toBeUpdated) {
        if (buchenFrame != null)
            buchenFrame.setToBeUpdated(toBeUpdated);
        if (suchenFrame != null)
            suchenFrame.setToBeUpdated(toBeUpdated);
        if (monatUEFrame != null)
            monatUEFrame.setToBeUpdated(toBeUpdated);
        if (jahrUEKapitalFrame != null)
            jahrUEKapitalFrame.setToBeUpdated(toBeUpdated);
        if (jahrUEKoartFrame != null)
            jahrUEKoartFrame.setToBeUpdated(toBeUpdated);
        if (verwaltungFrame != null)
            verwaltungFrame.setToBeUpdated(toBeUpdated);
    }

}