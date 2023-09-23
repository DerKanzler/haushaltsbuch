package at.kanzler.haushaltsbuch.gui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.logic.dialogs.LogicBenutzer;
import at.kanzler.haushaltsbuch.widgets.LocalesCombo;

/**
 * This class implements the dialog which appears when you want to edit
 * a booking text.
 * 
 * @author pk
 *
 */
public class UIUserDialog extends IconAndMessageDialog {

    private Text nameText, password1Text, password2Text;
    private Label messageLabel;
    private LocalesCombo formatCombo;

    /**
     * Constructor: just calls the parent's constructor with the given parameters
     * 
     * @param Shell
     */
    public UIUserDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.getShell().setText("Benutzer anlegen");
        return createMessageArea(parent);
    }

    @Override
    protected Control createMessageArea(Composite parent) {
        Composite composite = (Composite) super.createMessageArea(parent);

        Composite c = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        c.setLayout(layout);

        Label questionLabel = new Label(c, SWT.LEFT);
        questionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        questionLabel.setText("Bitte geben sie ihren Name, das Passwort\nund ihre gewünschte Sprache ein!");

        Label nameLabel = new Label(c, SWT.LEFT);
        nameLabel.setText("Name");
        nameLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText = new Text(c, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.setTextLimit(32);

        Label passwordLabel = new Label(c, SWT.LEFT);
        passwordLabel.setText("Passwort");
        passwordLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        password1Text = new Text(c, SWT.PASSWORD | SWT.SINGLE | SWT.BORDER | SWT.LEFT);
        password1Text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        password1Text.setTextLimit(16);
        password2Text = new Text(c, SWT.PASSWORD | SWT.SINGLE | SWT.BORDER | SWT.LEFT);
        password2Text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        password2Text.setTextLimit(16);

        Label formatLabel = new Label(c, SWT.LEFT);
        formatLabel.setText("Sprache");
        formatLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        formatCombo = new LocalesCombo(c, SWT.READ_ONLY);
        formatCombo.getCombo().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        messageLabel = new Label(c, SWT.LEFT);
        messageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        messageLabel.setText("");

        return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "Speichern", true);
    }

    @Override
    protected void buttonPressed(int buttonID) {
        if (buttonID == IDialogConstants.OK_ID) {
            try {
                messageLabel.setText("");
                Benutzer b = readFields();
                LogicBenutzer logic = new LogicBenutzer();
                if (!logic.save(b)) {
                    buttonID = IDialogConstants.ABORT_ID;
                }
            } catch (RuntimeException re) {
                messageLabel.setText(re.getMessage());
                messageLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                buttonID = IDialogConstants.ABORT_ID;
            }
        }
        super.buttonPressed(buttonID);
    }

    private Benutzer readFields() throws RuntimeException {
        Benutzer b = new Benutzer();
        if (nameText.getText().length() > 0) {
            b.setName(nameText.getText());
        } else
            throw new RuntimeException("Name muss angegeben werden!");
        if (password1Text.getText().length() > 0 && password2Text.getText().length() > 0) {
            if (password1Text.getText().equals(password2Text.getText())) {
                b.setPasswort(password1Text.getText().toCharArray());
            } else
                throw new RuntimeException("Das Passwort muss übereinstimmen!");
        } else
            throw new RuntimeException("Das Passwort muss zweimal angegeben werden!");
        if (formatCombo.getSelectedAccount() != null) {
            b.setFormat(formatCombo.getSelectedAccount());
        } else
            throw new RuntimeException("Das Format für das Datum, Beträge, etc. muss angegeben werden!");
        return b;
    }

    @Override
    protected Image getImage() {
        return getQuestionImage();
    }

}