package tqs.myvet.ControllerTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import tqs.myvet.controllers.QueueRestController;

@WebMvcTest(QueueRestController.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
class QueueControllerTest {
    
    @Autowired
    private MockMvc mvc;


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
    void c_WhenGetQueues_thenReturnQueues() throws Exception {
        mvc.perform(get("/api/queues")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void d_WhenGetUserFromRecQueue_thenReturnUserPos() throws Exception {
        mvc.perform(get("/api/queues/receptionist/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.queueType").value("R"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void e_WhenGetUserFromDocQueue_thenReturnUserPos() throws Exception {
        mvc.perform(get("/api/queues/doctor/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2))
            .andExpect(jsonPath("$.queueType").value("D"))
            .andExpect(jsonPath("$.queuePos").value(0));
    }

    @Test
    void f_WhenDeleteReceptionist_thenRemoveHead() throws Exception {
        mvc.perform(delete("/api/queues/receptionist")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void g_WhenDeleteReceptionist_thenRemoveHead() throws Exception {
        mvc.perform(delete("/api/queues/doctor")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    
}
