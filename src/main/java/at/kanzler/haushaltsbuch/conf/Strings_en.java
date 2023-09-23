package at.kanzler.haushaltsbuch.conf;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class Strings_en extends ResourceBundle {

    private Hashtable<String, String> lang = new Hashtable<String, String>();

    public Strings_en() {
        lang.put("Application", "Haushaltsbuch");
        lang.put("ApplicationRunning", "Haushaltsbuch is already running!");
        lang.put("Accounting", "Accounting");
        lang.put("Search", "Search");
        lang.put("MonthOverview", "Overview Month");
        lang.put("YearOverviewAssets", "Overview Year Assets");
        lang.put("YearOverviewCosttypes", "Overview Year Categories");
        lang.put("Administration", "Administration");
        lang.put("QuickSearch", "Quicksearch:");
        lang.put("Tools", "Tools");
        lang.put("Quit", "Quit Haushaltsbuch");
        lang.put("Backup", "Backup database...");
        lang.put("Restore", "Restore database...");
        lang.put("Preferences", "Preferences");
        lang.put("About", "About Haushaltsbuch");
        lang.put("ZipFiles", "Zip Files");
        lang.put("BackupFailed", "Database backup failed!");
        lang.put("BackupSuccessfull", "Database backup was successfull!");
        lang.put("RestoreFailed", "Database restore failed!");
        lang.put("RestoreSuccessfull",
                "Database restore was successfull!\nHaushaltsbuch will be restarted!\n\nAttention: The password might have been resetted!");
        lang.put("LoginText", "Please enter your password:");
        lang.put("Login", "Login");
        lang.put("WrongPasswd", "The password is wrong!");
        lang.put("EnterPasswd", "Please enter your password!");
        lang.put("Summary", "Summary");
        lang.put("Booking", "Booking");
        lang.put("Abort", "Cancel");
        lang.put("Save", "Save");
        lang.put("SortInputDate", "<a>Order by date of entry</a>");
        lang.put("Edit", "Edit");
        lang.put("Delete", "Delete");
        lang.put("DisposableAmount", "Disposable Assets");
        lang.put("OfflimitAmount", "Offlimit Assets");
        lang.put("TotalAssets", "Total Assets");
        lang.put("TotalRevenues", "Revenues");
        lang.put("TotalExpenses", "Expenses");
        lang.put("TotalSavings", "Savings");
        lang.put("Date", "Date");
        lang.put("BookingText", "Bookingtitle");
        lang.put("Amount", "Amount");
        lang.put("Costtype", "Category");
        lang.put("FromAccount", "From");
        lang.put("ToAccount", "To");
        lang.put("BookMonth", "Bookingmonth");
        lang.put("SearchTerms", "Searchterms");
        lang.put("Clear", "Clear");
        lang.put("SearchResults", "Searchresults");
        lang.put("FromDate", "After");
        lang.put("ToDate", "Before");
        lang.put("Sum", "Total");
        lang.put("3TriesPasswd",
                "You have entered the wrong password three times now!\nThe application will be closed!");
        lang.put("DBError", "There is no connection to the database. Please restart the application.");
        lang.put("AddCosttypegroup", "Add new costtypegroup");
        lang.put("Title", "Title");
        lang.put("Category", "Category");
        lang.put("Type", "Type");
        lang.put("ShortTitle", "Short title");
        lang.put("Status", "Status");
        lang.put("ClosingDate", "Valid through");
        lang.put("BeginningYear", "Beginning of year");
        lang.put("Balance", "Balance");
        lang.put("EmptyTitle", "Please enter a title!");
        lang.put("EmptyShortTitle", "Please enter a short title!");
        lang.put("EmptyType", "Please choose a type for this account!");
        lang.put("EmptyStatus", "Please choose a status for this account!");
        lang.put("EmptyClosingDate", "Please select a valid through date!");
        lang.put("PastClosingDate", "Valid through has to be in the future!");
        lang.put("AddAccount", "Add new account");
        lang.put("Accounts", "Accounts");
        lang.put("Costtypes", "Costtypes");
        lang.put("Costtypegroups", "Costtypegroups");
        lang.put("AddCosttype", "Add new costtype");
        lang.put("Costtypegroup", "Costtypegroup");
        lang.put("EmptyCTTitle", "Please enter a title!");
        lang.put("EmptyCTShortTitle", "Please enter a short title!");
        lang.put("EmptyCosttypegroup", "Please choose a costtypegroup!");
        lang.put("Language", "Language");
        lang.put("Account", "Account");
        lang.put("Continue",
                "You have unsaved changes, do you really want to continue?\nYour changes will be discarded!");
        lang.put("Month", "Month");
        lang.put("Close", "Close");
        lang.put("Year", "Year");
        lang.put("BackupDB", "Creating database backup. Please wait.");
        lang.put("RestoreDB", "Restoring database. Please wait.");
        lang.put("Update", "Check for Updates...");
        lang.put("H2Databases", "H2 Databases");
        lang.put("InstallationException", "Some important files are missing. Please reinstall the application.");
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