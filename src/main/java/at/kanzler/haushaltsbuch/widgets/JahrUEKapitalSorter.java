package at.kanzler.haushaltsbuch.widgets;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import at.kanzler.haushaltsbuch.bean.util.KontoCollection;

public class JahrUEKapitalSorter extends ViewerSorter {

    public final static int KONTOBEZ_UP = 1;
    public final static int KONTOBEZ_DOWN = 2;
    public final static int KONTOKUERZEL_UP = 3;
    public final static int KONTOKUERZEL_DOWN = 4;
    public final static int AKTUELL_UP = 5;
    public final static int AKTUELL_DOWN = 6;
    public final static int EINGANG_UP = 7;
    public final static int EINGANG_DOWN = 8;
    public final static int AUSGANG_UP = 9;
    public final static int AUSGANG_DOWN = 10;
    public final static int DIFF_UP = 11;
    public final static int DIFF_DOWN = 12;

    private int criteria;

    public JahrUEKapitalSorter(int criteria) {
        super();
        this.criteria = criteria;
    }

    public int compare(Viewer viewer, Object o1, Object o2) {
        KontoCollection account1 = (KontoCollection) o1;
        KontoCollection account2 = (KontoCollection) o2;

        switch (criteria) {

            case KONTOBEZ_UP:
                return compareAccountName(account1, account2);

            case KONTOBEZ_DOWN:
                return 0 - compareAccountName(account1, account2);

            case KONTOKUERZEL_UP:
                return compareAccountTitle(account1, account2);

            case KONTOKUERZEL_DOWN:
                return 0 - compareAccountTitle(account1, account2);

            case AKTUELL_UP:
                return compareAccountAsset(account1, account2);

            case AKTUELL_DOWN:
                return 0 - compareAccountAsset(account1, account2);

            case EINGANG_UP:
                return compareAccountYearIn(account1, account2);

            case EINGANG_DOWN:
                return 0 - compareAccountYearIn(account1, account2);

            case AUSGANG_UP:
                return compareAccountYearOut(account1, account2);

            case AUSGANG_DOWN:
                return 0 - compareAccountYearOut(account1, account2);

            case DIFF_UP:
                return compareAccountDiff(account1, account2);

            case DIFF_DOWN:
                return 0 - compareAccountDiff(account1, account2);

            default:
                return 0;

        }
    }

    private int compareAccountName(KontoCollection account1, KontoCollection account2) {
        return account1.getKonto().getKtobez().compareTo(account2.getKonto().getKtobez());
    }

    private int compareAccountTitle(KontoCollection account1, KontoCollection account2) {
        return account1.getKonto().getKuerzel().compareTo(account2.getKonto().getKuerzel());
    }

    private int compareAccountAsset(KontoCollection account1, KontoCollection account2) {
        return account1.getKonto().getSaldo().compareTo(account2.getKonto().getSaldo());
    }

    private int compareAccountYearIn(KontoCollection account1, KontoCollection account2) {
        return account1.getYearIn().compareTo(account2.getYearIn());
    }

    private int compareAccountYearOut(KontoCollection account1, KontoCollection account2) {
        return account1.getYearOut().compareTo(account2.getYearOut());
    }

    private int compareAccountDiff(KontoCollection account1, KontoCollection account2) {
        return account1.getDiff().compareTo(account2.getDiff());
    }

    public int getCriteria() {
        return criteria;
    }

}