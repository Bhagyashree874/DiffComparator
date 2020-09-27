package com.test.waes.nl.comparator.api.domain;

/**
 * @author Bhagyashree Enum that defines the possible content sides of the diff
 */
public enum Side {

	/**
	 * Represents the left side of the diff.
	 */
	LEFT,

	/**
	 * Represents the right side of the diff.
	 */
	RIGHT;

	@Override
	public String toString() {
		switch (this) {
		case LEFT:
			return "LEFT";
		case RIGHT:
			return "RIGHT";
		}
		throw new Error("An error occurred while trying to get the correct side.");
	}
}
