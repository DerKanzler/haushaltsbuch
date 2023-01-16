package haushaltsbuch.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import haushaltsbuch.logic.LogicMain;
import haushaltsbuch.util.GUITools;

/**
 * This class builds the control frame which is visible
 * on the right side of the application at all times.
 * The user clicks on an icon/text, which he wants to see
 * as a dataframe and this class calls the methods to open the
 * corresponding dataframe and selects the correct image/buton.
 * 
 * @author pk
 *
 */
public class UIControlFrame extends Composite implements MouseListener, MouseTrackListener {

    private UIMainFrame frame;
    private ResourceBundle res = ResourceBundle.getBundle("haushaltsbuch.conf.Strings",
            LogicMain.instance().getUser().getFormat());
    private HashMap<Label, Label> labels = new HashMap<Label, Label>();

    private Label buchenText, suchenText, monatUEText, jahrUEKapitalText, jahrUEKoartText, verwaltungText;
    private Label buchenImage, suchenImage, monatUEImage, jahrUEKapitalImage, jahrUEKoartImage, verwaltungImage;

    private Image buchenIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/buchenIcon.png"));
    private Image suchenIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/suchenIcon.png"));
    private Image monatUEIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/monatUEIcon.png"));
    private Image jahrUEKapitalIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/jahrUEKapitalIcon.png"));
    private Image jahrUEKoartIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/jahrUEKoartIcon.png"));
    private Image verwaltungIcon = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/verwaltungIcon.png"));

    private Image buchenIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/buchenIcon_glow.png"));
    private Image suchenIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/suchenIcon_glow.png"));
    private Image monatUEIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/monatUEIcon_glow.png"));
    private Image jahrUEKapitalIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/jahrUEKapitalIcon_glow.png"));
    private Image jahrUEKoartIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/jahrUEKoartIcon_glow.png"));
    private Image verwaltungIcon_glow = new Image(this.getDisplay(),
            this.getClass().getClassLoader().getResourceAsStream("images/verwaltungIcon_glow.png"));

    private Color selected;
    private Color notselected;

    public UIControlFrame(UIMainFrame frame, Integer style) {
        super(frame, style);
        this.frame = frame;
        selected = GUITools.getSelectionColor();
        notselected = GUITools.getNonSelectionColor();
    }

    @Override
    public void dispose() {
        buchenIcon.dispose();
        suchenIcon.dispose();
        monatUEIcon.dispose();
        jahrUEKapitalIcon.dispose();
        jahrUEKoartIcon.dispose();
        verwaltungIcon.dispose();
        buchenIcon_glow.dispose();
        suchenIcon_glow.dispose();
        monatUEIcon_glow.dispose();
        jahrUEKapitalIcon_glow.dispose();
        jahrUEKoartIcon_glow.dispose();
        verwaltungIcon_glow.dispose();
        selected.dispose();
        notselected.dispose();
        super.dispose();
    }

    /**
     * Builds the user interface. It uses the GridLayout with just one column.
     * It doesn't have any margins.
     */
    public void buildUI() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        this.setLayout(layout);

        this.setBackground(notselected);

        GridData imageGD = new GridData(GridData.FILL_HORIZONTAL);
        GridData textGD = new GridData(GridData.FILL_HORIZONTAL);
        // sizes for the textlabels. This way the control frame gets the same width as well.
        textGD.widthHint = 130;
        textGD.heightHint = 36;
        textGD.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;

        buchenImage = new Label(this, SWT.CENTER);
        buchenImage.setLayoutData(imageGD);
        buchenImage.setBackground(notselected);
        buchenImage.setImage(buchenIcon);
        buchenImage.addMouseListener(this);
        buchenImage.addMouseTrackListener(this);

        buchenText = new Label(this, SWT.CENTER);
        buchenText.setLayoutData(textGD);
        buchenText.setBackground(notselected);
        buchenText.setText(res.getString("Accounting"));
        buchenText.addMouseListener(this);
        buchenText.addMouseTrackListener(this);

        suchenImage = new Label(this, SWT.CENTER);
        suchenImage.setLayoutData(imageGD);
        suchenImage.setBackground(notselected);
        suchenImage.setImage(suchenIcon);
        suchenImage.addMouseListener(this);
        suchenImage.addMouseTrackListener(this);

