package haushaltsbuch.gui.dialogs;

import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.logic.dialogs.LogicMonatsBuchungen;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;
import haushaltsbuch.widgets.BookingsTable;

public class UIMonatsBuchungen extends Dialog {

    private Kostenartsaldo koartsald;
    private BookingsTable resultTable;
    private Label koartkubezLabel, koartbezLabel, saldoLabel, koartgrpbezLabel, koartgrpkatLabel, koartgrpartLabel,
            monthLabel;

    private LogicMonatsBuchungen logic = new LogicMonatsBuchungen();
    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    public UIMonatsBuchungen(Shell parentShell) {
        super(parentShell);
    }

    public Integer open(Kostenartsaldo k) {
        this.koartsald = k;
        return this.open();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.getShell().setText("Buchungen zur Kostenart");
        Composite composite = (Composite) super.createDialogArea(parent);

        GridLayout layout = new GridLayout(8, false);
        layout.marginTop = 10;
        layout.marginWidth = 20;
        composite.setLayout(layout);

        koartbezLabel = new Label(composite, SWT.LEFT);
        GUITools.setBoldFont(koartbezLabel);
        GridData koartbezLabelGD = new GridData(GridData.FILL_HORIZONTAL);
        koartbezLabelGD.horizontalSpan = 6;
        koartbezLabel.setLayoutData(koartbezLabelGD);

        new Label(composite, SWT.LEFT).setText(res.getString("Month"));
        monthLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(monthLabel);
        monthLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(composite, SWT.LEFT).setText(res.getString("ShortTitle"));
        koartkubezLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(koartkubezLabel);
        koartkubezLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(composite, SWT.LEFT).setText(res.getString("Costtypegroup"));
        koartgrpbezLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(koartgrpbezLabel);
        GridData koartgrpbezLabelGD = new GridData(GridData.FILL_HORIZONTAL);
        koartgrpbezLabelGD.horizontalSpan = 4;
        koartgrpbezLabel.setLayoutData(koartgrpbezLabelGD);

        new Label(composite, SWT.LEFT).setText(res.getString("Balance"));
        saldoLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(saldoLabel);
        saldoLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(composite, SWT.LEFT).setText(res.getString("Category"));
        koartgrpkatLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(koartgrpkatLabel);
        koartgrpkatLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(composite, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(composite, SWT.LEFT).setText(res.getString("Type"));
        koartgrpartLabel = new Label(composite, SWT.RIGHT);
        GUITools.setBoldFont(koartgrpartLabel);
        koartgrpartLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        resultTable = new BookingsTable(composite, 8);

        fillFields(koartsald);

        return composite;
    }

    @Override
    protected void initializeBounds() {
        Point p = getInitialLocation(new Point(800, 500));
        getShell().setBounds(p.x, p.y, 800, 500);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, "Schließen", true);
    }

    /**
     * Searches for the bookings, doesn't read the fields first. Uses the given
     * SuchBuchung object.
     * 
     * @param SearchBuchung
     */
    private void fillFields(Kostenartsaldo koartsald) {
        try {
            SearchBuchung sb = new SearchBuchung();
            sb.setKoart(koartsald.getKoart());
            sb.setFromDate(koartsald.getKoartsalddat());
            sb.setToDate(koartsald.getKoartsalddat().plusMonths(1).minusDays(1));

            Vector<Buchung> data = logic.search(sb);
            resultTable.update(data);

            monthLabel.setText(Tools.printMonthDate(koartsald.getKoartsalddat()));
            koartbezLabel.setText(koartsald.getKoart().getKoartbez());
            koartkubezLabel.setText(koartsald.getKoart().getKoartkubez());
            koartgrpbezLabel.setText(koartsald.getKoart().getKoartgrp().getKoartgrpbez());
            saldoLabel.setText(Tools.printBigDecimal(koartsald.getKoartmonsaldo()) + " €");
            koartgrpkatLabel.setText(Tools.getKoartgrpkat(koartsald.getKoart().getKoartgrp()));
            koartgrpartLabel.setText(Tools.getKoartgrpart(koartsald.getKoart().getKoartgrp()));
        } catch (Exception e) {
            // messages.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_RED));
            // messages.setText(e.getMessage());
        }
    }

}