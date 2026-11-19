package ch.uetlib.db.data.sort;

import com.google.common.collect.Streams;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class PageableUtil {

    private PageableUtil() {}

    /**
     * @param page The page to retrieve (zero-based)
     * @param perPage Entries per page
     * @param orderBy property names to order by
     * @param orderByDirections Sort direction of the corresponding property in {@code orderBy},
     *     with default value {@link Sort.Direction#ASC} if this list is shorter than {@code
     *     orderBy}
     */
    public static Pageable toPageableWithDefaultDirection(
            final int page,
            final int perPage,
            final List<String> orderBy,
            final List<Sort.Direction> orderByDirections) {
        final var sort = toSortWithDefaultDirectionAsc(orderBy, orderByDirections);
        return PageRequest.of(page, perPage, sort);
    }

    public static Sort toSortWithDefaultDirectionAsc(
            final List<String> orderBy, final List<Sort.Direction> orderByDirections) {
        final var directionsSize = orderByDirections.size();
        final var orders =
                Streams.mapWithIndex(
                                orderBy.stream(),
                                (property, index) ->
                                        new Sort.Order(
                                                index < directionsSize
                                                        ? orderByDirections.get((int) index)
                                                        : Sort.Direction.ASC,
                                                property))
                        .toList();
        return Sort.by(orders);
    }
}
