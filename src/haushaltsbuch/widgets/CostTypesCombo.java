package haushaltsbuch.widgets;

import java.util.Vector;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import haushaltsbuch.bean.Kostenart;

public class CostTypesCombo {

    private Combo combo;
    private ComboViewer comboViewer;

    public CostTypesCombo(Composite parent, Integer style) {
        combo = new Combo(parent, style);
        comboViewer = new ComboViewer(combo);
        comboViewer.setContentProvider(new AccountsContentProvider());
    }

    public void update(Vector<Kostenart> data) {
        comboViewer.setInput(data);
    }

    public Kostenart getSelectedCostType() {
        if (combo.getSelectionIndex() == 0) {
            return null;
        } else
            return (Kostenart) comboViewer.getElementAt(combo.getSelectionIndex());
    }

    public Combo getCombo() {
        return this.combo;
    }

    private class AccountsContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object input) throws AssertionError {
            assert input instanceof Vector<?> : "Combo konnte nicht erstellt werden!";
            Vector<?> data = (Vector<?>) input;
            Object[] displayed = new Object[data.size() + 1];
            displayed[0] = "";
            for (int i = 0; i < data.size(); i++) {
                displayed[i + 1] = data.elementAt(i);
            }
            return displayed;
        }

        public void dispose() {}

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}

    }

}