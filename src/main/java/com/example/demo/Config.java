package com.example.demo;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import javax.security.auth.callback.CallbackHandler;
import java.util.Collections;
import java.util.List;

@EnableWs
@Configuration
public class Config extends WsConfigurerAdapter
{
    @Bean
    public ServletRegistrationBean messageDispatcherServlet (ApplicationContext context)
    {
        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(context);
        messageDispatcherServlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(messageDispatcherServlet,"/ws/*");
    }

    @Bean(name = "CourseDetails")
    public DefaultWsdl11Definition defaultWsdl11Definition (XsdSchema courseSchema)
    {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("CourseDetailsPort");
        defaultWsdl11Definition.setLocationUri("/ws/CourseDetails");
        defaultWsdl11Definition.setTargetNamespace("http://soap-example/courses");
        defaultWsdl11Definition.setSchema(courseSchema);
        return defaultWsdl11Definition;
    }

    @Bean
    public XsdSchema courseSchema ()
    {
        return new SimpleXsdSchema(new ClassPathResource("coursedetails.xsd"));
    }

    @Bean
    public XwsSecurityInterceptor securityInterceptor ()
    {
        XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
        securityInterceptor.setCallbackHandler(callbackHandler());
        securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));

        return securityInterceptor;
    }

    @Bean
    public SimplePasswordValidationCallbackHandler callbackHandler ()
    {
        SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
        handler.setUsersMap(Collections.singletonMap("user","password"));
        return handler;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor());
    }
}


