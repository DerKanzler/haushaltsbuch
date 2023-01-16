package haushaltsbuch.widgets;

import java.util.Collection;
import java.util.ResourceBundle;

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

import haushaltsbuch.bean.util.KontoCollection;
import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.Tools;

public class JahrUEKapitalTable implements SelectionListener {

    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    private Table table;
    private TableViewer tableViewer;

    public JahrUEKapitalTable(Composite parent, Integer span) {
        Composite compTable = new Composite(parent, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = span;
        compTable.setLayoutData(gd);

        table = new Table(compTable, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumnLayout layout = new TableColumnLayout();
        compTable.setLayout(layout);

        String[] columnNames = { res.getString("Account"), "Kürzel", "Aktueller Stand", "Jahreseingang",
                "Jahresausgang", "Differenz" };
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = new TableColumn(table, SWT.LEFT);
            column.setText(columnNames[i]);
            if (i > 1)
                column.setAlignment(SWT.RIGHT);
            if (i == 0) {
                layout.setColumnData(column, new ColumnWeightData(360));
            } else
                layout.setColumnData(column, new ColumnWeightData(100));
            column.addSelectionListener(this);
        }
        tableViewer = new TableViewer(table);
        tableViewer.setColumnProperties(columnNames);
        tableViewer.setContentProvider(new BookingsContentProvider());
        tableViewer.setLabelProvider(new BookingsLabelProvider());

        tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.KONTOKUERZEL_UP));
        table.setSortDirection(SWT.UP);
        table.setSortColumn(table.getColumn(1));
    }

    public void update(Collection<KontoCollection> data) {
        tableViewer.setInput(data);
        clearSelection();
        layout();
    }

    public KontoCollection getSelectedAccount() {
        return (KontoCollection) tableViewer.getElementAt(table.getSelectionIndex());
    }

    public void clearSelection() {
        tableViewer.setSelection(null);
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
            assert input instanceof Collection<?> : "Tabelle konnte nicht erstellt werden!";
            Collection<?> data = (Collection<?>) input;
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
            KontoCollection k = (KontoCollection) input;

            switch (column) {

                case 0:
                    return k.getKonto().getKtobez();

                case 1:
                    return k.getKonto().getKuerzel();

                case 2:
                    return Tools.printBigDecimal(k.getKonto().getSaldo());

                case 3:
                    return Tools.printBigDecimal(k.getYearIn());

                case 4:
                    return Tools.printBigDecimal(k.getYearOut());

                case 5:
                    return Tools.printBigDecimal(k.getDiff());
            }
            return "";
        }
    }

    public void widgetDefaultSelected(SelectionEvent se) {}

    public void widgetSelected(SelectionEvent se) {
        if (se.getSource().equals(table.getColumn(0))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.KONTOBEZ_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.KONTOBEZ_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(0));
        } else if (se.getSource().equals(table.getColumn(1))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.KONTOKUERZEL_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.KONTOKUERZEL_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(1));
        } else if (se.getSource().equals(table.getColumn(2))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.AKTUELL_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.AKTUELL_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(2));
        } else if (se.getSource().equals(table.getColumn(3))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.EINGANG_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.EINGANG_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(3));
        } else if (se.getSource().equals(table.getColumn(4))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.AUSGANG_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.AUSGANG_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(4));
        } else if (se.getSource().equals(table.getColumn(5))) {
            if (table.getSortDirection() == SWT.DOWN) {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.DIFF_UP));
                table.setSortDirection(SWT.UP);
            } else {
                tableViewer.setSorter(new JahrUEKapitalSorter(JahrUEKapitalSorter.DIFF_DOWN));
                table.setSortDirection(SWT.DOWN);
            }
            table.setSortColumn(table.getColumn(5));
        }
    }

}