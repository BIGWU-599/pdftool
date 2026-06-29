package test.coverter.model;

import java.io.InputStream;
import java.io.OutputStream;

class Synchronizer implements Runnable {
	
	private final OutputStream output;
	private final InputStream input;
	
	public Synchronizer(InputStream input, OutputStream output) {
	      this.input = input;
	      this.output = output;
	  }
	
	public void run() {
		try {
			final byte[] buffer = new byte[1024];
	        for (int length = 0; (length = input.read(buffer)) != -1; )
	        {
	            output.write(buffer, 0, length);
	        }
	    } catch (Exception e) {
	          e.printStackTrace();
	    }
	}
	
}
