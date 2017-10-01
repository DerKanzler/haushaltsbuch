package haushaltsbuch.logic;

import haushaltsbuch.bean.Buchung;
import haushaltsbuch.bean.Konto;
import haushaltsbuch.bean.Kontostand;
import haushaltsbuch.bean.Kostenart;
import haushaltsbuch.bean.Kostenartgruppe;
import haushaltsbuch.bean.Kostenartsaldo;
import haushaltsbuch.bean.util.SearchBuchung;
import haushaltsbuch.conf.HB;
import haushaltsbuch.dao.BuchungDAO;
import haushaltsbuch.dao.KontoDAO;
import haushaltsbuch.dao.KontostandDAO;
import haushaltsbuch.dao.KostenartDAO;
import haushaltsbuch.dao.KostenartsaldoDAO;
import haushaltsbuch.util.Tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class LogicMonatUE {
	
	private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> koart = new TreeMap<Kostenartgruppe, Vector<Kostenartsaldo>>();
	
	private BigDecimal disposableSum = new BigDecimal(0);
	private BigDecimal offlimitSum = new BigDecimal(0);
	private BigDecimal totalRevenues = new BigDecimal(0);
	private BigDecimal totalExpenses = new BigDecimal(0);
	
	public LogicMonatUE(Date date) {
		if (date.equals(Tools.getLastMonth())) {
			for (Konto k: KontoDAO.instance().getAllValid()) {
				if (k.isDisposable()) {
					disposableSum = disposableSum.add(k.getSaldo());
				}
				else offlimitSum = offlimitSum.add(k.getSaldo());
			}
			getCosttypeGroups();
		}
		else {
			for (Kontostand kost: KontostandDAO.instance().getKontostaende(date)) {
				if (kost.getKonto().isDisposable()) {
					disposableSum = disposableSum.add(kost.getKtostsaldo());
				}
				else offlimitSum = offlimitSum.add(kost.getKtostsaldo());
			}
			getCosttypeGroups(date);
		}
	}
	
	public Vector<Buchung> getBookings(SearchBuchung sb) {
		return BuchungDAO.instance().search(sb);
	}
	
	public Vector<Kontostand> getAccounts(Date date) {
		Vector<Kontostand> kost = new Vector<Kontostand>();
		if (date.equals(Tools.getLastMonth())) {
			for (Konto konto: KontoDAO.instance().getAll()) {
				Kontostand ks = new Kontostand();
				ks.setKonto(konto);
				ks.setKtostdat(konto.getBuchdat());
				ks.setKtostsaldo(konto.getSaldo());
				if (displayAccount(date, ks)) kost.add(ks);
			}
		}
		else {
			for (Kontostand ks: KontostandDAO.instance().getKontostaende(date)) {
				if (displayAccount(date, ks)) kost.add(ks);
			}
		}
		return kost;
	}
	
	public SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypes() {
		return koart;
	}
	
	private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypeGroups() {
		Vector<Kostenartsaldo> koartsaldi = new Vector<Kostenartsaldo>();
		for (Kostenart koart: KostenartDAO.instance().getAll()) {
			Kostenartsaldo k = new Kostenartsaldo();
			k.setKoart(koart);
			k.setKoartmonsaldo(koart.getKoartsaldo());
			k.setKoartsalddat(Tools.getLastMonth());
			koartsaldi.add(k);
		}
		return createCosttypeGroupsHashMap(koartsaldi);
	}
	
	private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> getCosttypeGroups(Date date) {
		return createCosttypeGroupsHashMap(KostenartsaldoDAO.instance().getKostenartsaldi(date));
	}
		
	private SortedMap<Kostenartgruppe, Vector<Kostenartsaldo>> createCosttypeGroupsHashMap(Vector<Kostenartsaldo> koartsaldi) {
		for (Kostenartsaldo ct: koartsaldi) {
			if (ct.getKoartmonsaldo().compareTo(new BigDecimal(0)) != 0) {
				if (ct.getKoart().getKoartgrp().getKoartgrpkat().equals(HB.AUSGABE)) {
					totalExpenses = totalExpenses.add(ct.getKoartmonsaldo());
				}
				else totalRevenues = totalRevenues.add(ct.getKoartmonsaldo());
				if (!koart.containsKey(ct.getKoart().getKoartgrp())) {
					Vector<Kostenartsaldo> cts = new Vector<Kostenartsaldo>();
					cts.add(ct);
					koart.put(ct.getKoart().getKoartgrp(), cts);
				}
				else koart.get(ct.getKoart().getKoartgrp()).add(ct);
			}
		}
		return koart;
	}	
	
	public BigDecimal getDisposableSum() {
		return disposableSum;
	}
	
	public BigDecimal getOffLimitSum() {
		return offlimitSum;
	}
	
	public BigDecimal getTotalAssets() {
		return getDisposableSum().add(getOffLimitSum());
	}
	
	public BigDecimal getTotalRevenues() {
		return totalRevenues;
	}
	
	public BigDecimal getTotalExpenses() {
		return totalExpenses;
	}
	
	public BigDecimal getTotalSavings() {
		return getTotalRevenues().subtract(getTotalExpenses());
	}
	
	private Boolean displayAccount(Date date, Kontostand ks) {
		// Falls das Abldaufdatums des Kontos in der Zukunft liegt (egal ob geschlossen oder offen), wird Konto angezeigt
		// Aber auch falls das Konto noch offen ist .
		// Oder wenn es in dem Monat eine Buchung gegeben hat.
		if (ks.getKonto().getAbldat().compareTo(date) >= 0 || ks.getKonto().isValid() || ks.getKtostsaldo().compareTo(new BigDecimal(0)) != 0) {
			return true;
		}
		else return false;
	}
	
}