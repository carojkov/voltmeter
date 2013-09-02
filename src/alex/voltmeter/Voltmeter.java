package alex.voltmeter;

import java.io.*;
import java.util.*;

public class Voltmeter extends TimerTask{
    private RandomAccessFile _meter;
    private Timer _timer;
    private int _probeCounter = 0;
    private int _probesTotal;
    private int _pause; 

    public Voltmeter(String[] args) throws IOException {
	_meter = new RandomAccessFile(args[0], "rwd");

	_probesTotal = Integer.parseInt(args[1]);
	
	_pause = 1000 * Integer.parseInt(args[2]);

	_timer = new Timer();

	Runtime.getRuntime().addShutdownHook(new Thread() {
		public void run() {
		    try {
			close();
		    } catch (Exception e) {
			throw new RuntimeException(e);
		    }
		}
	    });
    }
    
    private void reset() throws IOException{
	send("R\r");
	String feedback = read();
	System.out.println(feedback);
    }

    private void switchOn() throws IOException {
	send("PO,B,0,1\r");
	String feedback = read();
	System.out.println(feedback);
    }

    private void switchOff() throws IOException {
	send("PO,B,0,0\r");
        String feedback = read();
	System.out.println(feedback);
    }
    private void configure() throws IOException {
	send("C,3,0,0,2\r");
	String feedback = read();
	System.out.println(feedback);
    }

    private void send(String command) throws IOException {
	_meter.write(command.getBytes());
    }

    private String read() throws IOException {
	byte []buffer = new byte[256];
	int i;
	
        i = _meter.read(buffer);
	
	return new String(buffer, 0, i);	
    }

    public void run() {
	try {
	    switchOn();
	    readVoltage();
	    switchOff();
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}

	if (++_probeCounter < _probesTotal) {
	} else {
	    _timer.cancel();
	}
    }

    private void schedule() {
	_timer.schedule(this, new java.util.Date(), _pause);
    }

    private void readVoltage() throws IOException {
	send("A\r");

	String feedback = read();
	System.out.println(feedback);
    }

    private void close() throws Exception {
	RandomAccessFile file = _meter;
	_meter = null;
	if (file != null)
	    file.close();
    }
    
    public static void main(String []args) throws Exception {
	Voltmeter t = new Voltmeter(args);
	t.reset();
	t.configure();
	t.switchOn();
	t.schedule();
	//	t.close();
    }
}
