package kr.ac.ks.app.controller;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import kr.ac.ks.app.domain.Student;
import kr.ac.ks.app.repository.CourseRepository;
import kr.ac.ks.app.repository.LessonRepository;
import kr.ac.ks.app.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class CourseController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    public CourseController(StudentRepository studentRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/course")
    public String showCourseForm(Model model) {
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        return "courses/courseForm";
    }

    @PostMapping("/course")
    public String createCourse(@RequestParam("studentId") Long studentId,
                               @RequestParam("lessonId") Long lessonId
                               ) {
        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Course course = Course.createCourse(student,lesson);
        Course savedCourse = courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String courseList(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/courseList";
    }

    @GetMapping("/course/edit/{id}")
    public String showEditCourseForm(Model model,@PathVariable Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found:" + id));

        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        model.addAttribute("course", course);
        return "courses/courseEdit";
    }

    @PostMapping("/course/edit/{id}")
    public String editCourse(@RequestParam("studentId") Long studentId,
                             @RequestParam("lessonId") Long lessonId,
                             @PathVariable Long id
        ) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found:" + id));

        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();

        course.setStudent(student);
        course.setLesson(lesson);

        courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id){
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found:" + id));

        courseRepository.delete(course);

        return "redirect:/courses";
    }
}