        suchenText = new Label(this, SWT.CENTER);
        suchenText.setLayoutData(textGD);
        suchenText.setBackground(notselected);
        suchenText.setText(res.getString("Search"));
        suchenText.addMouseListener(this);
        suchenText.addMouseTrackListener(this);

        monatUEImage = new Label(this, SWT.CENTER);
        monatUEImage.setLayoutData(imageGD);
        monatUEImage.setBackground(notselected);
        monatUEImage.setImage(monatUEIcon);
        monatUEImage.addMouseListener(this);
        monatUEImage.addMouseTrackListener(this);

        monatUEText = new Label(this, SWT.CENTER);
        monatUEText.setLayoutData(textGD);
        monatUEText.setBackground(notselected);
        monatUEText.setText(res.getString("MonthOverview"));
        monatUEText.addMouseListener(this);
        monatUEText.addMouseTrackListener(this);

        jahrUEKoartImage = new Label(this, SWT.CENTER);
        jahrUEKoartImage.setLayoutData(imageGD);
        jahrUEKoartImage.setBackground(notselected);
        jahrUEKoartImage.setImage(jahrUEKoartIcon);
        jahrUEKoartImage.addMouseListener(this);
        jahrUEKoartImage.addMouseTrackListener(this);

        jahrUEKoartText = new Label(this, SWT.CENTER | SWT.WRAP);
        jahrUEKoartText.setLayoutData(textGD);
        jahrUEKoartText.setBackground(notselected);
        jahrUEKoartText.setText(res.getString("YearOverviewCosttypes"));
        jahrUEKoartText.addMouseListener(this);
        jahrUEKoartText.addMouseTrackListener(this);

        jahrUEKapitalImage = new Label(this, SWT.CENTER);
        jahrUEKapitalImage.setLayoutData(imageGD);
        jahrUEKapitalImage.setBackground(notselected);
        jahrUEKapitalImage.setImage(jahrUEKapitalIcon);
        jahrUEKapitalImage.addMouseListener(this);
        jahrUEKapitalImage.addMouseTrackListener(this);

        jahrUEKapitalText = new Label(this, SWT.CENTER | SWT.WRAP);
        jahrUEKapitalText.setLayoutData(textGD);
        jahrUEKapitalText.setBackground(notselected);
        jahrUEKapitalText.setText(res.getString("YearOverviewAssets"));
        jahrUEKapitalText.addMouseListener(this);
        jahrUEKapitalText.addMouseTrackListener(this);

        Label filler = new Label(this, SWT.CENTER);
        filler.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        filler.setBackground(notselected);

        verwaltungImage = new Label(this, SWT.CENTER);
        verwaltungImage.setLayoutData(imageGD);
        verwaltungImage.setBackground(notselected);
        verwaltungImage.setImage(verwaltungIcon);
        verwaltungImage.addMouseListener(this);
        verwaltungImage.addMouseTrackListener(this);

        verwaltungText = new Label(this, SWT.CENTER);
        verwaltungText.setLayoutData(textGD);
        verwaltungText.setBackground(notselected);
        verwaltungText.setText(res.getString("Administration"));
        verwaltungText.addMouseListener(this);
        verwaltungText.addMouseTrackListener(this);

