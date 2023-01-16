package haushaltsbuch.widgets;

import java.math.BigDecimal;
import java.util.Vector;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.conf.HB;
import haushaltsbuch.gui.dialogs.UIMonatsBuchungen;
import haushaltsbuch.util.GUITools;
import haushaltsbuch.util.Tools;

public class CostTypeGroupsComposite extends Composite {

    private BigDecimal total = new BigDecimal(0);

    public CostTypeGroupsComposite(Composite parent, Kostenartgruppe koartgrp, Vector<Kostenartsaldo> koartsaldi) {
        super(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        this.setLayout(layout);

        Label koartTitle = new Label(this, SWT.LEFT);
        koartTitle.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        koartTitle.setText(koartgrp.getKoartgrpbez());

        Label koartTotal = new Label(this, SWT.RIGHT);
        koartTotal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
        GUITools.setBoldFont(koartTotal);
        if (koartgrp.getKoartgrpkat().equals(HB.AUSGABE)) {
            koartTitle.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
            koartTotal.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_RED));
        } else {
            koartTitle.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
            koartTotal.setForeground(this.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        }

        Table table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL);
        GridData tableGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        tableGD.horizontalSpan = 2;
        table.setLayoutData(tableGD);
        table.setHeaderVisible(false);
        table.setLinesVisible(true);

        table.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent me) {
                Kostenartsaldo koartsald = (Kostenartsaldo) ((Table) me.widget).getSelection()[0].getData();
                new UIMonatsBuchungen(me.display.getActiveShell()).open(koartsald);
            }

            public void mouseDown(MouseEvent me) {}

            public void mouseUp(MouseEvent me) {}

        });

        table.addListener(SWT.FocusOut, new Listener() {

            public void handleEvent(Event e) {
                ((Table) e.widget).deselectAll();

            }

        });

        TableColumn column1 = new TableColumn(table, SWT.LEFT);
        column1.setMoveable(false);

        TableColumn column2 = new TableColumn(table, SWT.RIGHT);
        column2.setMoveable(false);

        for (Kostenartsaldo koartsald : koartsaldi) {
            TableItem ti = new TableItem(table, SWT.NONE);
            ti.setText(0, koartsald.getKoart().getKoartkubez());
            ti.setText(1, Tools.printBigDecimal(koartsald.getKoartmonsaldo()));
            ti.setData(koartsald);

            total = total.add(koartsald.getKoartmonsaldo());
        }

        AutoResizeTableLayout autoTableLayout = new AutoResizeTableLayout(table);
        autoTableLayout.addColumnData(new ColumnWeightData(3));
        autoTableLayout.addColumnData(new ColumnWeightData(2));
        table.setLayout(autoTableLayout);

        koartTotal.setText(Tools.printBigDecimal(total) + " €");
    }

}