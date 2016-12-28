/**
 * 
 */
package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;


public class GcdScrewDriver implements Tool {


	@Override
	public String getType() {
		//TODO:check if anything else needed in this function
		return new String("gs-driver");
	}


	@Override
	public long useOn(Product p) {
		//TODO: check!!!
		long reverse_id = Long.reverse(p.getStartId());
		BigInteger b1 = BigInteger.valueOf(p.getStartId());
	    BigInteger b2 = BigInteger.valueOf(reverse_id);
	    BigInteger gcd = b1.gcd(b2);
	    return gcd.intValue();
	}

}
