package com.nero.dronetask.helpers;

import com.nero.dronetask.dtos.responses.Wrapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public class ResponseBuilder {
	public enum Type {CREATED, OK}

	public static <T> ResponseEntity<Wrapper<T>> success(String message) {
		return ResponseEntity.ok(new Wrapper<>(message));
	}

	public static <T> ResponseEntity<Wrapper<T>> success(T data) {
		return ResponseEntity.ok(new Wrapper<>(data));
	}

	public static <T> ResponseEntity<Wrapper<T>> success(String message, Type successType) {
		return ResponseEntity.status(Type.CREATED.equals(successType) ? HttpStatus.OK : HttpStatus.CREATED).body(new Wrapper<>(message));
	}

	public static <T> ResponseEntity<Wrapper<T>> success(T data, Type successType) {
		return ResponseEntity.status(Type.CREATED.equals(successType) ? HttpStatus.CREATED : HttpStatus.OK).body(new Wrapper<>(data));
	}

	public static <T> ResponseEntity<Wrapper<T>> success(String message, T data) {
		return ResponseEntity.ok(new Wrapper<>(message, data));
	}

	public static <T> ResponseEntity<Wrapper<T>> success(String message, T data, Type successType) {
		return ResponseEntity.status(Type.CREATED.equals(successType) ? HttpStatus.CREATED : HttpStatus.OK).body(new Wrapper<>(message, data));
	}

	public static <D, T extends Page<D>> ResponseEntity<Wrapper<Collection<D>>> success(T data) {
		int totalPages = data.getTotalPages();
		int current = data.getNumber() + 1;
		long total = data.getTotalElements();
		int perPage = data.getSize();

		long from = ((long) (current - 1) * perPage) + 1 ;
		long to = Math.min(total, ((long) current * perPage));

		if (from > total) from = to = 0;

		Wrapper.Meta meta = new Wrapper.Meta(current, from, to, perPage, total, totalPages);
		return ResponseEntity.ok(new Wrapper<>(data.getContent(), meta));
	}
}
