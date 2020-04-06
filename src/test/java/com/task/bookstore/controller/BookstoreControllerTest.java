package com.task.bookstore.controller;

import com.task.bookstore.model.Login;
import com.task.bookstore.service.BookstoreService;
import com.task.bookstore.service.DataCollectorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BookstoreController.class)
public class BookstoreControllerTest {

    @MockBean
    private BookstoreService bookstoreService;

    @MockBean
    private DataCollectorService dataCollectorService;

    @MockBean
    private CommandLineRunner commandLineRunner;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void redirectToLogin() throws Exception {
        String uri = "/";
        mockMvc.perform(get(uri)).andExpect(redirectedUrl("/task/login"));
    }

    @Test
    public void showLoginPage() throws Exception {
        String uri = "/task/login";
        mockMvc.perform(get(uri)).andExpect(model().attribute("command", new Login()));
    }

    @Test
    public void doLogin_successfully() throws Exception {
        String uri = "/task/login";

        MvcResult mvcResult = mockMvc.perform(post(uri)
                .param("username", "user@check24.de")
                .param("password", "dummy")
                .flashAttr("login", new Login()))
                .andExpect(redirectedUrl("/task/books"))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(302, status);

    }

    @Test
    public void doLogin_invalidEmail() throws Exception {
        String uri = "/task/login";
        final MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post(uri)
                .param("username", "user@check24")
                .param("password", "dummy")
                .flashAttr("login", new Login())
                .session(mockHttpSession))
                .andExpect(model().attribute("command", new Login()));
        Object loginStatus = mockHttpSession.getAttribute("loginStatus");
        Object loginMessage = mockHttpSession.getAttribute("loginMessage");
        assertEquals("FAILED", loginStatus.toString());
        assertEquals("Invalid Email address for username", loginMessage.toString());
    }

    @Test
    public void doLogin_emailFromTestComDomain() throws Exception {
        String uri = "/task/login";
        final MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post(uri)
                .param("username", "user@test.com")
                .param("password", "dummy")
                .flashAttr("login", new Login())
                .session(mockHttpSession))
                .andExpect(model().attribute("command", new Login()));
        Object loginStatus = mockHttpSession.getAttribute("loginStatus");
        Object loginMessage = mockHttpSession.getAttribute("loginMessage");
        assertEquals("FAILED", loginStatus.toString());
        assertEquals("Username should not belong to test.com domain", loginMessage.toString());
    }

    @Test
    public void doLogin_emptyUsername() throws Exception {
        String uri = "/task/login";
        final MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post(uri)
                .param("username", "")
                .param("password", "dummy")
                .flashAttr("login", new Login())
                .session(mockHttpSession))
                .andExpect(model().attribute("command", new Login()));
        Object loginStatus = mockHttpSession.getAttribute("loginStatus");
        Object loginMessage = mockHttpSession.getAttribute("loginMessage");
        System.out.println(loginMessage.toString());
        assertEquals("FAILED", loginStatus.toString());
        assertEquals("Username should not be blank", loginMessage.toString());
    }

    @Test
    public void doLogin_emptyPassword() throws Exception {
        String uri = "/task/login";
        final MockHttpSession mockHttpSession = new MockHttpSession();

        mockMvc.perform(post(uri)
                .param("username", "test@check24.de")
                .param("password", "")
                .flashAttr("login", new Login())
                .session(mockHttpSession))
                .andExpect(model().attribute("command", new Login()));
        Object loginStatus = mockHttpSession.getAttribute("loginStatus");
        Object loginMessage = mockHttpSession.getAttribute("loginMessage");
        System.out.println(loginMessage.toString());
        assertEquals("FAILED", loginStatus.toString());
        assertEquals("Password should not be blank", loginMessage.toString());
    }
}
