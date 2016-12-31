/**
 * 
 */
package bgu.spl.a2.sim.tools;

import java.math.BigInteger;

import bgu.spl.a2.sim.Product;


public class GcdScrewDriver implements Tool {


	@Override
	public String getType() {
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

	/**
	 * reverses the given number (base 10)
	 * @param n a number to reverse
	 * @return reversed number
	 */
	private long reverse(long n){
	    long reversed=0;
	    while(n != 0){
	        reversed = reversed * 10;
	        reversed = reversed + (n % 10);
	        n = n / 10;
	    }
	    return reversed;
	  }
}
