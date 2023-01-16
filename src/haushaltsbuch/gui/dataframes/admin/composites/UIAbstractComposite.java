package haushaltsbuch.gui.dataframes.admin.composites;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class UIAbstractComposite extends Composite {

    protected Composite editComposite, viewComposite;

    public UIAbstractComposite(Composite c, int style) {
        super(c, style);
    }

    public abstract Boolean isNew();

    public abstract void setNew(Boolean b);

    public abstract Boolean isUsed();

    public abstract void setUsed(Boolean b);

    public abstract void lock();

    public abstract void unlock();

    public abstract void discard();

    protected void setBackgroundColor(Color color) {
        Control[] children;
        if (isUsed()) {
            children = editComposite.getChildren();
            editComposite.setBackground(color);
        } else {
            children = viewComposite.getChildren();
            viewComposite.setBackground(color);
        }
        for (Control ctrl : children) {
            colorControl(ctrl, color);
        }
    }

    private void colorControl(Control ctrl, Color color) {
        if (ctrl instanceof Label || (ctrl instanceof Composite && !(ctrl instanceof Combo))
                || ctrl instanceof Button) {
            ctrl.setBackground(color);
            if (ctrl instanceof Composite && !(ctrl instanceof Combo)) {
                for (Control c : ((Composite) ctrl).getChildren()) {
                    colorControl(c, color);
                }
            }
        }
    }

}