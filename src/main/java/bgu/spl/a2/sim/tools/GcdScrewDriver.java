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
		long result = 0;
	    for(Product part : p.getParts()){
			BigInteger b1 = BigInteger.valueOf(part.getFinalId());
		    BigInteger b2 = BigInteger.valueOf(reverse(part.getFinalId()));
		    BigInteger gcd = b1.gcd(b2);
			result += gcd.longValue();
		}
	    return result;
	}

	public long reverse(long n){
	    long reverse=0;
	    while( n != 0 ){
	        reverse = reverse * 10;
	        reverse = reverse + n%10;
	        n = n/10;
	    }
	    return reverse;
	  }
}
