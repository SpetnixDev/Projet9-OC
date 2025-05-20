package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
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

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3_NoLines() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3_NoCreditLine() throws Exception {
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

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG3_NoDebitLine() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null,
                null, new BigDecimal(123)));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null,
                null, new BigDecimal(123)));

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
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
        //manager.checkEcritureComptableUnit(vEcritureComptable);

        try {
            manager.checkEcritureComptableUnit(vEcritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5_AnneeInvalide() throws Exception {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("AC-2023/00001");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(100), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, null, new BigDecimal(100)));

        try {
            manager.checkEcritureComptableUnit(ecritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableUnitRG5_CodeInvalide() throws Exception {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("BQ-2025/00001");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(100), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, null, new BigDecimal(100)));

        try {
            manager.checkEcritureComptableUnit(ecritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void checkEcritureComptableUnitRG5() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("AC-2025/00001");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(100), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, null, new BigDecimal(100)));

        manager.checkEcritureComptableUnit(ecritureComptable);
    }

    @Test
    public void checkAddReferenceNewSequence() throws NotFoundException {
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
    public void checkAddReferenceExistingSequence() throws NotFoundException {
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

    @Test
    public void checkEcritureComptable_shouldNotThrowException_whenValidEcriture() throws FunctionalException, NotFoundException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");

        ecritureComptable.setReference("AC-2025/00015");
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(100), null));
        ecritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, null, new BigDecimal(100)));

        Mockito.when(comptabiliteDao.getEcritureComptableByRef(ecritureComptable.getReference())).thenThrow(new NotFoundException("Not found"));

        manager.checkEcritureComptable(ecritureComptable);
    }

    @Test
    public void checkEcritureComptableContext_shouldNotThrowIfNoReference() throws FunctionalException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");

        manager.checkEcritureComptableContext(ecritureComptable);
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContext_shouldThrowIfIdIsNull() throws FunctionalException, NotFoundException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("AC-2025/00015");

        EcritureComptable existingEcriture = new EcritureComptable();
        existingEcriture.setId(1);
        existingEcriture.setJournal(new JournalComptable("AC", "Achat"));
        existingEcriture.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        existingEcriture.setLibelle("Libelle");
        existingEcriture.setReference("AC-2025/00015");

        Mockito.when(comptabiliteDao.getEcritureComptableByRef(ecritureComptable.getReference())).thenReturn(existingEcriture);

        try {
            manager.checkEcritureComptableContext(ecritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test(expected = FunctionalException.class)
    public void checkEcritureComptableContext_shouldThrowIfIdIsDifferent() throws FunctionalException, NotFoundException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(2);
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("AC-2025/00015");

        EcritureComptable existingEcriture = new EcritureComptable();
        existingEcriture.setId(1);
        existingEcriture.setJournal(new JournalComptable("AC", "Achat"));
        existingEcriture.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        existingEcriture.setLibelle("Libelle");
        existingEcriture.setReference("AC-2025/00015");

        Mockito.when(comptabiliteDao.getEcritureComptableByRef(ecritureComptable.getReference())).thenReturn(existingEcriture);

        try {
            manager.checkEcritureComptableContext(ecritureComptable);
            Assert.fail("Une FunctionalException aurait dû être levée");
        } catch (FunctionalException e) {
            System.out.println("Message d'erreur capturé : " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void checkEcritureComptableContext_shouldNotThrowIfIdIsTheSame() throws FunctionalException, NotFoundException {
        EcritureComptable ecritureComptable = new EcritureComptable();
        ecritureComptable.setId(1);
        ecritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptable.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        ecritureComptable.setLibelle("Libelle");
        ecritureComptable.setReference("AC-2025/00015");

        EcritureComptable existingEcriture = new EcritureComptable();
        existingEcriture.setId(1);
        existingEcriture.setJournal(new JournalComptable("AC", "Achat"));
        existingEcriture.setDate(new GregorianCalendar(2025, Calendar.MARCH, 1).getTime());
        existingEcriture.setLibelle("Libelle");
        existingEcriture.setReference("AC-2025/00015");

        Mockito.when(comptabiliteDao.getEcritureComptableByRef(ecritureComptable.getReference())).thenReturn(existingEcriture);

        manager.checkEcritureComptableContext(ecritureComptable);
    }

    private int getCurrentYear() {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
    }
}
