package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.IOException;
import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PolyLineCSVReader implements IPolyLineReader {

    private Scanner scanner;

    public PolyLineCSVReader(InputStream stream) {
        scanner = new Scanner(stream);
        scanner.useDelimiter(",");
    }

    public PolyLine readLine() throws IOException {
        char extFlag = readChar();
        if (extFlag != 'b' && extFlag != 'e') {
            throw new IOException(
                    "Extension flag must be either 'b' of 'e'"
            );
        }
        PolyLine line;
        if (extFlag == 'b') {
            line = new PolyLine();
        } else {
            line = new PolyLineExt();
        }
        while(!scanner.hasNext("\n") && scanner.hasNext()) {
            line.appendPoint(readPoint());
        }
        return line;
    }

    private char readChar() throws IOException {
        String oneChar = readCSVCell();
        if (oneChar == null) {
            throw new IOException("Expected non-space character");
        }
        if (oneChar.length() != 1) {
            throw new IOException("Expected single character");
        }
        return oneChar.charAt(0);
    }

    private Point readPoint() throws IOException {
        String token = readCSVCell();
        String maybeNumbers[] = token.split("\\s+");
        if (maybeNumbers.length != 2) {
            throw new IOException("Expected two numbers separated by a space");
        }
        try {
            return new Point(
                    Integer.parseInt(maybeNumbers[0]),
                    Integer.parseInt(maybeNumbers[1])
            );
        } catch (NumberFormatException nfe) {
            throw new IOException("Point coordinates are not integers");
        }
    }

    private String readCSVCell() throws IOException {
        try {
            return scanner.next();
        } catch (NoSuchElementException exc) {
            throw new IOException("Unexpected end of input");
        }
    }

}
