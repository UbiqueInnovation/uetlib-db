package ch.ubique.uetlib.db.data.sort;

import static ch.ubique.uetlib.db.data.sort.PageableUtil.toPageableWithDefaultDirection;
import static ch.ubique.uetlib.db.data.sort.PageableUtil.toSortWithDefaultDirectionAsc;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SuppressWarnings("DataFlowIssue")
class PageableUtilTest {

    @Test
    void testToPageableWithDefaultDirection() {
        assertEquals(
                PageRequest.of(1, 2, Sort.by(Sort.Direction.DESC, "id")),
                toPageableWithDefaultDirection(1, 2, List.of("id"), List.of(Sort.Direction.DESC)));
        assertEquals(
                PageRequest.of(
                        1,
                        2,
                        Sort.by(
                                new Sort.Order(Sort.Direction.DESC, "id"),
                                new Sort.Order(Sort.Direction.ASC, "startedAt"))),
                toPageableWithDefaultDirection(
                        1, 2, List.of("id", "startedAt"), List.of(Sort.Direction.DESC)));
        assertEquals(
                PageRequest.of(
                        1,
                        2,
                        Sort.by(
                                new Sort.Order(Sort.Direction.DESC, "id"),
                                new Sort.Order(Sort.Direction.ASC, "startedAt"))),
                toPageableWithDefaultDirection(
                        1,
                        2,
                        List.of("id", "startedAt"),
                        List.of(Sort.Direction.DESC, Sort.Direction.ASC, Sort.Direction.DESC)));

        assertThrows(
                NullPointerException.class,
                () ->
                        toPageableWithDefaultDirection(
                                1,
                                2,
                                null,
                                List.of(
                                        Sort.Direction.DESC,
                                        Sort.Direction.ASC,
                                        Sort.Direction.DESC)));
        assertThrows(
                NullPointerException.class,
                () -> toPageableWithDefaultDirection(1, 2, List.of("id", "startedAt"), null));
    }

    @Test
    void testToSortWithDefaultDirectionAsc() {
        assertEquals(
                Sort.by(Sort.Direction.DESC, "id"),
                toSortWithDefaultDirectionAsc(List.of("id"), List.of(Sort.Direction.DESC)));
        assertEquals(
                Sort.by(
                        new Sort.Order(Sort.Direction.DESC, "id"),
                        new Sort.Order(Sort.Direction.ASC, "startedAt")),
                toSortWithDefaultDirectionAsc(
                        List.of("id", "startedAt"), List.of(Sort.Direction.DESC)));
        assertEquals(
                Sort.by(
                        new Sort.Order(Sort.Direction.DESC, "id"),
                        new Sort.Order(Sort.Direction.ASC, "startedAt")),
                toSortWithDefaultDirectionAsc(
                        List.of("id", "startedAt"),
                        List.of(Sort.Direction.DESC, Sort.Direction.ASC, Sort.Direction.DESC)));

        assertThrows(
                NullPointerException.class,
                () ->
                        toSortWithDefaultDirectionAsc(
                                null,
                                List.of(
                                        Sort.Direction.DESC,
                                        Sort.Direction.ASC,
                                        Sort.Direction.DESC)));
        assertThrows(
                NullPointerException.class,
                () -> toSortWithDefaultDirectionAsc(List.of("id", "startedAt"), null));
    }
}
