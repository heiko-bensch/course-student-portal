package de.bensch.course.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SemesterServiceTest {

    SemesterService semesterService;


    @BeforeEach
    void setup() {
        semesterService = spy(new SemesterService());
    }

    @Test
    void findAllSemester() {
        doReturn(LocalDate.of(2023, 6, 1)).when(semesterService).getLocalDate();
        List<String> allSemester = semesterService.findAllSemester();
        assertThat(allSemester).containsExactlyInAnyOrderElementsOf(List.of("2/2024", "1/2024", "2/2023", "1/2023", "2/2022", "1/2022", "2/2021", "1/2021", "2/2020", "1/2020"));
    }

    @ParameterizedTest
    @MethodSource("provideCurrentSemester")
    void getCurrentSemester(LocalDate now, String expecedResult) {
        doReturn(now).when(semesterService).getLocalDate();
        String currentSemester = semesterService.getCurrentSemester();
        assertThat(currentSemester).isEqualTo(expecedResult);
    }

    public static Stream<Arguments> provideCurrentSemester() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 1, 1), "2/2023"),
                Arguments.of(LocalDate.of(2024, 9, 1), "1/2024"),
                Arguments.of(LocalDate.of(2024, 8, 1), "2/2023"),
                Arguments.of(LocalDate.of(2024, 12, 1), "1/2024"),
                Arguments.of(LocalDate.of(2025, 1, 1), "2/2024")

        );
    }

}
