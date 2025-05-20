package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CompteComptableTest {
    @Test
    public void testGetByNumero() {
        CompteComptable compte1 = new CompteComptable(1, "Compte 1");
        CompteComptable compte2 = new CompteComptable(2, "Compte 2");
        CompteComptable compte3 = new CompteComptable(3, "Compte 3");
        List<CompteComptable> comptes = Arrays.asList(compte1, compte2, compte3);

        CompteComptable result = CompteComptable.getByNumero(comptes, 2);
        Assert.assertNotNull(result);
        assertEquals(2L, (long) result.getNumero());
        assertEquals("Compte 2", result.getLibelle());

        result = CompteComptable.getByNumero(comptes, 4);
        Assert.assertNull(result);

        comptes = Arrays.asList(compte1, null, compte3);
        result = CompteComptable.getByNumero(comptes, 3);
        Assert.assertNotNull(result);
        assertEquals(3L, (long) result.getNumero());
    }
}
