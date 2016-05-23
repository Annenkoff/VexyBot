package vexybot.aggregator.model;

import java.io.IOException;

public interface Strategy {
    String getInfo(String searchString) throws IOException;
}
