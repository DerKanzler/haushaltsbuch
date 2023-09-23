package at.kanzler.haushaltsbuch.widgets;

import java.util.Vector;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import at.kanzler.haushaltsbuch.bean.Konto;

public class AccountsCombo {

    private Combo combo;
    private ComboViewer comboViewer;

    public AccountsCombo(Composite parent, Integer style) {
        combo = new Combo(parent, style);
        comboViewer = new ComboViewer(combo);
        comboViewer.setContentProvider(new AccountsContentProvider());

    }

    public void update(Vector<Konto> kontenVector) {
        comboViewer.setInput(kontenVector);
    }

    public void setSelectedAccount(Konto k) {
        if (k == null) {
            combo.select(0);
        } else
            comboViewer.setSelection(new StructuredSelection(k));
    }

    public Konto getSelectedAccount() {
        if (combo.getSelectionIndex() == 0) {
            return null;
        } else
            return (Konto) comboViewer.getElementAt(combo.getSelectionIndex());
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