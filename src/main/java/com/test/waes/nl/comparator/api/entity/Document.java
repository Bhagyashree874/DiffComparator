package com.test.waes.nl.comparator.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Document {

	/**
	 * Using the primitive type, since it does not accept null values.
	 */
	@Id
	private long id;
	/**
	 * Right side of the comparison
	 */
	@Lob
	@Column(length = 32000)
	private String right1;
	/**
	 * Left side of the comparison
	 */
	@Lob
	@Column(length = 32000)
	private String left1;

	/**
	 * Empty constructor for creating instances
	 */
	public Document() {

	}

	/**
	 * This constructor creates new object instances for validation and persistence.
	 *
	 * @param id     of the object. It must to be informed. It is not
	 *               auto-generated.
	 * @param left1  side of data is being sent through the request.
	 * @param right1 side of data is being sent through the request.
	 */
	public Document(long id, String left, String right) {
		this.id = id;
		this.left1 = left;
		this.right1 = right;
	}

	/**
	 *
	 * Eclipse auto-generated equals() for comparing each attribute.
	 *
	 * @author Bhagyashree
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		if (id != other.id)
			return false;
		if (left1 == null) {
			if (other.left1 != null)
				return false;
		} else if (!left1.equals(other.left1))
			return false;
		if (right1 == null) {
			if (other.right1 != null)
				return false;
		} else if (!right1.equals(other.right1))
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public String getLeft() {
		return left1;
	}

	public String getRight() {
		return right1;
	}

	/**
	 *
	 * An Eclipse auto-generated hashCode() for comparing each attribute.
	 *
	 * @author Bhagyashree
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((left1 == null) ? 0 : left1.hashCode());
		result = prime * result + ((right1 == null) ? 0 : right1.hashCode());
		return result;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLeft(String left) {
		this.left1 = left;
	}

	public void setRight(String right) {
		this.right1 = right;
	}

	/**
	 *
	 * Formatting the toString() for presenting all class fields in a single String.
	 *
	 * @author Bhagyashree
	 * @return
	 */
	@Override
	public String toString() {
		return "Document [id=" + getId() + ", left1=" + getLeft() + ", right1=" + getRight() + "]";
	}
}