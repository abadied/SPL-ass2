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
	public void testResolve() {
		def.resolve(5);
		assertEquals("Expected value of 5", 5, def.get().intValue());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testResolveWhenCalledAfterResolve() throws IllegalStateException {
		def.resolve(5);
		def.resolve(6);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetBeforeResolve() throws IllegalStateException {
		def.get();
	}

	@Test
	public void testIsResolvedBeforeResolve() {
		assertTrue(!def.isResolved());
	}
	
	@Test
	public void testIsResolvedAfterResolve() {
		def.resolve(5);
		assertTrue(def.isResolved());
	}

	@Test
	public void testWhenResolvedBeforeResolve() {
		def.whenResolved(() -> {
			res = true;
		});
		
		def.resolve(5);
		assertTrue(res);
	}
	
	@Test
	public void testWhenResolvedAfterResolve() {
		def.resolve(5);
		
		res = false;
		def.whenResolved(() -> {
			res = true;
		});
		
		assertTrue(res);
	}

}
