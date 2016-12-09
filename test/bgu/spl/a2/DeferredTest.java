package bgu.spl.a2;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeferredTest {

	Deferred<Integer> def;
	boolean res;
	
	@Before
	public void setUp() throws Exception {
		def = new Deferred<>();
		res = false;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet() {
		// before resolve
		boolean gotException = false;
		try {
			def.get();
		}
		catch (IllegalStateException e){
			gotException = true;
		}
		assertTrue(gotException);
		
		// after resolve
		def.resolve(5);
		assertEquals(5, def.get().intValue());
	}

	@Test
	public void testIsResolved() {
		assertTrue(!def.isResolved());
		def.resolve(5);
		assertTrue(def.isResolved());
	}

	@Test
	public void testResolve() {
		// resolve once
		def.resolve(3);
		assertEquals(3, def.get().intValue());
		
		// resolve twice
		boolean secondResolveFail = false;
		try {
			def.resolve(5);
		}
		catch (IllegalStateException e){
			secondResolveFail = true;
		}
		
		assertTrue(secondResolveFail);
		
		assertEquals(3, def.get().intValue());
	}

	@Test
	public void testWhenResolved() {
		// test for whenResolved before resolve
		def.whenResolved(() -> {
			res = true;
		});
		
		def.resolve(6);
		assertTrue(res);
		
		// test for whenResolved after resolve
		res = false;
		def.whenResolved(() -> {
			res = true;
		});
		assertTrue(res);
	}

}
