package at.kanzler.haushaltsbuch.gui.dataframes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import at.kanzler.haushaltsbuch.exceptions.MathException;
import at.kanzler.haushaltsbuch.gui.UIDataFrame;
import at.kanzler.haushaltsbuch.logic.LogicJahrUEKoart;
import at.kanzler.haushaltsbuch.logic.LogicMain;
import at.kanzler.haushaltsbuch.util.GUITools;
import at.kanzler.haushaltsbuch.util.Tools;

public class UIJahrUEKoart extends UIAbstractDataFrame implements SelectionListener {

    private Spinner yearText;
    private Button previousYearButton, nextYearButton;

    private Label incomeCurrentLabel, incomePrevYearLabel, incomeCurYearLabel, incomeDiffLabel, incomePercLabel,
            incomeAvgLabel;
    private Label expCurrentLabel, expPrevYearLabel, expCurYearLabel, expDiffLabel, expPercLabel, expAvgLabel;
    private Label expHHCurrentLabel, expHHPrevYearLabel, expHHCurYearLabel, expHHDiffLabel, expHHPercLabel,
            expHHAvgLabel;
    private Label savCurrentLabel, savPrevYearLabel, savCurYearLabel, savDiffLabel, savPercLabel, savAvgLabel;

    private Color green = Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN);
    private Color red = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
    private Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

    private LogicJahrUEKoart logic;
    private ResourceBundle res = ResourceBundle.getBundle("at.kanzler.haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());

    /**
     * Constructor: Since UIJahrUEKapital extends Composite the super constructor is
     * called here with the given arguments. Builds the visible user interface. Uses
     * GridLayout with 4 columns.
     * 
     * @param Composite
     * @param Integer
     */
    public UIJahrUEKoart(UIDataFrame parent, Integer style) {
        super(parent, style);

        GridLayout layout = new GridLayout(4, false);
        this.setLayout(layout);

        Label yearLabel = new Label(this, SWT.NONE);
        yearLabel.setText(res.getString("Year"));

        yearText = new Spinner(this, SWT.BORDER);
        yearText.setTextLimit(4);
        yearText.setIncrement(1);
        yearText.addSelectionListener(this);

        previousYearButton = new Button(this, SWT.FLAT);
        previousYearButton.setImage(
                new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/back.png")));
        previousYearButton.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_END | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
        previousYearButton.addSelectionListener(this);

        nextYearButton = new Button(this, SWT.FLAT);
        nextYearButton.setImage(new Image(this.getDisplay(),
                this.getClass().getClassLoader().getResourceAsStream("images/forward.png")));
        nextYearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER));
        nextYearButton.addSelectionListener(this);

        Group expHHmaryGroup = new Group(this, SWT.NONE);
        GridData expHHmaryGroupGD = new GridData(GridData.FILL_HORIZONTAL);
        expHHmaryGroupGD.horizontalSpan = 4;
        expHHmaryGroup.setLayoutData(expHHmaryGroupGD);
        // expHHmaryGroup.setText(res.getString("Summary"));
        // Calls the method to fill this group with the appropiate widgets.
        buildSummaryGroup(expHHmaryGroup);

        Composite costtypesGroup = new Composite(this, SWT.NONE);
        GridData costtypesGroupGD = new GridData(GridData.FILL_BOTH);
        costtypesGroupGD.horizontalSpan = 4;
        costtypesGroup.setLayoutData(costtypesGroupGD);
        // accountsGroup.setText(res.getString("Costtypes"));
        costtypesGroup.setLayout(new GridLayout(4, false));

        Tree tree = new Tree(costtypesGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData treeGD = new GridData(GridData.FILL_BOTH);
        treeGD.horizontalSpan = 4;
        tree.setLayoutData(treeGD);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("Column 1");
        column1.setWidth(200);
        TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
        column2.setText("Column 2");
        column2.setWidth(200);
        TreeColumn column3 = new TreeColumn(tree, SWT.RIGHT);
        column3.setText("Column 3");
        column3.setWidth(200);
        for (int i = 0; i < 4; i++) {
            TreeItem item = new TreeItem(tree, SWT.NONE);
            item.setText(new String[] { "koartruppe " + i, "abc", "defghi" });
            for (int j = 0; j < 4; j++) {
                TreeItem subItem = new TreeItem(item, SWT.NONE);
                subItem.setText(new String[] { "koart  " + j, "jklmnop", "qrs" });
            }
        }

        fillFields(Tools.getLastMonth().getYear());
    }

    private void buildSummaryGroup(Group group) {
        GridLayout layout = new GridLayout(9, false);
        group.setLayout(layout);

        Label incomeTitleLabel = new Label(group, SWT.NONE);
        GUITools.setBoldFont(incomeTitleLabel);
        incomeTitleLabel.setText("Gesamteinnahmen");

        incomeCurYearLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(incomeCurYearLabel);
        incomeCurYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Vorjahr");
        incomePrevYearLabel = new Label(group, SWT.RIGHT);
        incomePrevYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Differenz");
        incomeDiffLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(incomeDiffLabel);
        incomeDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite incomeButtonContainer = new Composite(group, SWT.NONE);
        GridData buttonContainerGD = new GridData(GridData.FILL_VERTICAL);
        buttonContainerGD.verticalSpan = 2;
        incomeButtonContainer.setLayoutData(buttonContainerGD);
        GridLayout buttonLayout = new GridLayout(2, true);
        buttonLayout.marginHeight = 0;
        buttonLayout.marginLeft = buttonLayout.marginWidth;
        buttonLayout.marginWidth = 0;
        buttonLayout.marginRight = 0;
        incomeButtonContainer.setLayout(buttonLayout);

        Button incomeChartButton = new Button(incomeButtonContainer, SWT.FLAT);
        incomeChartButton.setImage(
                new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
        GridData buttonGD = new GridData();
        buttonGD.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        buttonGD.horizontalAlignment = GridData.HORIZONTAL_ALIGN_CENTER;
        incomeChartButton.setLayoutData(buttonGD);
        incomeChartButton.setToolTipText("Verlauf");
        incomeChartButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                // new UIKapitalJahrDialog(Display.getDefault().getActiveShell(),
                // UIKapitalJahrDialog.KAPITAL_VERFUEGBAR,
                // logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_VERFUEGBAR)).open();
            }
        });

        Button incomeBookButton = new Button(incomeButtonContainer, SWT.FLAT);
        incomeBookButton.setImage(new Image(this.getDisplay(),
                this.getClass().getClassLoader().getResourceAsStream("images/buchungen.png")));
        incomeBookButton.setLayoutData(buttonGD);
        incomeBookButton.setToolTipText("Buchungen");
        incomeBookButton.setEnabled(false);

        new Label(group, SWT.NONE).setText("Aktueller Monat");
        incomeCurrentLabel = new Label(group, SWT.RIGHT);
        incomeCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Monatsschnitt");
        incomeAvgLabel = new Label(group, SWT.RIGHT);
        incomeAvgLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Steigerung");
        incomePercLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(incomePercLabel);
        incomePercLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        GridData separatorGD = new GridData(GridData.FILL_HORIZONTAL);
        separatorGD.horizontalSpan = 9;
        new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorGD);

        Label expTitleLabel = new Label(group, SWT.LEFT);
        GUITools.setBoldFont(expTitleLabel);
        expTitleLabel.setText("Gesamtausgaben");

        expCurYearLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expCurYearLabel);
        expCurYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Vorjahr");
        expPrevYearLabel = new Label(group, SWT.RIGHT);
        expPrevYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Differenz");
        expDiffLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expDiffLabel);
        expDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite expButtonContainer = new Composite(group, SWT.NONE);
        expButtonContainer.setLayoutData(buttonContainerGD);
        expButtonContainer.setLayout(buttonLayout);

        Button expChartButton = new Button(expButtonContainer, SWT.FLAT);
        expChartButton.setImage(
                new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
        expChartButton.setLayoutData(buttonGD);
        expChartButton.setToolTipText("Verlauf");
        expChartButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                // new UIKapitalJahrDialog(Display.getDefault().getActiveShell(),
                // UIKapitalJahrDialog.KAPITAL_GESPERRT,
                // logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_GESPERRT)).open();
            }
        });

        Button expBookButton = new Button(expButtonContainer, SWT.FLAT);
        expBookButton.setImage(new Image(this.getDisplay(),
                this.getClass().getClassLoader().getResourceAsStream("images/buchungen.png")));
        expBookButton.setLayoutData(buttonGD);
        expBookButton.setToolTipText("Buchungen");
        expBookButton.setEnabled(false);

        new Label(group, SWT.NONE).setText("Aktueller Monat");
        expCurrentLabel = new Label(group, SWT.RIGHT);
        expCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Monatsschnitt");
        expAvgLabel = new Label(group, SWT.RIGHT);
        expAvgLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Steigerung");
        expPercLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expPercLabel);
        expPercLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorGD);

        Label expHHTitleLabel = new Label(group, SWT.LEFT);
        GUITools.setBoldFont(expHHTitleLabel);
        expHHTitleLabel.setText("Ausgaben ohne Haus");

        expHHCurYearLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expHHCurYearLabel);
        expHHCurYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Vorjahr");
        expHHPrevYearLabel = new Label(group, SWT.RIGHT);
        expHHPrevYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Differenz");
        expHHDiffLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expHHDiffLabel);
        expHHDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite expHHButtonContainer = new Composite(group, SWT.NONE);
        expHHButtonContainer.setLayoutData(buttonContainerGD);
        expHHButtonContainer.setLayout(buttonLayout);

        Button expHHChartButton = new Button(expHHButtonContainer, SWT.FLAT);
        expHHChartButton.setImage(
                new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
        expHHChartButton.setLayoutData(buttonGD);
        expHHChartButton.setToolTipText("Verlauf");
        expHHChartButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                // new UIKapitalJahrDialog(Display.getDefault().getActiveShell(),
                // UIKapitalJahrDialog.KAPITAL_GESAMT,
                // logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_GESAMT)).open();
            }
        });

        Button expHHBookButton = new Button(expHHButtonContainer, SWT.FLAT);
        expHHBookButton.setImage(new Image(this.getDisplay(),
                this.getClass().getClassLoader().getResourceAsStream("images/buchungen.png")));
        expHHBookButton.setLayoutData(buttonGD);
        expHHBookButton.setToolTipText("Buchungen");
        expHHBookButton.setEnabled(false);

        new Label(group, SWT.NONE).setText("Aktueller Monat");
        expHHCurrentLabel = new Label(group, SWT.RIGHT);
        expHHCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Monatsschnitt");
        expHHAvgLabel = new Label(group, SWT.RIGHT);
        expHHAvgLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Steigerung");
        expHHPercLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(expHHPercLabel);
        expHHPercLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorGD);

        Label savTitleLabel = new Label(group, SWT.LEFT);
        GUITools.setBoldFont(savTitleLabel);
        savTitleLabel.setText("Ersparnis");

        savCurYearLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(savCurYearLabel);
        savCurYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Vorjahr");
        savPrevYearLabel = new Label(group, SWT.RIGHT);
        savPrevYearLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Differenz");
        savDiffLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(savDiffLabel);
        savDiffLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite savButtonContainer = new Composite(group, SWT.NONE);
        savButtonContainer.setLayoutData(buttonContainerGD);
        savButtonContainer.setLayout(buttonLayout);

        Button savChartButton = new Button(savButtonContainer, SWT.FLAT);
        savChartButton.setImage(
                new Image(this.getDisplay(), this.getClass().getClassLoader().getResourceAsStream("images/chart.png")));
        savChartButton.setLayoutData(buttonGD);
        savChartButton.setToolTipText("Verlauf");
        savChartButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                // new UIKapitalJahrDialog(Display.getDefault().getActiveShell(),
                // UIKapitalJahrDialog.KAPITAL_ERSPARNIS,
                // logic.getDetailedData(UIKapitalJahrDialog.KAPITAL_ERSPARNIS)).open();
            }
        });

        Button savBookButton = new Button(savButtonContainer, SWT.FLAT);
        savBookButton.setImage(new Image(this.getDisplay(),
                this.getClass().getClassLoader().getResourceAsStream("images/buchungen.png")));
        savBookButton.setLayoutData(buttonGD);
        savBookButton.setToolTipText("Buchungen");
        savBookButton.setEnabled(false);

        new Label(group, SWT.NONE).setText("Aktueller Monat");
        savCurrentLabel = new Label(group, SWT.RIGHT);
        savCurrentLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Monatsschnitt");
        savAvgLabel = new Label(group, SWT.RIGHT);
        savAvgLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(group, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        new Label(group, SWT.NONE).setText("Steigerung");
        savPercLabel = new Label(group, SWT.RIGHT);
        GUITools.setBoldFont(savPercLabel);
        savPercLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void fillFields(Integer year) {
        LocalDate lastDate = Tools.getLastDate();
        if (Month.JANUARY.equals(lastDate.getMonth())) {
            yearText.setMaximum(lastDate.getYear() - 1);
            if (year == (lastDate.getYear() - 1)) {
                year = year - 1;
            }
        } else {
            yearText.setMaximum(Tools.getLastDate().getYear());
        }
        yearText.setMinimum(Tools.getFirstDate().getYear());
        yearText.setSelection(year);
        if (year == yearText.getMinimum()) {
            previousYearButton.setEnabled(false);
        } else
            previousYearButton.setEnabled(true);
        if (year == yearText.getMaximum()) {
            nextYearButton.setEnabled(false);
        } else
            nextYearButton.setEnabled(true);

        logic = new LogicJahrUEKoart(year);

        incomePrevYearLabel.setText(Tools.printBigDecimal(logic.getIncPrevYear()) + " €");
        incomeCurYearLabel.setText(Tools.printBigDecimal(logic.getIncCurYear()) + " €");
        incomeDiffLabel.setText(Tools.printBigDecimal(logic.getIncDiff()) + " €");
        if (logic.getIncDiff().compareTo(new BigDecimal(0)) < 0)
            incomeDiffLabel.setForeground(red);
        else if (logic.getIncDiff().compareTo(new BigDecimal(0)) > 0)
            incomeDiffLabel.setForeground(green);
        else
            incomeDiffLabel.setForeground(black);
        incomeCurrentLabel.setText(Tools.printBigDecimal(logic.getIncCurrent()) + " €");
        incomeAvgLabel.setText(Tools.printBigDecimal(logic.getIncAvg()) + " €");
        try {
            incomePercLabel.setText(Tools.printBigDecimal(logic.getIncPerc()) + " %");
        } catch (MathException e) {
            incomePercLabel.setText("∞");
        }
        incomePercLabel.setForeground(incomeDiffLabel.getForeground());

        expPrevYearLabel.setText(Tools.printBigDecimal(logic.getExpPrevYear()) + " €");
        expCurYearLabel.setText(Tools.printBigDecimal(logic.getExpCurYear()) + " €");
        expDiffLabel.setText(Tools.printBigDecimal(logic.getExpDiff()) + " €");
        if (logic.getExpDiff().compareTo(new BigDecimal(0)) < 0)
            expDiffLabel.setForeground(green);
        else if (logic.getExpDiff().compareTo(new BigDecimal(0)) > 0)
            expDiffLabel.setForeground(red);
        else
            expDiffLabel.setForeground(black);
        expCurrentLabel.setText(Tools.printBigDecimal(logic.getExpCurrent()) + " €");
        expAvgLabel.setText(Tools.printBigDecimal(logic.getExpAvg()) + " €");
        try {
            expPercLabel.setText(Tools.printBigDecimal(logic.getExpPerc()) + " %");
        } catch (MathException e) {
            expPercLabel.setText("∞");
        }
        expPercLabel.setForeground(expDiffLabel.getForeground());

        expHHPrevYearLabel.setText(Tools.printBigDecimal(logic.getExpHHPrevYear()) + " €");
        expHHCurYearLabel.setText(Tools.printBigDecimal(logic.getExpHHCurYear()) + " €");
        expHHDiffLabel.setText(Tools.printBigDecimal(logic.getExpHHDiff()) + " €");
        if (logic.getExpHHDiff().compareTo(new BigDecimal(0)) < 0)
            expHHDiffLabel.setForeground(green);
        else if (logic.getExpHHDiff().compareTo(new BigDecimal(0)) > 0)
            expHHDiffLabel.setForeground(red);
        else
            expHHDiffLabel.setForeground(black);
        expHHCurrentLabel.setText(Tools.printBigDecimal(logic.getExpHHCurrent()) + " €");
        expHHAvgLabel.setText(Tools.printBigDecimal(logic.getExpHHAvg()) + " €");
        try {
            expHHPercLabel.setText(Tools.printBigDecimal(logic.getExpHHPerc()) + " %");
        } catch (MathException e) {
            expHHPercLabel.setText("∞");
        }
        expHHPercLabel.setForeground(expHHDiffLabel.getForeground());

        savPrevYearLabel.setText(Tools.printBigDecimal(logic.getSavPrevYear()) + " €");
        savCurYearLabel.setText(Tools.printBigDecimal(logic.getSavCurYear()) + " €");
        savDiffLabel.setText(Tools.printBigDecimal(logic.getSavDiff()) + " €");
        if (logic.getSavDiff().compareTo(new BigDecimal(0)) < 0)
            savDiffLabel.setForeground(red);
        else if (logic.getSavDiff().compareTo(new BigDecimal(0)) > 0)
            savDiffLabel.setForeground(green);
        else
            savDiffLabel.setForeground(black);
        savCurrentLabel.setText(Tools.printBigDecimal(logic.getSavCurrent()) + " €");
        savAvgLabel.setText(Tools.printBigDecimal(logic.getSavAvg()) + " €");
        try {
            savPercLabel.setText(Tools.printBigDecimal(logic.getSavPerc()) + " %");
        } catch (MathException e) {
            savPercLabel.setText("∞");
        }
        savPercLabel.setForeground(savDiffLabel.getForeground());
    }

    /**
     * Does nothing.
     */
    public void widgetDefaultSelected(SelectionEvent se) {}

    /**
     * Catches the SelectionEvent when a button is clicked.
     */
    public void widgetSelected(SelectionEvent se) {
        if (se.getSource().equals(previousYearButton)) {
            fillFields(yearText.getSelection() - 1);
        } else if (se.getSource().equals(nextYearButton)) {
            fillFields(yearText.getSelection() + 1);
        } else if (se.getSource().equals(yearText)) {
            fillFields(yearText.getSelection());
        }
    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update() {
        fillFields(Integer.valueOf(yearText.getText()));
        setToBeUpdated(false);
    }

}