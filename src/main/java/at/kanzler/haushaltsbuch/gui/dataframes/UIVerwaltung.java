package at.kanzler.haushaltsbuch.gui.dataframes;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import at.kanzler.haushaltsbuch.gui.dataframes.admin.UIAbstractAdmin;
import at.kanzler.haushaltsbuch.gui.dataframes.admin.UIKoartAdmin;
import at.kanzler.haushaltsbuch.gui.dataframes.admin.UIKoartgrpAdmin;
import at.kanzler.haushaltsbuch.gui.dataframes.admin.UIKontoAdmin;
import at.kanzler.haushaltsbuch.logic.LogicMain;

public class UIVerwaltung extends UIAbstractDataFrame implements SelectionListener {

    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    private TabFolder tabFolder;
    private TabItem kontenTab, koartgrpTab, koartTab;
    private TabItem activeTab;
    private UIKontoAdmin uiKonto;
    private UIKoartgrpAdmin uiKoartgrp;
    private UIKoartAdmin uiKoart;

    public UIVerwaltung(Composite parent, Integer style) {
        super(parent, style);
    }

    public void buildUI() {
        GridLayout layout = new GridLayout(1, false);
        this.setLayout(layout);

        tabFolder = new TabFolder(this, SWT.TOP);
        GridData tabFolderGD = new GridData(GridData.FILL_BOTH);
        tabFolder.setLayoutData(tabFolderGD);

        kontenTab = new TabItem(tabFolder, SWT.NONE);
        kontenTab.setText(res.getString("Accounts"));
        uiKonto = new UIKontoAdmin(tabFolder, SWT.NONE);
        kontenTab.setControl(uiKonto);

        koartTab = new TabItem(tabFolder, SWT.NONE);
        koartTab.setText(res.getString("Costtypes"));

        koartgrpTab = new TabItem(tabFolder, SWT.NONE);
        koartgrpTab.setText(res.getString("Costtypegroups"));

        tabFolder.setSelection(0);
        tabFolder.addSelectionListener(this);

        activeTab = kontenTab;
    }

    @Override
    public void update() {
        if (tabFolder.getSelectionIndex() == 0) {
            uiKonto.update();
        } else if (tabFolder.getSelectionIndex() == 1) {
            uiKoart.update();
        } else if (tabFolder.getSelectionIndex() == 2) {
            uiKoartgrp.update();
        }
        setToBeUpdated(false);
    }

    public void widgetDefaultSelected(SelectionEvent se) {}

    public void widgetSelected(SelectionEvent se) {
        if (this.getShell().getModified()) {
            MessageBox box = new MessageBox(this.getShell(),
                    SWT.ICON_QUESTION | SWT.PRIMARY_MODAL | SWT.YES | SWT.NO | SWT.SHEET);
            box.setMessage(res.getString("Continue"));
            se.doit = box.open() == SWT.YES;
            if (se.doit) {
                UIAbstractAdmin aa = (UIAbstractAdmin) activeTab.getControl();
                aa.unlock();
            } else
                tabFolder.setSelection(activeTab);
        }
        if (se.doit) {
            if (se.item.equals(kontenTab)) {
                if (kontenTab.getControl() == null) {
                    uiKonto = new UIKontoAdmin(tabFolder, SWT.NONE);
                    kontenTab.setControl(uiKonto);
                } else
                    uiKonto.update();
                activeTab = kontenTab;
            } else if (se.item.equals(koartTab)) {
                if (koartTab.getControl() == null) {
                    uiKoart = new UIKoartAdmin(tabFolder, SWT.NONE);
                    koartTab.setControl(uiKoart);
                } else
                    uiKoart.update();
                activeTab = koartTab;
            } else if (se.item.equals(koartgrpTab)) {
                if (koartgrpTab.getControl() == null) {
                    uiKoartgrp = new UIKoartgrpAdmin(tabFolder, SWT.NONE);
                    koartgrpTab.setControl(uiKoartgrp);
                } else
                    uiKoartgrp.update();
                activeTab = koartgrpTab;
            }
        }
    }

    @Override
    public void deactivate() {
        UIAbstractAdmin aa = (UIAbstractAdmin) activeTab.getControl();
        aa.unlock();
    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub

    }

}