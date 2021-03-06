package su.vexy.vexybot;

/**
 * Содержит основные этапы общения с ботом.
 */
public enum Status {
    START,
    CHOOSE_NOTE,
    CHOOSE_NOTE_FOR_DELETE,
    LANGUAGE_SELECTION,
    START_LANGUAGE_SELECTION,
    ON_SELECT_STANDART_LOCATION,
    START_SELECT_STANDART_LOCATION,
    GET_WEATHER;

    @Override
    public String toString() {
        return Status.this.name();
    }
}