        // Puts all the icons and texts into the HashMap.
        // That's needed to select (change the background)
        // of the correct image and text.
        labels.put(buchenImage, buchenText);
        labels.put(suchenImage, suchenText);
        labels.put(monatUEImage, monatUEText);
        labels.put(jahrUEKapitalImage, jahrUEKapitalText);
        labels.put(jahrUEKoartImage, jahrUEKoartText);
        labels.put(verwaltungImage, verwaltungText);
    }

    /**
     * Selects the search menu point.
     */
    public void selectSuchen() {
        setSelection(suchenText);
    }

    /**
     * Sets the selection on the given image/text.
     * Selects both (corresponding text and image).
     * @param Object
     */
    private void setSelection(Object label) {
        // A Set stores the entry set of the hashmap
        Set<Map.Entry<Label, Label>> es = labels.entrySet();
        // for every entry set in the set
        for (Map.Entry<Label, Label> map : es) {
            // If the given object equals the key or the value of the entry set it gets selected
            if ((label.equals(map.getKey())) || (label.equals(map.getValue()))) {
                map.getKey().setBackground(selected);
                map.getValue().setBackground(selected);
            }
            // Otherwise it is being unselected. Even if it wasn't selected.
            else {
                map.getKey().setBackground(notselected);
                map.getValue().setBackground(notselected);
            }
        }
    }

    /**
     * Does nothing.
     */
    public void mouseDoubleClick(MouseEvent me) {}

    /**
     * Does nothing.
     */
    public void mouseDown(MouseEvent me) {}

    /**
     * Catches the MouseEvent which is triggered when the user
     * releases the mouse key.
     */
    public void mouseUp(MouseEvent me) {
        Boolean doit = true;
        if (this.getShell().getModified()) {
            MessageBox box = new MessageBox(this.getShell(),
                    SWT.ICON_QUESTION | SWT.PRIMARY_MODAL | SWT.YES | SWT.NO | SWT.SHEET);
            box.setMessage(res.getString("Continue"));
            doit = box.open() == SWT.YES;
            this.getShell().setModified(false);
        }
        if (doit) {
            frame.getDataFrame().deactivateDataFrame();
            // First sets the selection.
            setSelection(me.getSource());

            // Then the corresponding dataframe is being activated if either the text or the image were clicked.
            if (me.getSource().equals(buchenText) || me.getSource().equals(buchenImage)) {
                frame.getDataFrame().activateBuchen();
            } else if (me.getSource().equals(suchenText) || me.getSource().equals(suchenImage)) {
                frame.getDataFrame().activateSuchen();
            } else if (me.getSource().equals(monatUEText) || me.getSource().equals(monatUEImage)) {
                frame.getDataFrame().activateMonatUE();
            } else if (me.getSource().equals(jahrUEKapitalText) || me.getSource().equals(jahrUEKapitalImage)) {
                frame.getDataFrame().activateJahrUEKapital();
            } else if (me.getSource().equals(jahrUEKoartText) || me.getSource().equals(jahrUEKoartImage)) {
                frame.getDataFrame().activateJahrUEKoart();
            } else if (me.getSource().equals(verwaltungText) || me.getSource().equals(verwaltungImage)) {
                frame.getDataFrame().activateVerwaltung();
            }
        }
    }

    public void mouseEnter(MouseEvent me) {
        if (me.getSource().equals(buchenText) || me.getSource().equals(buchenImage)) {
            buchenImage.setImage(buchenIcon_glow);
        } else if (me.getSource().equals(suchenText) || me.getSource().equals(suchenImage)) {
            suchenImage.setImage(suchenIcon_glow);
        } else if (me.getSource().equals(monatUEText) || me.getSource().equals(monatUEImage)) {
            monatUEImage.setImage(monatUEIcon_glow);
        } else if (me.getSource().equals(jahrUEKapitalText) || me.getSource().equals(jahrUEKapitalImage)) {
            jahrUEKapitalImage.setImage(jahrUEKapitalIcon_glow);
        } else if (me.getSource().equals(jahrUEKoartText) || me.getSource().equals(jahrUEKoartImage)) {
            jahrUEKoartImage.setImage(jahrUEKoartIcon_glow);
        } else if (me.getSource().equals(verwaltungText) || me.getSource().equals(verwaltungImage)) {
            verwaltungImage.setImage(verwaltungIcon_glow);
        }
    }

    public void mouseExit(MouseEvent me) {
        if (me.getSource().equals(buchenText) || me.getSource().equals(buchenImage)) {
            buchenImage.setImage(buchenIcon);
        } else if (me.getSource().equals(suchenText) || me.getSource().equals(suchenImage)) {
            suchenImage.setImage(suchenIcon);
        } else if (me.getSource().equals(monatUEText) || me.getSource().equals(monatUEImage)) {
            monatUEImage.setImage(monatUEIcon);
        } else if (me.getSource().equals(jahrUEKapitalText) || me.getSource().equals(jahrUEKapitalImage)) {
            jahrUEKapitalImage.setImage(jahrUEKapitalIcon);
        } else if (me.getSource().equals(jahrUEKoartText) || me.getSource().equals(jahrUEKoartImage)) {
            jahrUEKoartImage.setImage(jahrUEKoartIcon);
        } else if (me.getSource().equals(verwaltungText) || me.getSource().equals(verwaltungImage)) {
            verwaltungImage.setImage(verwaltungIcon);
        }
    }

    public void mouseHover(MouseEvent me) {}

}