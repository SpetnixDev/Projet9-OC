package com.dummy.myerp.testbusiness.business;

import com.dummy.myerp.business.contrat.manager.ComptabiliteManager;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ComptabiliteManagerIntegrationTest extends BusinessTestCase {
    private ComptabiliteManager comptabiliteManager;
    private TransactionStatus vTS;

    @Before
    public void setUp() {
        comptabiliteManager = getBusinessProxy().getComptabiliteManager();
        vTS = getTransactionManager().beginTransactionMyERP();
    }

    @Test
    public void testInsertEcritureComptable() throws FunctionalException {
        final EcritureComptable ecriture = new EcritureComptable();
        ecriture.setJournal(new JournalComptable("AC", "Achat"));
        ecriture.setDate(new Date());
        ecriture.setLibelle("Test Integration - Insert");
        ecriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401), null, new BigDecimal("200.50"), null));
        ecriture.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401), null, null, new BigDecimal("200.50")));

        try {
            comptabiliteManager.insertEcritureComptable(ecriture);
            Assert.assertNotNull("L'id de l'écriture ne doit pas être null.", ecriture.getId());
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Test
    public void testUpdateEcritureComptable() throws FunctionalException {
        final EcritureComptable ecriture = comptabiliteManager.getListEcritureComptable().get(0);
        ecriture.setLibelle("Test Integration - Update");

        try {
            comptabiliteManager.updateEcritureComptable(ecriture);
            final EcritureComptable ecritureUpdated = comptabiliteManager.getListEcritureComptable().stream().filter(e -> e.getId().equals(ecriture.getId())).findFirst().get();
            Assert.assertEquals("Le libellé de l'écriture n'a pas été mis à jour.", ecriture.getLibelle(), ecritureUpdated.getLibelle());
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Test
    public void testDeleteEcritureComptable() throws FunctionalException {
        final EcritureComptable ecriture = comptabiliteManager.getListEcritureComptable().get(0);

        try {
            comptabiliteManager.deleteEcritureComptable(ecriture.getId());
            Assert.assertFalse("L'écriture comptable n'a pas été supprimée.", comptabiliteManager.getListEcritureComptable().stream().anyMatch(e -> e.getId().equals(ecriture.getId())));
            getTransactionManager().commitMyERP(vTS);
            vTS = null;
        } finally {
            getTransactionManager().rollbackMyERP(vTS);
        }
    }

    @Test
    public void testGetListsIntegration() {
        List<CompteComptable> comptes = comptabiliteManager.getListCompteComptable();
        List<JournalComptable> journaux = comptabiliteManager.getListJournalComptable();
        List<EcritureComptable> ecritures = comptabiliteManager.getListEcritureComptable();

        Assert.assertNotNull("La liste des comptes comptables ne doit pas être nulle", comptes);
        Assert.assertFalse("La liste des comptes comptables ne doit pas être vide", comptes.isEmpty());

        Assert.assertNotNull("La liste des journaux comptables ne doit pas être nulle", journaux);
        Assert.assertFalse("La liste des journaux comptables ne doit pas être vide", journaux.isEmpty());

        Assert.assertNotNull("La liste des écritures comptables ne doit pas être nulle", ecritures);
        Assert.assertFalse("La liste des écritures comptables ne doit pas être vide", ecritures.isEmpty());
    }
}