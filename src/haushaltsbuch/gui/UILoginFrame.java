package haushaltsbuch.gui;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import haushaltsbuch.exceptions.DatabaseException;
import haushaltsbuch.exceptions.LoginException;
import haushaltsbuch.logic.LogicLogin;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;

public class UILoginFrame {

    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings");
    private LogicLogin logic = new LogicLogin();

    private Text passwordText;
    private Label messageLabel;

    private Boolean loggedIn = false;

    public Boolean open() {
        final Shell shell = new Shell(Display.getDefault(), SWT.ON_TOP);
        shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
        shell.setLayout(new FillLayout(SWT.VERTICAL));

        GUITools.enableMenu(Display.getDefault().getSystemMenu(), false);

        Composite composite = new Composite(shell, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        composite.setLayout(layout);

        Image img = new Image(shell.getDisplay(),
                shell.getClass().getClassLoader().getResourceAsStream("images/img_login.png"));
        composite.setBackgroundImage(img);

        Label passwordLabel = new Label(composite, SWT.RIGHT);
        GridData passwordLabelGD = new GridData(
                GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_END);
        passwordLabelGD.horizontalSpan = 3;
        passwordLabel.setLayoutData(passwordLabelGD);
        passwordLabel.setText(res.getString("LoginText"));

        passwordText = new Text(composite, SWT.PASSWORD | SWT.BORDER | SWT.SINGLE);
        GridData passwordTextGD = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
        passwordTextGD.horizontalSpan = 3;
        passwordTextGD.widthHint = 160;
        passwordText.setLayoutData(passwordTextGD);
        passwordText.setTextLimit(16);

        messageLabel = new Label(composite, SWT.RIGHT);
        GridData messageLabelGD = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END);
        messageLabelGD.horizontalSpan = 3;
        messageLabelGD.widthHint = 240;
        messageLabel.setLayoutData(messageLabelGD);
        messageLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

        Button exitButton = new Button(composite, SWT.PUSH);
        exitButton.setText(res.getString("Quit"));
        GUITools.setButtonLayout(exitButton, 1);
        exitButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                shell.dispose();
            }

        });

        Button loginButton = new Button(composite, SWT.PUSH);
        loginButton.setText(res.getString("Login"));
        GUITools.setButtonLayout(loginButton, 2);
        loginButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event arg0) {
                try {
                    if (passwordText.getText().length() > 0) {
                        if (logic.login(passwordText.getText())) {
                            shell.setDefaultButton(null);
                            loggedIn = true;
                            shell.dispose();
                        } else {
                            passwordText.setText("");
                            passwordText.setFocus();
                            messageLabel.setText(res.getString("WrongPasswd"));
                        }
                    } else {
                        passwordText.setFocus();
                        messageLabel.setText(res.getString("EnterPasswd"));
                    }

                } catch (LoginException le) {
                    MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING | SWT.SHEET);
                    mb.setText(res.getString("Application"));
                    mb.setMessage(res.getString("3TriesPasswd"));
                    if (mb.open() == SWT.OK) {
                        shell.dispose();
                    }
                } catch (DatabaseException de) {
                    messageLabel.setText(res.getString("DBError"));
                }
            }

        });

        passwordText.setFocus();
        shell.setDefaultButton(loginButton);

        // Adding ability to move shell around
        Listener l = new Listener() {

            Point origin;

            public void handleEvent(Event e) {
                switch (e.type) {
                    case SWT.MouseDown:
                        origin = new Point(e.x, e.y);
                        break;
                    case SWT.MouseUp:
                        origin = null;
                        break;
                    case SWT.MouseMove:
                        if (origin != null) {
                            Point p = shell.getDisplay().map(shell, null, e.x, e.y);
                            shell.setLocation(p.x - origin.x, p.y - origin.y);
                        }
                        break;
                }
            }
        };

        composite.addListener(SWT.MouseDown, l);
        composite.addListener(SWT.MouseUp, l);
        composite.addListener(SWT.MouseMove, l);

        shell.setSize(440, 220);
        Monitor primary = Display.getDefault().getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();

        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y / 2 + (rect.height);

        shell.setLocation(x, y);
        shell.open();

        while (!shell.isDisposed()) {
            if (!Display.getDefault().readAndDispatch())
                Display.getDefault().sleep();
        }
        img.dispose();
        if (!loggedIn) {
            Display.getDefault().dispose();
            LogicMain.instance().exit();
        }
        return loggedIn;
    }

}