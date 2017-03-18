package polylines.io;


import polylines.Point;
import polylines.PolyLine;
import polylines.PolyLineExt;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PolyLineCSVReader implements IPolyLineReader {

    private Scanner scanner;

    public PolyLineCSVReader(InputStream stream) {
        scanner = new Scanner(stream);
        scanner.useDelimiter(",");
    }

    public PolyLine readLine() {
        char extFlag = readChar();
        if (extFlag != 'b' && extFlag != 'e') {
            throw new InputMismatchException(
                    "Extension flag must be either 'b' of 'e'"
            );
        }
        PolyLine line;
        if (extFlag == 'b') {
            line = new PolyLine();
        } else {
            line = new PolyLineExt();
        }
        while(!scanner.hasNext("\n")) {
            line.appendPoint(readPoint());
        }
        return line;
    }

    private char readChar() {
        String oneChar = scanner.next();
        if (oneChar == null) {
            throw new InputMismatchException("Expected non-space character");
        }
        if (oneChar.length() > 1) {
            throw new InputMismatchException("Expected single character");
        }
        return oneChar.charAt(0);
    }

    private Point readPoint() {
        String token = scanner.next();
        String maybeNumbers[] = token.split("\\s+");
        if (maybeNumbers.length != 2) {
            throw new InputMismatchException("Expected two numbers separated by a space");
        }
        try {
            return new Point(
                    Integer.parseInt(maybeNumbers[0]),
                    Integer.parseInt(maybeNumbers[1])
            );
        } catch (NumberFormatException nfe) {
            throw new InputMismatchException("Point coordinates are not integers");
        }
    }

}
