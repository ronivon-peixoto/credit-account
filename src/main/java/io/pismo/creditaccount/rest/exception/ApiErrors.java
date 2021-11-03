package io.pismo.creditaccount.rest.exception;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ApiErrors implements Serializable {

	private static final long serialVersionUID = 6544526482152979780L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime timestamp;

	private Integer status;

	private List<String> errors;

	public ApiErrors(HttpStatus httpStatus, List<String> errors) {
		this.timestamp = LocalDateTime.now();
		this.status = httpStatus.value();
		this.errors = errors;
	}

	public ApiErrors(HttpStatus httpStatus, String message) {
		this.timestamp = LocalDateTime.now();
		this.status = httpStatus.value();
		this.errors = Arrays.asList(message);
	}

}
