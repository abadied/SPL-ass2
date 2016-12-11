package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest {

	VersionMonitor vm;
	
	@Before
	public void setUp() throws Exception {
		vm = new VersionMonitor();
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testGetVersion() {
		assertEquals(0, vm.getVersion());
	}

	@Test
	public void testInc() {
		vm.inc();
		assertEquals(1, vm.getVersion());
	}

	@Test
	public void testAwait() throws InterruptedException {

		Thread t1 = new Thread(() -> {
				try {
					vm.await(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
		t1.start();
		vm.inc();
		t1.join();
	}

}
