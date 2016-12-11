package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VersionMonitorTest {

	VersionMonitor vm;
	Thread t1;
	Thread[] tArr;
	
	@Before
	public void setUp() throws Exception {
		vm = new VersionMonitor();
		tArr = new Thread[10];
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testGetVersion() {
		assertEquals(0, vm.getVersion());
		Runnable rn = () -> {
			for (int i = 0; i < 10; i++){
				vm.inc();
			}
		};
		for (int i = 0; i < 10; i++)
			tArr[i] = new Thread(rn);
		try {
			for (Thread t : tArr)
				t.start();
			for (Thread t : tArr)
				t.join();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(100, vm.getVersion());
	}

	@Test
	public void testInc() {
		vm.inc();
		assertEquals(1, vm.getVersion());
		Runnable rn = () -> {
			for (int i = 0; i < 10; i++){
				vm.inc();
			}
		};
		for (int i = 0; i < 10; i++)
			tArr[i] = new Thread(rn);
		try {
			for (Thread t : tArr)
				t.start();
			for (Thread t : tArr)
				t.join();
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(101, vm.getVersion());
	}

	@Test
	public void testAwait() {
		Runnable rn = () -> {
				for (int i = 0; i < 10; i++){
					vm.inc();
				}
			};
		t1 = new Thread(() -> {
				try {
					vm.await(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
		for (int i = 0; i < 10; i++)
			tArr[i] = new Thread(rn);
		
		try {
			t1.start();
			
			for (Thread t : tArr)
				t.start();
			
			for (Thread t : tArr)
				t.join();
			t1.join();
			
			assertEquals(100, vm.getVersion());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
