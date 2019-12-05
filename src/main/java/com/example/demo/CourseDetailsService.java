package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class CourseDetailsService
{
    public enum Status
    {
        SUCCESS,FAILURE;
    }

    private static List<Course> courses = new ArrayList<>();

    static
    {
        Course course1 = new Course(1,"Spring","Dependency Injection");
        Course course2 = new Course(2,"Java","Platform Independent");
        Course course3 = new Course(3,"Kafka","Messaging Service");
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);

    }

    // get 1 course
    // findById --> int id, Course

    public Course findById (int id)
    {
        for (Course course : courses)
        {
            if (course.getId()==id)
            {
                return course;
            }
        }
        return null;
    }

    // get All courses
    // findAll --> List<Courses>

    public List<Course> findAll ()
    {
        return courses;
    }

    // delete a course
    // deleteById --> int id, int

    public Status deleteById (int id)
    {
        Iterator iterator = courses.iterator();
        while (iterator.hasNext())
        {
            Course course = (Course) iterator.next();
            if (course.getId()==id)
            {
                iterator.remove();
                return Status.SUCCESS;
            }
        }
        return Status.FAILURE;
    }
}
