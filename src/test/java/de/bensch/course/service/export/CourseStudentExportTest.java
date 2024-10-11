package de.bensch.course.service.export;

import de.bensch.course.model.WeekDay;
import de.bensch.course.model.entity.StudentCourseSelection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

class CourseStudentExportTest {
    private final StudentTestDataFactory testDataFactory = new StudentTestDataFactory();

    @BeforeEach
    void setup() {
        File file = Path.of("torte.xlsx").toFile();
        file.delete();
    }

    @Test
    void torte() throws IOException {
        CourseStudentExport export = new CourseStudentExport();
        Collection<StudentCourseSelection> selection = testDataFactory.createCourseSelections(WeekDay.Monday, "3", 12);
        byte[] export1 = export.export(selection);
        Files.write(Path.of("torte.xlsx"), export1, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

    }


}
