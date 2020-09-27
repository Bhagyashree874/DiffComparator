package com.test.waes.nl.comparator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The side is empty or invalid")

public class DiffSideIsNotDefinedException extends Exception {

}
