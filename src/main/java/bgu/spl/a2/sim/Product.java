package bgu.spl.a2.sim;

import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product {
	/**
	 * Constructor
	 * 
	 * @param startId
	 *            - Product start id
	 * @param name
	 *            - Product name
	 */
	public Product(long startId, String name) {
		// TODO
	}

	/**
	 * @return The product name as a string
	 */
	public String getName() {
		// TODO
		return null;// change
	}

	/**
	 * @return The product start ID as a long. start ID should never be changed.
	 */
	public long getStartId() {
		// TODO
		return 0;// change
	}

	/**
	 * @return The product final ID as a long. final ID is the ID the product
	 *         received as the sum of all UseOn();
	 */
	public long getFinalId() {
		// TODO
		return 0;// change
	}

	/**
	 * @return Returns all parts of this product as a List of Products
	 */
	public List<Product> getParts() {
		// TODO
		return null;// change
	}

	/**
	 * Add a new part to the product
	 * 
	 * @param p
	 *            - part to be added as a Product object
	 */
	public void addPart(Product p) {
		// TODO
	}

}
