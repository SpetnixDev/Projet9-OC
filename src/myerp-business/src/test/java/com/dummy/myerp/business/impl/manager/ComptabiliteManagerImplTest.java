package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.dummy.myerp.technical.exception.FunctionalException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComptabiliteManagerImplTest {
    @Mock
    private DaoProxy daoProxy;

    @Mock
    private ComptabiliteDao comptabiliteDao;

    @InjectMocks
    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @Before
    public void init() throws Exception {
        AbstractBusinessManager.configure(null, daoProxy, null);
        Mockito.when(daoProxy.getComptabiliteDao()).thenReturn(comptabiliteDao);
    }

    @Test
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitViolation() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG2() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        manager.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void testAddReferenceNewSequence() throws NotFoundException {
        EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());

        int currentYear = getCurrentYear();

        Mockito.when(comptabiliteDao.getSequenceEcritureComptable("AC", currentYear)).thenThrow(new NotFoundException("Not found"));
        Mockito.doNothing().when(comptabiliteDao).insertSequenceEcritureComptable(Mockito.any(SequenceEcritureComptable.class));

        manager.addReference(vEcritureComptable);

        Assert.assertEquals("AC-" + currentYear + "/00001", vEcritureComptable.getReference());

        Mockito.verify(comptabiliteDao).insertSequenceEcritureComptable(
                ArgumentMatchers.argThat(seq ->
                        seq.getJournalCode().equals("AC")
                                && seq.getAnnee() == currentYear
                                && seq.getDerniereValeur() == 1
                )
        );
    }

    @Test
    public void testAddReferenceExistingSequence() throws NotFoundException {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());

        int currentYear = getCurrentYear();

        SequenceEcritureComptable sequence = new SequenceEcritureComptable("AC", currentYear, 15);

        Mockito.when(comptabiliteDao.getSequenceEcritureComptable("AC", currentYear)).thenReturn(sequence);

        manager.addReference(vEcritureComptable);

        Assert.assertEquals("AC-" + currentYear + "/00016", vEcritureComptable.getReference());

        Mockito.verify(comptabiliteDao).updateSequenceEcritureComptable(
                ArgumentMatchers.argThat(seq ->
                        seq.getJournalCode().equals("AC")
                                && seq.getAnnee() == currentYear
                                && seq.getDerniereValeur() == 16
                )
        );
    }

    private int getCurrentYear() {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
    }
}
