package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortillaSaldoaAlussa() {
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenOnnistuu() {
        kortti.lataaRahaa(900);
        
        assertEquals(910, kortti.saldo());
    }
    
    @Test
    public void saldoVaheneeJosRahaaTarpeeksi() {
        kortti.otaRahaa(8);
        
        assertEquals(2, kortti.saldo());
    }
    
    @Test
    public void saldoEiMuutuJosRahaEiRiita() {
        kortti.otaRahaa(150);
        
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void saldonOttaminenPalauttaaTrueJosRahaRiittaa() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void merkkijonoEsitysOikein() {
        assertEquals("saldo: 0.10", kortti.toString());
    }
}
