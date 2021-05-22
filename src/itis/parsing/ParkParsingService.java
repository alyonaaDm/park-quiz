package itis.parsing;

import java.io.IOException;

interface ParkParsingService {

    Park parseParkData(String parkDatafilePath) throws ParkParsingException, IOException, IllegalAccessException;


}
