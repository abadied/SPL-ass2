/**
 * 
 */
package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

import java.math.BigInteger;

public class NextPrimeHammer implements Tool {


	@Override
	public String getType() {
		return new String("np-hammer");
	}

	@Override
	public long useOn(Product p) {
		long result = 0;
		long prime;
		for(Product part : p.getParts()){
			BigInteger b = BigInteger.valueOf(part.getFinalId());
			prime = b.nextProbablePrime().longValue();
			if (prime < 2)
				prime = 2;
			result += prime;
		}
        return result;
	}

}
