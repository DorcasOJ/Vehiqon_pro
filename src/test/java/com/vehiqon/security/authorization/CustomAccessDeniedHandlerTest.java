package com.vehiqon.security.authorization;


import com.vehiqon.common.api.dto.RequestContext;
import com.vehiqon.security.config.CorsConfig;
import com.vehiqon.security.config.SecurityConfig;
import com.vehiqon.security.controller.TestAdminController;
import com.vehiqon.security.jwt.JwtAuthenticationEntryPoint;
import com.vehiqon.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("Temporarily disabled while fixing security test configuration")
@WebMvcTest(TestAdminController.class)
@Import({
        SecurityConfig.class,
        CorsConfig.class,
        CustomAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class
})
class CustomAccessDeniedHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private RequestContext requestContext;


    @Test
    @DisplayName("Should return 403 Forbidden with custom ApiError when user lacks ADMIN role")
    @WithMockUser(username = "johndoe@email.com", roles = {"USER"}) // Authenticated as standard USER
    void whenStandardUserAccessesAdminEndpoint_thenReturn403ApiError() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                .header("X-Request-Id", "test-request-id-123")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(403))
                .andExpect(jsonPath("$.message").value("Forbidden"))
                .andExpect(jsonPath("$.path").value("/api/admin/dashboard"))
                .andExpect(jsonPath("$.timestamp").value(notNullValue()))
                .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
                .andExpect(jsonPath("$.error.message").value("You do not have permission to access this resource"));

    }
}