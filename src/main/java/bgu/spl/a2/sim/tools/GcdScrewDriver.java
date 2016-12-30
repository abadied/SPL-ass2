/**
 * 
 */
package bgu.spl.a2.sim.tools;

import java.awt.List;
import java.math.BigInteger;
import java.util.LinkedList;

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
		int num_of_parts = p.getParts().size();
		long result = 0;
		long reverse_id = Long.reverse(p.getStartId() + 1);
		BigInteger b1 = BigInteger.valueOf(p.getStartId() + 1);
	    BigInteger b2 = BigInteger.valueOf(reverse_id);
	    BigInteger gcd = b1.gcd(b2);
	    for(int i = 0 ; i<num_of_parts; i++){
			result += gcd.intValue();
		}
	    
	    return result;
	}

}
