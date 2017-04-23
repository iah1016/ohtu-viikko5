
package ohtu.intjoukkosovellus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class IntJoukkoYksiparametrisellaKonstruktorillaTest extends IntJoukkoTest {
    
    @Before
    @Override
    public void setUp() {
        joukko = new IntJoukko(3);
        joukko.lisaaLukuJoukkoon(10);
        joukko.lisaaLukuJoukkoon(3);
    }
    
    // perii kaikki testit luokasta IntJoukkoTest
}
