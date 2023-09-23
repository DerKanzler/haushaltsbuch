package at.kanzler.haushaltsbuch.widgets;

import java.util.Locale;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class LocalesCombo {

    private String[] data = new String[2];
    private Combo combo;
    private ComboViewer comboViewer;

    public LocalesCombo(Composite parent, Integer style) {
        combo = new Combo(parent, style);
        comboViewer = new ComboViewer(combo);
        comboViewer.setContentProvider(new LocalesContentProvider());
        data[0] = Locale.GERMAN.getLanguage();
        data[1] = Locale.ENGLISH.getLanguage();
        comboViewer.setInput(data);
    }

    public void setSelectedAccount(Locale l) {
        comboViewer.setSelection(new StructuredSelection(l.getDisplayLanguage()));
    }

    public Locale getSelectedAccount() {
        return new Locale(data[combo.getSelectionIndex()]);
    }

    public Combo getCombo() {
        return this.combo;
    }

    private class LocalesContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object input) throws AssertionError {
            assert input instanceof String[] : "Combo konnte nicht erstellt werden!";
            String[] data = (String[]) input;
            Object[] displayed = new Object[data.length];
            for (int i = 0; i < data.length; i++) {
                Locale l = new Locale(data[i]);
                displayed[i] = l.getDisplayLanguage();
            }
            return displayed;
        }

        public void dispose() {}

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}

    }

}