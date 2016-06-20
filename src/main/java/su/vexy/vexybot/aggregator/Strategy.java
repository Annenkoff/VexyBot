package su.vexy.vexybot.aggregator;

import java.io.IOException;

public interface Strategy {
    String getInfo(String searchString) throws IOException;
}
