package vexybot;

public enum Status {
    START,
    CHOOSENOTE,
    CHOOSENOTEFORDEL,
    LANGUAGESELECTION;

    @Override
    public String toString() {
        return Status.this.name();
    }
}
