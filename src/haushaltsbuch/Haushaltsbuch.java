package haushaltsbuch;

import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import haushaltsbuch.db.DB;
import haushaltsbuch.db.DBConfiguration;
import haushaltsbuch.exceptions.ConfigurationException;
import haushaltsbuch.exceptions.InstallationException;
import haushaltsbuch.gui.UIHaushaltsbuch;
import haushaltsbuch.logic.LogicMain;

/**
 * Main entry point for the Application. Just calls
 * the methods to open the first window, after it
 * made sure there's a database connection.
 *
 * @author pk
 */
public class Haushaltsbuch {

    private static ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings");

    /**
     * The main method connects to the database and
     * then opens the login window.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (args.length > 0 && !args[0].isEmpty()) {
            LogicMain.instance().setProfile(args[0]);
        } else {
            LogicMain.instance().setProfile(System.getProperty("user.name"));
        }
        Display.setAppName(res.getString("Application"));
        Display display = new Display();
        try {
            if (DB.instance().connect()) {
                new UIHaushaltsbuch();
            } else {
                Shell shell = new Shell(display);

                MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
                mb.setText(res.getString("Application"));
                mb.setMessage(res.getString("ApplicationRunning"));
                if (mb.open() == SWT.OK) {
                    shell.dispose();
                    display.dispose();
                    LogicMain.instance().exit();
                }
            }
        } catch (InstallationException e) {
            Shell shell = new Shell(display);

            MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
            mb.setText(res.getString("Application"));
            mb.setMessage(res.getString("InstallationException"));
            if (mb.open() == SWT.OK) {
                shell.dispose();
                display.dispose();
                LogicMain.instance().exit();
            }
        } catch (ConfigurationException e) {
            Shell shell = new Shell(display);

            FileDialog dbDialog = new FileDialog(shell, SWT.OPEN);
            dbDialog.setFilterNames(new String[] { res.getString("H2Databases") });
            dbDialog.setFilterExtensions(new String[] { "*.db" });
            dbDialog.setFilterPath("~");
            String file = dbDialog.open();
            if (file != null) {
                new DBConfiguration().writeConfiguration(file);
                shell.dispose();
                display.dispose();
                main(new String[] {});
            }
        }
    }

}