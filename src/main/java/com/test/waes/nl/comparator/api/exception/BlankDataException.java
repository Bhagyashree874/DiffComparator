package com.test.waes.nl.comparator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when attempts to add content from one of the sides of the diff an
 * invalid content. The content is invalid when one of the following cases is
 * true: Is null or empty
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The content is empty or data is missing")

public class BlankDataException extends Exception {

	/**
	 *
	 */

}
