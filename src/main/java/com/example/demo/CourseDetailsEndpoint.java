package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pojoClasses.*;
import java.util.List;

@Endpoint
public class CourseDetailsEndpoint
{

    private static final String NAMESPACE = "http://soap-example/courses";

    @Autowired
    private CourseDetailsService service;


    @PayloadRoot(namespace = NAMESPACE,localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request)
    {
        Course course = service.findById(request.getId());
        if (course==null)
        {
            throw new CourseNotFoundException("Invalid course id " + request.getId());
        }
        return mapCourseDetails(course);
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest request)
    {
        List<Course> courses = service.findAll();
        return mapAllCourseDetails(courses);
    }

    @PayloadRoot(namespace = NAMESPACE,localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request)
    {
        CourseDetailsService.Status status = service.deleteById(request.getId());
        DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
        response.setStatus(mapStatus(status));
        return response;
    }

    private pojoClasses.Status mapStatus(CourseDetailsService.Status status)
    {
        if (status== CourseDetailsService.Status.FAILURE)
        {
            return pojoClasses.Status.FAILURE;
        }

        return pojoClasses.Status.SUCCESS;
    }


    private GetCourseDetailsResponse mapCourseDetails(Course course) {
        GetCourseDetailsResponse response = new GetCourseDetailsResponse();
        //CourseDetails courseDetails = mapCourse(course);
        response.setCourseDetails(mapCourse(course));
        return response;
    }

    private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
        GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
        for (Course course : courses)
        {
            CourseDetails mapCourse = mapCourse(course);
            response.getCourseDetails().add(mapCourse);
        }
        return response;
    }

    private CourseDetails mapCourse(Course course) {
        CourseDetails courseDetails = new CourseDetails();
        courseDetails.setId(course.getId());
        courseDetails.setName(course.getName());
        courseDetails.setDescription(course.getDescription());
        return courseDetails;
    }



}
