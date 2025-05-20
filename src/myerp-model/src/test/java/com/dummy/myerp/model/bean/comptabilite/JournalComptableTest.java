package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JournalComptableTest {
    @Test
    public void testGetByCode() {
        JournalComptable journal1 = new JournalComptable("AC", "Achat");
        JournalComptable journal2 = new JournalComptable("VE", "Vente");
        JournalComptable journal3 = new JournalComptable("BQ", "Banque");
        List<JournalComptable> journalList = Arrays.asList(journal1, journal2, journal3);

        JournalComptable result = JournalComptable.getByCode(journalList, "VE");
        assertNotNull(result);
        assertEquals("VE", result.getCode());
        assertEquals("Vente", result.getLibelle());

        result = JournalComptable.getByCode(journalList, "XX");
        assertNull(result);

        journalList = Arrays.asList(journal1, null, journal3);
        result = JournalComptable.getByCode(journalList, "BQ");
        assertNotNull(result);
        assertEquals("BQ", result.getCode());
    }
}