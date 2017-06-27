package org.example.seed.mapper.jpa;

import org.springframework.scheduling.annotation.AsyncResult;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by PINA on 26/06/2017.
 */
public interface GenericMapper<F, T extends Serializable> {

    T map(final F from);

    F map(final T from);

    default Future<F> map(final Future<T> from) {
        return Optional.of(from).map(f -> {
            try {
                return new AsyncResult<>(this.map(f.get()));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }

    default List<T> mapList(final List<F> from) {
        return Optional.of(from).orElse(null).stream().map(this::map).collect(Collectors.toList());
    }

    default Future<List<T>> mapList(final Future<List<F>> from) {
        return Optional.of(from).map(f -> {
            try {
                return new AsyncResult<>(this.mapList(f.get()));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }

    default List<F> mapListReverse(final List<T> from) {
        return Optional.of(from).orElse(null).stream().map(this::map).collect(Collectors.toList());
    }

    default Future<List<F>> mapListReverse(final Future<List<T>> from) {
        return Optional.of(from).map(f -> {
            try {
                return new AsyncResult<>(this.mapListReverse(f.get()));
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }
}
