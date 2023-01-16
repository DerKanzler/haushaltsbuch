package haushaltsbuch.gui;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import haushaltsbuch.logic.LogicMain;

/**
 * 
 * This is the main window of the application.
 * 
 * It displays today's date, contains the fastsearch field, the
 * control frame and the dataframe.
 * 
 * @author pk
 *
 */
public class UIProgressFrame extends Composite {

    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    public UIProgressFrame(Composite parent, String message) {
        super(parent, SWT.NONE);

        GridLayout layout = new GridLayout(1, false);
        this.setLayout(layout);

        Label l = new Label(this, SWT.CENTER);
        GridData lGD = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_END);
        lGD.grabExcessHorizontalSpace = true;
        lGD.grabExcessVerticalSpace = true;
        l.setLayoutData(lGD);
        l.setText(res.getString(message));

        ProgressBar pb = new ProgressBar(this, SWT.SMOOTH | SWT.HORIZONTAL | SWT.INDETERMINATE);
        GridData pbGD = new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING);
        pbGD.grabExcessHorizontalSpace = true;
        pbGD.grabExcessVerticalSpace = true;
        pbGD.widthHint = 250;
        pb.setLayoutData(pbGD);
    }

}