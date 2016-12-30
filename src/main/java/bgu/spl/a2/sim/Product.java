package bgu.spl.a2.sim;

import java.util.LinkedList;
import java.util.List;

/**
 * A class that represents a product produced during the simulation.
 */
public class Product {
	private long startId;
	private String name;
	private long finalId;
	private LinkedList<Product> parts;
	/**
	 * Constructor
	 * 
	 * @param startId
	 *            - Product start id
	 * @param name
	 *            - Product name
	 */
	public Product(long startId, String name) {
		this.startId = startId;
		this.name = name;
		parts = new LinkedList<Product>();
		finalId = startId;
	}

	/**
	 * @return The product name as a string
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return The product start ID as a long. start ID should never be changed.
	 */
	public long getStartId() {
		return startId;
	}

	/**
	 * @return The product final ID as a long. final ID is the ID the product
	 *         received as the sum of all UseOn();
	 */
	public long getFinalId() {
		//TODO:check
		return finalId;
	}

	/**
	 * @return Returns all parts of this product as a List of Products
	 */
	public List<Product> getParts() {
		// TODO:check!
		return parts;
	}

	/**
	 * Add a new part to the product
	 * 
	 * @param p
	 *            - part to be added as a Product object
	 */
	public void addPart(Product p) {
		// TODO:check!!
		parts.add(p);
	}
	
	/**
	 * setter for finalId
	 * @param id
	 */
	public void setFinalId(long id){
		finalId += id;
	}

}
