package ohtu.intjoukkosovellus;

import java.util.Arrays;

public class IntJoukko {

    public final static int OLETUSKAPASITEETTI = 5, // aloitustalukon koko
            OLETUSKASVATUS = 5;  // luotava uusi taulukko on 
    // näin paljon isompi kuin vanha
    private int kasvatuskoko;     // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] lukutaulukko;     // Joukon luvut säilytetään taulukon alkupäässä. 
    private int alkioidenLkm;    // Tyhjässä joukossa alkioiden_määrä on nolla. 

    public IntJoukko() {
        this(OLETUSKAPASITEETTI, OLETUSKASVATUS);
    }

    public IntJoukko(int kapasiteetti) {
        this(kapasiteetti, OLETUSKASVATUS);
    }

    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        kapasiteetinJaKasvatuskoonValidointi(kapasiteetti, kasvatuskoko);
        alustaLukujono(kapasiteetti);
        alkioidenLkm = 0;
        this.kasvatuskoko = kasvatuskoko;
    }

    private void alustaLukujono(int kapasiteetti) {
        lukutaulukko = new int[kapasiteetti];
        for (int i = 0; i < lukutaulukko.length; i++) {
            lukutaulukko[i] = 0;
        }
    }

    private void kapasiteetinJaKasvatuskoonValidointi(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) {
            throw new IndexOutOfBoundsException("Kapasiteetti liian pieni");
        }
        if (kasvatuskoko < 0) {
            throw new IndexOutOfBoundsException("Kasvatuskoko liian pieni");
        }
    }

    public boolean lisaaLukuJoukkoon(int luku) {
        return lukuLisataanTyhjaanJoukkoon(luku)
                || lisaaLukuJosSeEiJoKuuluJoukkoon(luku);
    }

    private boolean lukuLisataanTyhjaanJoukkoon(int luku) {
        if (alkioidenLkm == 0) {
            lukutaulukko[0] = luku;
            alkioidenLkm++;
            return true;
        }
        return false;
    }

    private boolean lisaaLukuJosSeEiJoKuuluJoukkoon(int luku) {
        if (!kuuluuJoukkoon(luku)) {
            paivitaLukutaulukkoLisayksenJalkeen(luku);
            return true;
        }
        return false;
    }

    private void paivitaLukutaulukkoLisayksenJalkeen(int luku) {
        lukutaulukko[alkioidenLkm] = luku;
        alkioidenLkm++;
        kasvataLukutaulukonKokoaJosTaulukkoTayttyy();
    }

    private void kasvataLukutaulukonKokoaJosTaulukkoTayttyy() {
        if (alkioidenLkm % lukutaulukko.length == 0) {
            int[] taulukkoOld = lukutaulukko;
            lukutaulukko = new int[alkioidenLkm + kasvatuskoko];
            kopioiTaulukko(taulukkoOld, lukutaulukko, 0);
        }
    }

    public boolean kuuluuJoukkoon(int luku) {
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luku == lukutaulukko[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean poistaLukuJoukosta(int luku) {
        int poistokohta = haePoistokohtaLukutaulukosta(luku);
        if (poistokohta == -1) {
            return false;
        }
        paivitaLukutaulukkoPoistonJalkeen(poistokohta);
        return true;
    }

    private int haePoistokohtaLukutaulukosta(int luku) {
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luku == lukutaulukko[i]) {
                return i;
            }
        }
        return -1;
    }

    private void paivitaLukutaulukkoPoistonJalkeen(int poistokohta) {
        for (int j = poistokohta; j < alkioidenLkm - 1; j++) {
            int apu = lukutaulukko[j];
            lukutaulukko[j] = lukutaulukko[j + 1];
            lukutaulukko[j + 1] = apu;
        }
        alkioidenLkm--;
    }

    private void kopioiTaulukko(int[] vanha, int[] uusi, int alkukohta) {
        for (int i = alkukohta; i < vanha.length; i++) {
            uusi[i] = vanha[i];
        }

    }

    public int mahtavuus() {
        return alkioidenLkm;
    }

    @Override
    public String toString() {
        if (alkioidenLkm == 0) {
            return "{}";
        } else if (alkioidenLkm == 1) {
            return "{" + lukutaulukko[0] + "}";
        }
        return "{" + kaksioidenTaiSuurempienJoukkojenAlkiotToStringissa() + "}";
    }

    private String kaksioidenTaiSuurempienJoukkojenAlkiotToStringissa() {
        String tuotos = "";
        for (int i = 0; i < alkioidenLkm - 1; i++) {
            tuotos += lukutaulukko[i] + ", ";
        }
        tuotos += lukutaulukko[alkioidenLkm - 1];
        return tuotos;
    }

    public int[] toIntArray() {
        int[] taulu = new int[alkioidenLkm];
        for (int i = 0; i < taulu.length; i++) {
            taulu[i] = lukutaulukko[i];
        }
        return taulu;
    }

    public static IntJoukko yhdiste(IntJoukko joukkoA, IntJoukko joukkoB) {
        IntJoukko yhdistejoukko = new IntJoukko();
        int[] aTaulu = joukkoA.toIntArray();
        int[] bTaulu = joukkoB.toIntArray();
        lisaaKaikkiLuvutTaulustaJoukkoon(aTaulu, yhdistejoukko);
        lisaaKaikkiLuvutTaulustaJoukkoon(bTaulu, yhdistejoukko);
        return yhdistejoukko;
    }

    private static void lisaaKaikkiLuvutTaulustaJoukkoon(
            int[] taulu, IntJoukko joukko) {
        for (int i = 0; i < taulu.length; i++) {
            joukko.lisaaLukuJoukkoon(taulu[i]);
        }
    }

    public static IntJoukko leikkaus(IntJoukko joukkoA, IntJoukko joukkoB) {
        IntJoukko leikkausjoukko = new IntJoukko();
        int[] aTaulu = joukkoA.toIntArray();
        int[] bTaulu = joukkoB.toIntArray();
        lisaaKaikkiLuvutTaulustaJoukkoon(
                kahdenTaulunLeikkaus(aTaulu, bTaulu), leikkausjoukko);
        return leikkausjoukko;
    }

    private static int[] kahdenTaulunLeikkaus(int[] aTaulu, int[] bTaulu) {
        return Arrays.stream(aTaulu)
                .filter(x -> Arrays.stream(bTaulu).anyMatch(y -> y == x))
                .toArray();
    }

    public static IntJoukko erotus(IntJoukko joukkoA, IntJoukko joukkoB) {
        IntJoukko erotusjoukko = new IntJoukko();
        int[] aTaulu = joukkoA.toIntArray();
        int[] bTaulu = joukkoB.toIntArray();
        lisaaKaikkiLuvutTaulustaJoukkoon(
                taulujenErotus(aTaulu, bTaulu), erotusjoukko);
        return erotusjoukko;
    }

    private static int[] taulujenErotus(int[] aTaulu, int[] bTaulu) {
        return Arrays.stream(aTaulu)
                .filter(x -> Arrays.stream(bTaulu).noneMatch(y -> y == x))
                .toArray();
    }

}
