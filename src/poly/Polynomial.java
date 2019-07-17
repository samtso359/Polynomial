package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	private Node reverse(Node node) {
		Node prev = null;
		Node current = node;
		Node next = null;
			while (current != null) {
				next = current.next;
				current.next = prev;
				prev = current;
				current = next;
			}
		
			node = prev;
			return node;
	}
	 


	public Polynomial add(Polynomial p) {
		Polynomial result = new Polynomial();
		Polynomial given = new Polynomial();
		given.poly = this.poly;
		Node gnode = given.poly;   
		Node pnode = p.poly;
	
		while(gnode !=null || pnode != null){
			Node temp = null;
			if(gnode ==null){
				temp = new Node(pnode.term.coeff, pnode.term.degree, null);
				pnode = pnode.next;
			}
			else if(pnode==null){
				temp = new Node(gnode.term.coeff, gnode.term.degree, null);
				gnode = gnode.next;
			}
			else if(gnode.term.degree > pnode.term.degree){
				temp = new Node(pnode.term.coeff, pnode.term.degree, null);
				pnode = pnode.next;
			}
			else if(gnode.term.degree < pnode.term.degree){
				temp = new Node(gnode.term.coeff, gnode.term.degree, null);
				gnode = gnode.next;
			}
		
			else if(gnode.term.degree == pnode.term.degree){
				float coeff = gnode.term.coeff + pnode.term.coeff;
				if(coeff == 0){
					break;}
				else{
					temp = new Node(coeff, gnode.term.degree, null);
					gnode = gnode.next;
					pnode = pnode.next;
				}
	
			}
			//System.out.println("inputting "+temp.term.coeff+"x^"+temp.term.degree+" into result");
			temp.next=reverse(result.poly);
			result.poly = reverse(temp);
			//System.out.println(temp.term.coeff+"x^"+temp.term.degree+" has been inputted into result");
			//System.out.println("relooping");
		}
		//System.out.println("finished inputting");
		return result;
	}

	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		Polynomial result = new Polynomial();
		Polynomial given = new Polynomial();
		given.poly = this.poly;
		for (Node gnode = given.poly; gnode!=null; gnode = gnode.next){
			Polynomial temp = new Polynomial();
			for(Node pnode = p.poly; pnode!=null; pnode= pnode.next){
				temp.poly = new Node(gnode.term.coeff*pnode.term.coeff, gnode.term.degree+pnode.term.degree, null);
				result = result.add(temp);
			}
			
		}
		
		return result;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	private float power(float coeff, int degree){
	    if(degree == 0) return 1;
	    return coeff * power(coeff, --degree);
	}
	
	public float evaluate(float x) {
		float answer=0;
		Polynomial given = new Polynomial();
		given.poly = this.poly;
		Node gnode = given.poly;
		while(gnode!=null){
			answer += (power(x, gnode.term.degree))* gnode.term.coeff;
			gnode = gnode.next;
		}
		return answer;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
