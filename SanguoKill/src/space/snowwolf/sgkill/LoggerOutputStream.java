package space.snowwolf.sgkill;

import java.io.IOException;
import java.io.OutputStream;

public class LoggerOutputStream extends OutputStream {
	
	private OutputStream[] streams;
	
	public LoggerOutputStream(OutputStream ... streams) {
		this.streams = streams;
	}

	@Override
	public void write(int b) throws IOException {
		for(OutputStream out : streams) {
			if(out != null) {
				out.write(b);
			}
		}
	}
	
}
