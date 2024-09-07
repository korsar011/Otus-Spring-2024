package ru.otus.hw.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorController.class)
@Import(SecurityConfiguration.class)
@TestPropertySource(properties = {
        "spring.security.user.details-service.enabled=false"
})
public class AuthorControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserDetailsService userDetailsService;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "/library/authors, 200",
            "/library/authors/new, 200"
    })
    public void testAuthenticatedUserAccess(String url, int expectedStatus) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is(expectedStatus));
    }

    @ParameterizedTest
    @CsvSource({
            "/library/authors, 302",
            "/library/authors/new, 302",
            "/library/authors/edit/1, 302"
    })
    public void testUnauthenticatedUserAccess(String url, int expectedStatus) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @ParameterizedTest
    @CsvSource({
            "/library/authors, New Author",
            "/library/authors/delete/1, null"
    })
    public void testAuthenticatedUserActions(String url, String param) throws Exception {
        if (url.contains("/delete")) {
            mockMvc.perform(post(url))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/authors"));
        } else {
            mockMvc.perform(post(url)
                            .param("name", param))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(result -> result.getResponse().getRedirectedUrl().equals("/library/authors"));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "/library/authors, New Author",
            "/library/authors/delete/1, null"
    })
    public void testUnauthenticatedUserActions(String url, String param) throws Exception {
        mockMvc.perform(post(url)
                        .param("name", param))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().startsWith("/login"));
    }
}