package at.kanzler.haushaltsbuch.widgets;

import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import at.kanzler.haushaltsbuch.bean.Buchung;
import at.kanzler.haushaltsbuch.logic.LogicMain;
import at.kanzler.haushaltsbuch.util.Tools;

public class BookingsTable implements SelectionListener {

    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    private Table table;
    private TableViewer tableViewer;

    public BookingsTable(Composite parent, Integer span) {
        Composite compTable = new Composite(parent, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = span;
        compTable.setLayoutData(gd);

        table = new Table(compTable, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumnLayout layout = new TableColumnLayout();
        compTable.setLayout(layout);

        String[] columnNames = { res.getString("Date"), res.getString("BookingText"), res.getString("Amount"),
                res.getString("Costtype"), res.getString("FromAccount"), res.getString("ToAccount") };
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText(columnNames[i]);
            if (i > 1)
                column.setAlignment(SWT.RIGHT);
            if (i == 1) {
                layout.setColumnData(column, new ColumnWeightData(360));
            } else
                layout.setColumnData(column, new ColumnWeightData(100));
            column.addSelectionListener(this);
        }
        tableViewer = new TableViewer(table);
        tableViewer.setColumnProperties(columnNames);
        tableViewer.setContentProvider(new BookingsContentProvider());
        tableViewer.setLabelProvider(new BookingsLabelProvider());

        setSorting(BookingsSorter.BUCHDAT_DOWN);
    }

    public void update(Vector<Buchung> data) {
        tableViewer.setInput(data);
    }

    public Buchung getSelectedBooking() {
        return (Buchung) tableViewer.getElementAt(table.getSelectionIndex());
    }

    public void clearSelection() {
        tableViewer.setSelection(null);
    }

    public void setSorting(Integer sorting) {
        tableViewer.setSorter(new BookingsSorter(sorting));
        if (sorting <= table.getColumnCount() * 2) {
            if (sorting % 2 == 0) {
                table.setSortDirection(SWT.DOWN);
                table.setSortColumn(table.getColumn((sorting / 2) - 1));
            } else {
                table.setSortDirection(SWT.UP);
                table.setSortColumn(table.getColumn(((sorting + 1) / 2) - 1));
            }
        } else {
            table.setSortDirection(SWT.NONE);
            table.setSortColumn(null);
        }
    }

    public Table getTable() {
        return this.table;
    }

    public TableViewer getTableViewer() {
        return this.tableViewer;
    }

    public void layout() {
        table.getParent().layout();
    }

    private class BookingsContentProvider implements IStructuredContentProvider {

        public Object[] getElements(Object input) throws AssertionError {
            assert input instanceof Vector<?> : "Tabelle konnte nicht erstellt werden!";
            Vector<?> data = (Vector<?>) input;
            return data.toArray();
        }

        public void dispose() {}

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}

    }

    private class BookingsLabelProvider extends LabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object arg0, int arg1) {
            return null;
        }

        public String getColumnText(Object input, int column) {
            Buchung b = (Buchung) input;

            switch (column) {

                case 0:
                    return Tools.printDate(b.getBuchdat());

                case 1:
                    return b.getBuchtext();

                case 2:
                    return Tools.printBigDecimal(b.getBuchbetr());

                case 3:
                    if (b.getKoart() != null) {
                        return b.getKoart().getKoartkubez();
                    } else
                        return "";

                case 4:
                    if (b.getKontovon() != null) {
                        return b.getKontovon().getKuerzel();
                    } else
                        return "";

                case 5:
                    if (b.getKontonach() != null) {
                        return b.getKontonach().getKuerzel();
                    } else
                        return "";
            }
            return "";
        }
    }

    public void widgetDefaultSelected(SelectionEvent se) {}

    public void widgetSelected(SelectionEvent se) {
        if (se.getSource().equals(table.getColumn(0))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.BUCHDAT_UP);
            else
                setSorting(BookingsSorter.BUCHDAT_DOWN);
        } else if (se.getSource().equals(table.getColumn(1))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.BUCHTEXT_UP);
            else
                setSorting(BookingsSorter.BUCHTEXT_DOWN);
        } else if (se.getSource().equals(table.getColumn(2))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.BUCHBETR_UP);
            else
                setSorting(BookingsSorter.BUCHBETR_DOWN);
        } else if (se.getSource().equals(table.getColumn(3))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.KOART_UP);
            else
                setSorting(BookingsSorter.KOART_DOWN);
        } else if (se.getSource().equals(table.getColumn(4))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.KONTOVON_UP);
            else
                setSorting(BookingsSorter.KONTOVON_DOWN);
        } else if (se.getSource().equals(table.getColumn(5))) {
            if (table.getSortDirection() == SWT.DOWN)
                setSorting(BookingsSorter.KONTONACH_UP);
            else
                setSorting(BookingsSorter.KONTONACH_DOWN);
        }
    }

}