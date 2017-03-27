package task3;


import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class BroadcastingOutputStream extends OutputStream {

    private List<OutputStream> streams;

    public BroadcastingOutputStream(OutputStream... streams) {
        this.streams = Arrays.asList(streams);
    }

    public BroadcastingOutputStream(Collection<OutputStream> streams) {
        this.streams = new ArrayList<>(streams);
    }

    @Override
    public void write(int b) throws IOException {
        for(OutputStream stream : this.streams) {
            stream.write(b);
        }
    }
}
