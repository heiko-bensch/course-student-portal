package de.bensch.course.service.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.StudentCourseSelection;
import de.bensch.course.service.CourseStudentExcelExportService;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Slf4j
class CourseStudentExportTest {
    private final StudentTestDataFactory testDataFactory = new StudentTestDataFactory();

    private static final Faker dataFacker = new Faker();

    @BeforeEach
    void setup() {
        File file = Path.of("torte.xlsx").toFile();
        boolean delete = file.delete();
        log.info("File {} delete {}", file, delete);
    }

    @Test
    void torte() throws IOException {
        CourseStudentExcelExportService export = new CourseStudentExcelExportService();
        List<StudentCourseSelection> all = new ArrayList<>();
        all.addAll(getCourseSelection(WeekDay.Monday));
        all.addAll(getCourseSelection(WeekDay.Wednesday));
        all.addAll(getCourseSelection(WeekDay.Thursday));
        all.addAll(getCourseSelection(WeekDay.Tuesday));


        byte[] export1 = export.export(all);
        Files.write(Path.of("torte.xlsx"), export1, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

    }

    List<StudentCourseSelection> getCourseSelection(WeekDay weekDay) {
        List<StudentCourseSelection> all = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            all.addAll(testDataFactory.createCourseSelections(weekDay, dataFacker.number().numberBetween(11, 33)));
            all.addAll(testDataFactory.createCourseSelections(weekDay, dataFacker.number().numberBetween(1, 25)));
            all.addAll(testDataFactory.createCourseSelections(weekDay, dataFacker.number().numberBetween(1, 25)));
            all.addAll(testDataFactory.createCourseSelections(weekDay, dataFacker.number().numberBetween(1, 25)));

        }
        return all;

    }


}
