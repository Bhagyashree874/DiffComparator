package com.test.waes.nl.comparator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The identifier is empty or missing")
public class InvalidIdentifierException extends Exception {

}
