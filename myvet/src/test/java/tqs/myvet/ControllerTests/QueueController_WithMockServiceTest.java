package tqs.myvet.ControllerTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import tqs.myvet.controllers.QueueRestController;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.CustomUserDetailsService;

@WebMvcTest(QueueRestController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class QueueController_WithMockServiceTest {
    
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;
    
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void a_WhenPostToReceptionistQueue_thenAddToReceptionistQueue() throws Exception {
        mvc.perform(post("/api/queues/receptionist/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.queueType").value("R"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void b_WhenPostToDoctorQueue_thenAddToDoctorQueue() throws Exception {
        mvc.perform(post("/api/queues/doctor/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.queueType").value("D"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void c_WhentPostToReceptionistQueue_andUserExistThenError() throws Exception {
        mvc.perform(post("/api/queues/receptionist/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void d_WhentPostToReceptionistQueue_andUserExistThenError() throws Exception {
        mvc.perform(post("/api/queues/doctor/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    void e_WhenGetQueues_thenReturnQueues() throws Exception {
        mvc.perform(get("/api/queues")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void f_WhenGetUserFromRecQueue_thenReturnUserPos() throws Exception {
        mvc.perform(get("/api/queues/receptionist/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.queueType").value("R"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void g_WhenGetUserFromDocQueue_thenReturnUserPos() throws Exception {
        mvc.perform(get("/api/queues/doctor/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.queueType").value("D"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void h_WhenDeleteReceptionist_thenRemoveHead() throws Exception {
        mvc.perform(delete("/api/queues/receptionist")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void i_WhenDeleteReceptionist_thenRemoveHead() throws Exception {
        mvc.perform(delete("/api/queues/doctor")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void j_WhenDeleteReceptionist_andIsEmptythenError() throws Exception {
        mvc.perform(delete("/api/queues/receptionist")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void k_WhenDeleteDoctor_andIsEmptythenError() throws Exception {
        mvc.perform(delete("/api/queues/doctor")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void l_WhenGetUserFromRecQueue_andUserNotInQueueThenError() throws Exception {
        mvc.perform(get("/api/queues/receptionist/-99")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void m_WhenGetUserFromDocQueue_andUserNotInQueueThenError() throws Exception {
        mvc.perform(get("/api/queues/doctor/-99")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
}
