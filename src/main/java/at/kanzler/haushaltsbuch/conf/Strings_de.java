package at.kanzler.haushaltsbuch.conf;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class Strings_de extends ResourceBundle {

    private Hashtable<String, String> lang = new Hashtable<String, String>();

    public Strings_de() {
        lang.put("Application", "Haushaltsbuch");
        lang.put("ApplicationRunning", "Das Programm läuft bereits!");
        lang.put("Accounting", "Buchen");
        lang.put("Search", "Suchen");
        lang.put("MonthOverview", "Monatsübersicht");
        lang.put("YearOverviewAssets", "Jahresübersicht Kapital");
        lang.put("YearOverviewCosttypes", "Jahresübersicht Kostenarten");
        lang.put("Administration", "Verwaltung");
        lang.put("QuickSearch", "Schnellsuche:");
        lang.put("Tools", "Extras");
        lang.put("Quit", "Haushaltsbuch beenden");
        lang.put("Backup", "Datenbank sichern ...");
        lang.put("Restore", "Datenbank wiederherstellen ...");
        lang.put("Preferences", "Einstellungen");
        lang.put("About", "Über Haushaltsbuch");
        lang.put("ZipFiles", "Zip Dateien");
        lang.put("BackupFailed", "Das Sichern der Datenbank ist fehlgeschlagen!");
        lang.put("BackupSuccessfull", "Das Sichern der Datenbank war erfolgreich!");
        lang.put("RestoreFailed", "Das Wiederherstellen der Datenbank ist fehlgeschlagen!");
        lang.put("RestoreSuccessfull",
                "Wiederherstellen erfolgreich abgeschlossen!\nDas Programm wird neu gestartet!\n\nAchtung: Das Passwort wurde unter Umständen dabei zurückgesetzt!");
        lang.put("LoginText", "Bitte Passwort eingeben:");
        lang.put("Login", "Anmelden");
        lang.put("WrongPasswd", "Das Passwort ist falsch!");
        lang.put("EnterPasswd", "Bitte das Passwort eingeben!");
        lang.put("Summary", "Übersicht");
        lang.put("Booking", "Buchung");
        lang.put("Abort", "Abbrechen");
        lang.put("Save", "Speichern");
        lang.put("SortInputDate", "<a>Nach Eingabedatum sortieren</a>");
        lang.put("Edit", "Ändern");
        lang.put("Delete", "Löschen");
        lang.put("DisposableAmount", "Verfügbare Gelder");
        lang.put("OfflimitAmount", "Gesperrte Gelder");
        lang.put("TotalAssets", "Gesamtkapital");
        lang.put("TotalRevenues", "Einnahmen");
        lang.put("TotalExpenses", "Ausgaben");
        lang.put("TotalSavings", "Ersparnis");
        lang.put("Date", "Datum");
        lang.put("BookingText", "Buchungstext");
        lang.put("Amount", "Betrag");
        lang.put("Costtype", "Kostenart");
        lang.put("FromAccount", "Konto von");
        lang.put("ToAccount", "Konto nach");
        lang.put("BookMonth", "Buchungsmonat");
        lang.put("SearchTerms", "Suchbegriffe");
        lang.put("Clear", "Zurücksetzen");
        lang.put("SearchResults", "Suchergebnis");
        lang.put("FromDate", "Von-Datum");
        lang.put("ToDate", "Bis-Datum");
        lang.put("Sum", "Summe");
        lang.put("3TriesPasswd", "Das Passwort wurde drei mal falsch eingegeben!\nDas Programm wird geschlossen!");
        lang.put("DBError", "Es besteht keine Verbindung zur Datenbank. Bitte starten sie die Applikation neu.");
        lang.put("AddCosttypegroup", "Neue Kostenartgruppe hinzufügen");
        lang.put("Title", "Bezeichnung");
        lang.put("Category", "Kategorie");
        lang.put("Type", "Art");
        lang.put("ShortTitle", "Kürzel");
        lang.put("Status", "Status");
        lang.put("ClosingDate", "Ablaufdatum");
        lang.put("BeginningYear", "Jahreseingang");
        lang.put("Balance", "Saldo");
        lang.put("EmptyTitle", "Kontobezeichnung darf nicht leer sein!");
        lang.put("EmptyShortTitle", "Kontokuerzel darf nicht leer sein!");
        lang.put("EmptyType", "Es muss ein Kontotyp gewählt werden!");
        lang.put("EmptyStatus", "Es muss ein Kontostatus gewählt werden!");
        lang.put("EmptyClosingDate", "Es muss ein Ablaufdatum gewählt werden!");
        lang.put("PastClosingDate", "Das Ablaufdatum muss in der Zukunft liegen!");
        lang.put("AddAccount", "Neues Konto hinzufügen");
        lang.put("Accounts", "Konten");
        lang.put("Costtypes", "Kostenarten");
        lang.put("Costtypegroups", "Kostenartgruppen");
        lang.put("AddCosttype", "Neue Kostenart hinzufügen");
        lang.put("Costtypegroup", "Kostenartgruppe");
        lang.put("EmptyCTTitle", "Kostenartbezeichnung darf nicht leer sein!");
        lang.put("EmptyCTShortTitle", "Kostenartkürzel darf nicht leer sein!");
        lang.put("EmptyCosttypegroup", "Es muss eine Kostenartgruppe gewählt werden!");
        lang.put("Language", "Sprache");
        lang.put("Account", "Konto");
        lang.put("Continue",
                "Es wurden Änderungen vorgenommen. Möchten sie wirklich fortfahren?\nIhre Änderungen werden dadurch verworfen!");
        lang.put("Month", "Monat");
        lang.put("Close", "Schließen");
        lang.put("Year", "Jahr");
        lang.put("BackupDB", "Datenbank wird gesichert. Bitte warten.");
        lang.put("RestoreDB", "Datenbank wird wiederhergestellt. Bitte warten.");
        lang.put("Update", "Auf Update überprüfen ...");
        lang.put("H2Databases", "H2 Datenbank");
        lang.put("InstallationException",
                "Haushaltsbuch fehlen wichtige Dateien. Das Programm muss neu installiert werden.");
    }

    @Override
    public Enumeration<String> getKeys() {
        return lang.keys();
    }

    @Override
    protected Object handleGetObject(String key) {
        return lang.get(key);
    }

}