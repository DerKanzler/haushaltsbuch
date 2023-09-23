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

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.logic.dialogs.LogicBuchungAendern;

/**
 * This class implements the dialog which appears when you want to edit
 * a booking text.
 * 
 * @author pk
 *
 */
public class UIBuchungAendern extends IconAndMessageDialog {

    private Buchung buchung;

    private Text bookingText;
    private Label messagesLabel;

    private LogicBuchungAendern logic = new LogicBuchungAendern();

    /**
     * Constructor: just calls the parent's constructor with the given parameters
     * 
     * @param Shell
     */
    public UIBuchungAendern(Shell parentShell) {
        super(parentShell);
    }

    public Integer open(Buchung b) {
        this.buchung = b;
        return this.open();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.getShell().setText("Buchungstext ändern");
        return createMessageArea(parent);
    }

    @Override
    protected Control createMessageArea(Composite parent) {
        Composite composite = (Composite) super.createMessageArea(parent);

        Composite c = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        c.setLayout(layout);

        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 400;
        gd.heightHint = 22;

        Label questionLabel = new Label(c, SWT.LEFT);
        questionLabel.setLayoutData(gd);
        questionLabel.setText("Bitte geben sie den neuen Buchungstext ein!");

        bookingText = new Text(c, SWT.LEFT | SWT.BORDER);
        bookingText.setLayoutData(gd);
        bookingText.setText(buchung.getBuchtext());
        bookingText.setTextLimit(52);

        messagesLabel = new Label(c, SWT.LEFT);
        messagesLabel.setLayoutData(gd);
        messagesLabel.setText("");

        return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, "Abbrechen", false);
        createButton(parent, IDialogConstants.OK_ID, "Speichern", true);
    }

    @Override
    protected void buttonPressed(int buttonID) {
        if (buttonID == IDialogConstants.OK_ID) {
            // If the new booking text is the same as the old one we just close the dialog
            if (bookingText.getText().equals(buchung.getBuchtext())) {
                buttonID = IDialogConstants.CANCEL_ID;
            }
            // But if not we set the new booking text to be the text of the old booking
            // and try to save it in the database
            else {
                String oldBookingText = buchung.getBuchtext();
                buchung.setBuchtext(bookingText.getText());
                try {
                    logic.editBookingText(buchung);
                } catch (Exception e) {
                    messagesLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
                    messagesLabel.setText(e.getMessage());
                    buchung.setBuchtext(oldBookingText);
                    buttonID = IDialogConstants.ABORT_ID;
                }
            }
        }
        super.buttonPressed(buttonID);
    }

    @Override
    protected Image getImage() {
        return this.getInfoImage();
    }

}