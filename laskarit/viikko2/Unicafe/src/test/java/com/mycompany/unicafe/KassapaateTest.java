package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class KassapaateTest {

    Kassapaate kassa;
    Maksukortti kortti;

    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(10000);
    }

    @Test
    public void uudessaKassassaRahatOikein() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void uudessaKassassaMyynnitOikein() {
        assertEquals(0, kassa.edullisiaLounaitaMyyty()+kassa.maukkaitaLounaitaMyyty());
    }
        
    @Test
    public void kassanSummaKasvaaEdullisessaKateisostossa() {
        kassa.syoEdullisesti(300);
       
        assertEquals(100240, kassa.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinEdullisessaKateisostossa() {
        assertEquals(60, kassa.syoEdullisesti(300));
    }
    
    @Test
    public void kassanSummaKasvaaMaukkaassaKateisostossa() {
        kassa.syoMaukkaasti(500);
       
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahaOikeinMaukkaassaKateisostossa() {
        assertEquals(60, kassa.syoMaukkaasti(460));
    }
    
    @Test
    public void myytyjenLounaidenMaaraKasvaaEdullisessaKateisostossa() {
        kassa.syoEdullisesti(250);
        
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenLounaidenMaaraKasvaaMaukkaassaKateisostossa() {
        kassa.syoMaukkaasti(500);
        
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kassanSummaEiMuutuJosEdullisenKateismaksuEiRiita() {
        kassa.syoEdullisesti(1);
        
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kassanSummaEiMuutuJosMaukkaanKateismaksuEiRiita() {
        kassa.syoMaukkaasti(1);
        
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void vaihtorahatTakaisinJosEdullisenKateismaksuEiRiita() {
        assertEquals(1, kassa.syoEdullisesti(1));
    }
    
    @Test
    public void vaihtorahatTakaisinJosMaukkaanKateismaksuEiRiita() {
        assertEquals(1, kassa.syoMaukkaasti(1));
    }
    
    @Test
    public void myytyjenMaaraEiMuutuJosEdullisenKateismaksuEiRiita() {
        kassa.syoEdullisesti(0);
        
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaaraEiMuutuJosMaukkaanKateismaksuEiRiita() {
        kassa.syoMaukkaasti(0);
        
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoOnnistuuJosKortillaRahaa() {
        assertTrue(kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void maukkaanOstoOnnistuuJosKortillaRahaa() {
        assertTrue(kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void edullistenMyytyjenMaaraKasvaaJosKortillaRahaa() {
        kassa.syoEdullisesti(kortti);
        
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukkaidenMyytyjenMaaraKasvaaJosKortillaRahaa() {
        kassa.syoMaukkaasti(kortti);
        
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenOstoEiOnnistuJosKortillaEiRahaa() {
        assertFalse(kassa.syoEdullisesti(new Maksukortti(0)));
    }
    
    @Test
    public void maukkaanOstoEiOnnistuJosKortillaEiRahaa() {
        assertFalse(kassa.syoMaukkaasti(new Maksukortti(0)));
    }
    
    @Test
    public void kortinSaldoEiMuutuJosOstetaanEdullinenIlmanRahaa() {
        kortti = new Maksukortti(0);
        kassa.syoEdullisesti(kortti);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void kortinSaldoEiMuutuJosOstetaanMaukasIlmanRahaa() {
        kortti = new Maksukortti(0);
        kassa.syoMaukkaasti(kortti);
        assertEquals(0, kortti.saldo());
    }
    
    @Test
    public void myytyjenEdullistenMaaraEiMuutuJosKortillaEiRahaa() {
        kortti = new Maksukortti(0);
        kassa.syoEdullisesti(kortti);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaukkaidenMaaraEiMuutuJosKortillaEiRahaa() {
        kortti = new Maksukortti(0);
        kassa.syoMaukkaasti(kortti);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kassanSummaEiMuutuEdullisessaKorttiostossa() {
        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kassanSummaEiMuutuMaukkaassaKorttiostossa() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
 
    @Test
    public void kortinSaldoMuuttuuKunSiihenLadataanRahaa() {
        kassa.lataaRahaaKortille(kortti, 10);
        assertEquals(10010, kortti.saldo());
    }
 
    @Test
    public void kassanSummaMuuttuuKunKortilleLadataanRahaa() {
        kassa.lataaRahaaKortille(kortti, 10);
        assertEquals(100010, kassa.kassassaRahaa());
    }
    
    @Test
    public void kortinSaldoEiMuutuJosSiihenLadataanNegatiivinenMaara() {
        kassa.lataaRahaaKortille(kortti, -1);
        assertEquals(10000, kortti.saldo());
    } 
    
    @Test
    public void kassanSummaEiMuutuJosKorttiaLadataanNegatiivisellaMaaralla() {
        kassa.lataaRahaaKortille(kortti, -1);
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
