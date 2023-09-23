package at.kanzler.haushaltsbuch.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class GUITools {

    public static Boolean isMac() {
        if (System.getProperty("os.name").equals("Mac OS X")) {
            return true;
        } else
            return false;
    }

    public static MenuItem getItem(Menu menu, int id) {
        MenuItem[] items = menu.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getID() == id)
                return items[i];
        }
        return null;
    }

    public static void enableMenu(Menu menu, Boolean bool) {
        if (isMac()) {
            getItem(menu, SWT.ID_ABOUT).setEnabled(bool);
            getItem(menu, SWT.ID_PREFERENCES).setEnabled(bool);
            getItem(menu, SWT.ID_QUIT).setEnabled(bool);
        }
    }

    public static void setButtonLayout(Button b, Integer span) {
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END);
        data.horizontalSpan = span;
        if (span > 1)
            data.grabExcessHorizontalSpace = true;
        Point minSize = b.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
        data.widthHint = Math.max(120, minSize.x);
        b.setLayoutData(data);
    }

    public static void setBoldFont(Control c) {
        FontData[] fontData = c.getFont().getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setStyle(SWT.BOLD);
        }
        c.setFont(new Font(Display.getDefault(), fontData));
    }

    public static void setSmallFont(Control c) {
        FontData[] fontData = c.getFont().getFontData();
        for (int i = 0; i < fontData.length; i++) {
            fontData[i].setHeight(fontData[i].getHeight() - 2);
        }
        c.setFont(new Font(Display.getDefault(), fontData));
    }

    public static Color getSelectionColor() {
        return new Color(Display.getDefault(), 130, 150, 180);
    }

    public static Color getNonSelectionColor() {
        return new Color(Display.getDefault(), 210, 220, 230);
    }

}