package at.kanzler.haushaltsbuch.gui.dialogs;

import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.dao.BenutzerDAO;
import at.kanzler.haushaltsbuch.dao.KontoDAO;
import at.kanzler.haushaltsbuch.db.DBConfiguration;
import at.kanzler.haushaltsbuch.logic.LogicMain;
import at.kanzler.haushaltsbuch.widgets.AccountsCombo;
import at.kanzler.haushaltsbuch.widgets.LocalesCombo;

public class UIEinstellungen extends Dialog {

    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    private AccountsCombo accountOneCombo, accountTwoCombo, accountThreeCombo;
    private LocalesCombo userLocaleCombo;
    private Text dblocationText;
    private Composite generalContent, localizationContent, databaseContent;

    private Benutzer benutzer;

    public UIEinstellungen(Shell parentShell) {
        super(parentShell);
    }

    public Integer open(Benutzer b) {
        this.benutzer = b;
        return this.open();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Image genIcon = new Image(composite.getDisplay(),
                composite.getClass().getClassLoader().getResourceAsStream("images/general.png"));
        Image locIcon = new Image(composite.getDisplay(),
                composite.getClass().getClassLoader().getResourceAsStream("images/localization.png"));
        Image dbIcon = new Image(composite.getDisplay(),
                composite.getClass().getClassLoader().getResourceAsStream("images/database.png"));

        GridLayout gl = new GridLayout(1, false);
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        composite.setLayout(gl);

        final ToolBar toolbar = new ToolBar(composite, SWT.HORIZONTAL | SWT.FLAT);
        toolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final Composite content = new Composite(composite, SWT.NONE);
        content.setLayoutData(new GridData(GridData.FILL_BOTH));
        final StackLayout sl = new StackLayout();
        content.setLayout(sl);

        ToolItem generalTI = new ToolItem(toolbar, SWT.PUSH);
        generalTI.setImage(genIcon);
        generalTI.setText("Basics");
        generalTI.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                if (generalContent == null)
                    generalContent = createGeneralContent(content);
                sl.topControl = generalContent;
                content.layout();
            }

        });

        ToolItem localizationTI = new ToolItem(toolbar, SWT.PUSH);
        localizationTI.setImage(locIcon);
        localizationTI.setText("Internalization");
        localizationTI.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                if (localizationContent == null)
                    localizationContent = createLocalizationContent(content);
                sl.topControl = localizationContent;
                content.layout();
            }

        });

        ToolItem databaseTI = new ToolItem(toolbar, SWT.PUSH);
        databaseTI.setImage(dbIcon);
        databaseTI.setText("Database");
        databaseTI.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                if (databaseContent == null)
                    databaseContent = createDatabaseContent(content);
                sl.topControl = databaseContent;
                content.layout();
            }

        });

        toolbar.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

        if (generalContent == null)
            generalContent = createGeneralContent(content);
        sl.topControl = generalContent;

        return composite;
    }

    @Override
    protected void buttonPressed(int buttonID) {
        if (buttonID == IDialogConstants.OK_ID) {
            benutzer.setKonto1(accountOneCombo.getSelectedAccount());
            benutzer.setKonto2(accountTwoCombo.getSelectedAccount());
            benutzer.setKonto3(accountThreeCombo.getSelectedAccount());

            if (userLocaleCombo != null) {
                BenutzerDAO.instance().getUser().setFormat(userLocaleCombo.getSelectedAccount());
            }

            BenutzerDAO.instance().saveOrUpdate(benutzer);

            if (dblocationText != null) {
                new DBConfiguration().writeConfiguration(dblocationText.getText());
            }
        }
        super.buttonPressed(buttonID);
    }

    @Override
    protected Control createButtonBar(Composite parent) {
        Control c = super.createButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setText(res.getString("Save"));
        getButton(IDialogConstants.CANCEL_ID).setText(res.getString("Abort"));
        return c;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(360, 280);
    }

    @Override
    public void create() {
        super.create();
        getShell().setText(res.getString("Preferences"));
        getButton(IDialogConstants.OK_ID).setFocus();
    }

    private Composite createGeneralContent(Composite parent) {
        Composite c = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, true);
        layout.marginWidth = 20;
        layout.marginTop = 20;
        c.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;

        Label accountOneLabel = new Label(c, SWT.LEFT);
        accountOneLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        accountOneLabel.setText(res.getString("Account").concat(" 1"));

        accountOneCombo = new AccountsCombo(c, SWT.READ_ONLY);
        accountOneCombo.getCombo().setLayoutData(gd);
        accountOneCombo.update(KontoDAO.instance().getAllValid());
        accountOneCombo.setSelectedAccount(benutzer.getKonto1());

        Label accountTwoLabel = new Label(c, SWT.LEFT);
        accountTwoLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        accountTwoLabel.setText(res.getString("Account").concat(" 2"));

        accountTwoCombo = new AccountsCombo(c, SWT.READ_ONLY);
        accountTwoCombo.getCombo().setLayoutData(gd);
        accountTwoCombo.update(KontoDAO.instance().getAllValid());
        accountTwoCombo.setSelectedAccount(benutzer.getKonto2());

        Label accountThreeLabel = new Label(c, SWT.LEFT);
        accountThreeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        accountThreeLabel.setText(res.getString("Account").concat(" 3"));

        accountThreeCombo = new AccountsCombo(c, SWT.READ_ONLY);
        accountThreeCombo.getCombo().setLayoutData(gd);
        accountThreeCombo.update(KontoDAO.instance().getAllValid());
        accountThreeCombo.setSelectedAccount(benutzer.getKonto3());

        return c;
    }

    private Composite createLocalizationContent(Composite parent) {
        Composite c = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, true);
        layout.marginWidth = 20;
        layout.marginTop = 20;
        c.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;

        Label userLocaleLabel = new Label(c, SWT.LEFT);
        userLocaleLabel.setText(res.getString("Language"));

        userLocaleCombo = new LocalesCombo(c, SWT.READ_ONLY);
        userLocaleCombo.getCombo().setLayoutData(gd);
        userLocaleCombo.setSelectedAccount(BenutzerDAO.instance().getUser().getFormat());

        return c;
    }

    private Composite createDatabaseContent(Composite parent) {
        Composite c = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3, true);
        layout.marginWidth = 20;
        layout.marginTop = 20;
        c.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;

        DBConfiguration dbconfig = new DBConfiguration();

        dblocationText = new Text(c, SWT.LEFT | SWT.BORDER);
        try {
            dblocationText.setText(dbconfig.getDBPath().concat(dbconfig.getDBName().concat(".h2.db")));
        } catch (Exception e) {
            dblocationText.setText("");
        }
        dblocationText.setLayoutData(gd);

        Button b = new Button(c, SWT.PUSH);
        b.setText("Choose...");
        b.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                FileDialog dbDialog = new FileDialog(getShell(), SWT.OPEN | SWT.SHEET);
                dbDialog.setFilterNames(new String[] { res.getString("H2Databases") });
                dbDialog.setFilterExtensions(new String[] { "*.db" });
                dbDialog.setFilterPath(dblocationText.getText());
                String file = dbDialog.open();
                if (file != null) {
                    dblocationText.setText(file);
                }
            }

        });

        return c;
    }

}