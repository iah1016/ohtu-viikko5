package ohtu;

/**
 *
 * @author Ilja Häkkinen
 */
public enum TennisTerminology {

    ALL("All"), DEUCE("Deuce"), ADVANTAGE("Advantage"), WIN("Win for"),
    LOVE("Love"), FIFTEEN("Fifteen"), THIRTY("Thirty"), FORTY("Forty");

    private final String term;

    private TennisTerminology(String term) {
        this.term = term;
    }

    public String term() {
        return term;
    }
}
