/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ohtu.verkkokauppa;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Ilja Häkkinen
 */
public class KauppaTest {

    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;
    Kauppa kauppa;

    public KauppaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        varasto = mock(Varasto.class);

        when(viite.uusi()).thenReturn(42);

        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.saldo(2)).thenReturn(7);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "olut", 7));

        kauppa = new Kauppa(varasto, pankki, viite);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ostetaanYksiTuoteJaMetodiaTilisiirtoKutsutaanOikein() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(5));
    }

    @Test
    public void ostetaanKaksiEriTuotettaJaMetodiaTilisiirtoKutsutaanOikein() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(12));
    }

    @Test
    public void ostetaanKaksiSamaaTuotettaJaMetodiaTilisiirtoKutsutaanOikein() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(14));
    }

    @Test
    public void tilisiirtoaKutsutaanOikeinKunYhtaTuotettaOnJaToistaEi() {
        kauppa.aloitaAsiointi();
        when(varasto.saldo(1)).thenReturn(0);
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(7));
    }

    @Test
    public void aloitaAsiointiNollaaEdellisenOstoksenTiedot() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);

        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(10));
    }

    @Test
    public void kauppaPyytaaUudenViitenumeronJokaiselleMaksutapahtumalle() {
        int[] viitteet = {20336, 20349, 20352};
        when(viite.uusi())
                .thenReturn(viitteet[0])
                .thenReturn(viitteet[1])
                .thenReturn(viitteet[2]);
        
        for (int i = 0; i < 2; i++) {
            kauppa.aloitaAsiointi();
            kauppa.lisaaKoriin(1);
            kauppa.tilimaksu("pekka", "12345");
            verify(pankki).tilisiirto(
                    eq("pekka"),
                    eq(viitteet[i]),
                    eq("12345"),
                    anyString(),
                    eq(5));
        }
    }
    
    @Test
    public void tuotteenPoistaminenKoristaVahentaaSenHinnanSummasta() {
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(2);
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(2);

        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(
                eq("pekka"),
                eq(42),
                eq("12345"),
                anyString(),
                eq(12));
    }
}
