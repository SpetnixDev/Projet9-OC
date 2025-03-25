package com.dummy.myerp.model.bean.comptabilite;


/**
 * Bean représentant une séquence pour les références d'écriture comptable
 */
public class SequenceEcritureComptable {

    // ==================== Attributs ====================
    /** Le code du journal */
    private String journalCode;

    /** L'année */
    private Integer annee;
    /** La dernière valeur utilisée */
    private Integer derniereValeur;

    // ==================== Constructeurs ====================
    /**
     * Constructeur
     */
    public SequenceEcritureComptable() {
    }

    /**
     * Constructeur
     *
     * @param pAnnee -
     * @param pDerniereValeur -
     */
    public SequenceEcritureComptable(String pJournalCode, Integer pAnnee, Integer pDerniereValeur) {
        journalCode = pJournalCode;
        annee = pAnnee;
        derniereValeur = pDerniereValeur;
    }


    // ==================== Getters/Setters ====================
    public String getJournalCode() {
        return journalCode;
    }
    public void setJournalCode(String pJournalCode) {
        journalCode = pJournalCode;
    }
    public Integer getAnnee() {
        return annee;
    }
    public void setAnnee(Integer pAnnee) {
        annee = pAnnee;
    }
    public Integer getDerniereValeur() {
        return derniereValeur;
    }
    public void setDerniereValeur(Integer pDerniereValeur) {
        derniereValeur = pDerniereValeur;
    }


    // ==================== Méthodes ====================
    @Override
    public String toString() {
        final String vSEP = ", ";
        return this.getClass().getSimpleName() + "{" +
                "journalCode=" + journalCode +
                vSEP +
                "annee=" + annee +
                vSEP + "derniereValeur=" + derniereValeur +
                "}";
    }
}
