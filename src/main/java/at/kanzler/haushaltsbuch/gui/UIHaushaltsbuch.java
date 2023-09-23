package at.kanzler.haushaltsbuch.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import at.kanzler.haushaltsbuch.bean.Benutzer;
import at.kanzler.haushaltsbuch.conf.HB;
import at.kanzler.haushaltsbuch.dao.BenutzerDAO;
import at.kanzler.haushaltsbuch.gui.dialogs.UIEinstellungen;
import at.kanzler.haushaltsbuch.gui.dialogs.UIInfo;
import at.kanzler.haushaltsbuch.gui.dialogs.UIUserDialog;
import at.kanzler.haushaltsbuch.logic.LogicMain;
import at.kanzler.haushaltsbuch.util.GUITools;

public class UIHaushaltsbuch {

    private Shell shell;

    private UIMainFrame mainFrame;
    private UIProgressFrame progressFrame;

    private Menu menu, applicationMenu, toolsMenu;
    private MenuItem applicationMenuItem, applicationCloseItem, toolsMenuItem, toolsBackupItem, toolsRestoreItem,
            toolsSettingsItem, aboutItem;

    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings");
    private LogicMain logic = LogicMain.instance();

    public UIHaushaltsbuch() {
        if (!logic.userAvailable()) {
            createFrame();
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), false);
            if (new UIUserDialog(shell).open() == IDialogConstants.OK_ID) {
                BenutzerDAO.instance().clear();
                reset();
                createMenuBar();
                revealFrame();
            } else
                exit();
        } else if (new UILoginFrame().open()) {
            createFrame();
            reset();
            createMenuBar();
            revealFrame();
        }
    }

    public void reset() {
        if (mainFrame != null && !mainFrame.isDisposed())
            mainFrame.dispose();
        if (progressFrame != null && !progressFrame.isDisposed())
            progressFrame.dispose();
        res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings", logic.getUser().getFormat());
        shell.setText(res.getString("Application") + " " + logic.getUser().getName());
        mainFrame = new UIMainFrame(shell, SWT.NONE, this);
        shell.layout();
    }

    public void exit() {
        if (checkState())
            shell.dispose();
    }

    public Boolean checkState() {
        if (shell.getModified()) {
            MessageBox box = new MessageBox(shell,
                    SWT.ICON_QUESTION | SWT.PRIMARY_MODAL | SWT.YES | SWT.NO | SWT.SHEET);
            box.setMessage(res.getString("Continue"));
            return box.open() == SWT.YES;
        } else
            return true;
    }

    /**
     * Creates the frame and sets the application name as well as the minimum size.
     * The shell created here is more or less the mother of all the controls and
     * widgets.
     */
    private void createFrame() {
        shell = new Shell(Display.getDefault());
        shell.setMinimumSize(1000, 750);
        shell.setText(res.getString("Application"));
        shell.setModified(false);

        shell.addShellListener(new ShellAdapter() {

            public void shellClosed(ShellEvent se) {
                se.doit = checkState();
            }
        });

        shell.setLayout(new FillLayout(SWT.VERTICAL));
    }

    /**
     * Finally opens the shell, hence opens the frame. Runs as long as the shell is
     * disposed. If it is disposed the application is exited and the database
     * connection is disconnected.
     */
    private void revealFrame() {
        shell.open();
        while (shell != null && shell.isDisposed() == false) {
            Display display = Display.getDefault();
            if (display != null && display.readAndDispatch() == false)
                display.sleep();
        }
        Display.getDefault().dispose();
        LogicMain.instance().exit();
    }

    /**
     * Creates the menubar with the main entry points.
     */
    private void createMenuBar() {
        menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);
        if (GUITools.isMac()) {
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), true);
            try {
                // new SparkleActivator().start();
            } catch (Exception e) {
                // do nothing
            }
            MenuItem pref = GUITools.getItem(Display.getDefault().getSystemMenu(), SWT.ID_PREFERENCES);
            pref.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    new PreferencesAction().run();
                }

            });
            MenuItem about = GUITools.getItem(Display.getDefault().getSystemMenu(), SWT.ID_ABOUT);
            about.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    new AboutAction().run();
                }

            });
        } else {
            applicationMenuItem = new MenuItem(menu, SWT.CASCADE);
            applicationMenuItem.setText(res.getString("Application"));
            createApplicationMenu();
        }
        toolsMenuItem = new MenuItem(menu, SWT.CASCADE);
        toolsMenuItem.setText(res.getString("Tools"));
        createToolsMenu();
    }

    /**
     * Creates the menu for the 'Application' menubar entry
     */
    private void createApplicationMenu() {
        applicationMenu = new Menu(shell, SWT.DROP_DOWN);
        applicationMenuItem.setMenu(applicationMenu);

        aboutItem = new MenuItem(applicationMenu, SWT.PUSH);
        aboutItem.setText(res.getString("About"));
        aboutItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                new AboutAction().run();
            }

        });

        new Separator().fill(applicationMenu, 1);

        applicationCloseItem = new MenuItem(applicationMenu, SWT.PUSH);
        applicationCloseItem.setText(res.getString("Quit"));
        applicationCloseItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                exit();
            }

        });
    }

    /**
     * Creates the menu for the 'Edit' menubar entry
     */
    private void createToolsMenu() {
        toolsMenu = new Menu(shell, SWT.DROP_DOWN);
        toolsMenuItem.setMenu(toolsMenu);

        toolsBackupItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsBackupItem.setText(res.getString("Backup"));
        toolsBackupItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (checkState()) {
                    mainFrame.getDataFrame().deactivateDataFrame();
                    shell.setModified(false);

                    FileDialog backupDialog = new FileDialog(shell, SWT.SAVE | SWT.SHEET);
                    backupDialog.setFilterNames(new String[] { res.getString("ZipFiles") });
                    backupDialog.setFilterExtensions(new String[] { "*.zip" });
                    backupDialog.setFileName(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".zip");
                    backupDialog.setOverwrite(true);
                    final String file = backupDialog.open();

                    if (file != null) {
                        mainFrame.dispose();
                        progressFrame = new UIProgressFrame(shell, HB.BACKUP_DB);
                        toolsMenu.setEnabled(false);
                        if (!GUITools.isMac())
                            applicationMenu.setEnabled(false);
                        shell.layout();

                        Runnable longJob = new Runnable() {

                            boolean done = false;

                            public void run() {
                                Thread thread = new Thread(new Runnable() {

                                    Boolean b;

                                    public void run() {
                                        try {
                                            b = logic.backupDatabase(file);
                                        } catch (Exception e) {
                                            b = false;
                                        }
                                        Display.getDefault().asyncExec(new Runnable() {

                                            public void run() {
                                                MessageBox mb = new MessageBox(shell,
                                                        SWT.OK | SWT.ICON_INFORMATION | SWT.SHEET);
                                                if (b)
                                                    mb.setMessage(res.getString("BackupSuccessfull"));
                                                else
                                                    mb.setMessage(res.getString("BackupFailed"));
                                                if (mb.open() == SWT.OK) {
                                                    reset();
                                                    toolsMenu.setEnabled(true);
                                                    if (!GUITools.isMac())
                                                        applicationMenu.setEnabled(true);
                                                }
                                            }
                                        });
                                        done = true;
                                    }
                                });
                                thread.start();
                                while (!done && !shell.isDisposed()) {
                                    if (!Display.getDefault().readAndDispatch())
                                        Display.getDefault().sleep();
                                }
                            }
                        };
                        BusyIndicator.showWhile(Display.getDefault(), longJob);
                    }
                }
            }
        });

        toolsRestoreItem = new MenuItem(toolsMenu, SWT.PUSH);
        toolsRestoreItem.setText(res.getString("Restore"));
        toolsRestoreItem.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                if (checkState()) {
                    mainFrame.getDataFrame().deactivateDataFrame();
                    shell.setModified(false);

                    FileDialog restoreDialog = new FileDialog(shell, SWT.OPEN | SWT.SHEET);
                    restoreDialog.setFilterNames(new String[] { res.getString("ZipFiles") });
                    restoreDialog.setFilterExtensions(new String[] { "*.zip" });
                    final String file = restoreDialog.open();

                    if (file != null) {
                        mainFrame.dispose();
                        progressFrame = new UIProgressFrame(shell, HB.RESTORE_DB);
                        toolsMenu.setEnabled(false);
                        if (!GUITools.isMac())
                            applicationMenu.setEnabled(false);
                        shell.layout();

                        Runnable longJob = new Runnable() {

                            boolean done = false;

                            public void run() {
                                Thread thread = new Thread(new Runnable() {

                                    Boolean b;

                                    public void run() {
                                        try {
                                            b = logic.restoreDatabase(file);
                                        } catch (Exception e) {
                                            b = false;
                                        }
                                        Display.getDefault().asyncExec(new Runnable() {

                                            public void run() {
                                                MessageBox mb = new MessageBox(shell,
                                                        SWT.OK | SWT.ICON_INFORMATION | SWT.SHEET);
                                                if (b)
                                                    mb.setMessage(res.getString("RestoreSuccessfull"));
                                                else
                                                    mb.setMessage(res.getString("RestoreFailed"));
                                                if (mb.open() == SWT.OK) {
                                                    reset();
                                                    toolsMenu.setEnabled(true);
                                                    if (!GUITools.isMac())
                                                        applicationMenu.setEnabled(true);
                                                }
                                            }
                                        });
                                        done = true;
                                    }
                                });
                                thread.start();
                                while (!done && !shell.isDisposed()) {
                                    if (!Display.getDefault().readAndDispatch())
                                        Display.getDefault().sleep();
                                }
                            }
                        };
                        BusyIndicator.showWhile(Display.getDefault(), longJob);
                    }
                }
            }
        });

        if (!GUITools.isMac()) {
            new Separator().fill(toolsMenu, 2);

            toolsSettingsItem = new MenuItem(toolsMenu, SWT.PUSH);
            toolsSettingsItem.setText(res.getString("Preferences"));
            toolsSettingsItem.addListener(SWT.Selection, new Listener() {

                public void handleEvent(Event e) {
                    new PreferencesAction().run();
                }

            });
        }
    }

    private class AboutAction extends Action {

        public void run() {
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), false);
            UIInfo info = new UIInfo(Display.getDefault().getActiveShell());
            info.open();
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), true);
        }

    }

    private class PreferencesAction extends Action {

        public void run() {
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), false);
            Benutzer b = BenutzerDAO.instance().getUser();
            UIEinstellungen pop = new UIEinstellungen(Display.getDefault().getActiveShell());
            if (pop.open(b) == IDialogConstants.OK_ID) {
                reset();
            }
            GUITools.enableMenu(Display.getDefault().getSystemMenu(), true);
        }

    }

}