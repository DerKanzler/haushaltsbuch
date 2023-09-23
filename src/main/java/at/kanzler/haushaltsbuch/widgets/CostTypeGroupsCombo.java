package at.kanzler.haushaltsbuch.widgets;

import java.util.Vector;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import at.kanzler.haushaltsbuch.bean.Kostenartgruppe;

public class CostTypeGroupsCombo {

    private Combo combo;
    private ComboViewer comboViewer;

    public CostTypeGroupsCombo(Composite parent, Integer style) {
        combo = new Combo(parent, style);
        comboViewer = new ComboViewer(combo);
        comboViewer.setContentProvider(new AccountsContentProvider());
    }

    public void update(Vector<Kostenartgruppe> data) {
        comboViewer.setInput(data);
    }

    public Kostenartgruppe getSelectedCostType() {
        return (Kostenartgruppe) comboViewer.getElementAt(combo.getSelectionIndex());
    }

    public void setSelectedCostTypeGroup(Kostenartgruppe koartgrp) {
        if (koartgrp == null) {
            combo.select(0);
        } else
            comboViewer.setSelection(new StructuredSelection(koartgrp));
    }

    public Combo getCombo() {
        return this.combo;
    }

    private class AccountsContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object input) throws AssertionError {
            assert input instanceof Vector<?> : "Combo konnte nicht erstellt werden!";
            Vector<?> data = (Vector<?>) input;
            return data.toArray();
        }

        public void dispose() {}

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}

    }

}