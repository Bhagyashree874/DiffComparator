package com.test.waes.nl.comparator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
reason = "The content  or data is in invalid base 64 format")
public class InvalidBase64contentException extends Exception {

}
