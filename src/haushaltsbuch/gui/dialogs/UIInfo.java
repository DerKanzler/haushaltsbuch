package haushaltsbuch.gui.dialogs;

import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;

public class UIInfo extends Dialog {

    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    public UIInfo(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite c = (Composite) super.createDialogArea(parent);
        c.getShell().setText(res.getString("About"));
        c.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        GridLayout layout = new GridLayout(2, false);
        c.setLayout(layout);

        Label aboutImage = new Label(c, SWT.CENTER);
        GridData aboutImageGD = new GridData(
                GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER | GridData.FILL_BOTH);
        aboutImageGD.verticalSpan = 5;
        aboutImageGD.minimumWidth = 180;
        aboutImageGD.minimumHeight = 180;
        aboutImage.setLayoutData(aboutImageGD);
        aboutImage.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        aboutImage.setImage(new Image(Display.getDefault(),
                this.getClass().getClassLoader().getResourceAsStream("images/about.png")));

        Label applicationTitle = new Label(c, SWT.LEFT);
        applicationTitle.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_END | GridData.FILL_BOTH));
        GUITools.setBoldFont(applicationTitle);
        applicationTitle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        applicationTitle.setText(res.getString("Application"));

        Label versionTitle = new Label(c, SWT.LEFT);
        versionTitle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        versionTitle.setText("Version " + UIInfo.class.getPackage().getImplementationVersion());

        Label developerTitle = new Label(c, SWT.LEFT | SWT.WRAP);
        developerTitle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        developerTitle.setText("Originally developed by Helmut Kanzler\nPorted and modified by Peter Kanzler");

        Label copyrightTitle = new Label(c, SWT.LEFT);
        copyrightTitle.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        copyrightTitle.setText("Copyright © 1995-" + new GregorianCalendar().get(GregorianCalendar.YEAR));

        Link webLink = new Link(c, SWT.LEFT);
        webLink.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
        webLink.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
        webLink.setToolTipText("http://derkanzler.ath.cx");
        webLink.setText("Visit the <a href=\"http://derkanzler.ath.cx\">Website</a>");
        webLink.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                Program.launch(e.text);
            }

        });

        return c;
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        return parent;
    }

}