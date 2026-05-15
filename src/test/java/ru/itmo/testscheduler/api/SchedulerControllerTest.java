package ru.itmo.testscheduler.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmo.testscheduler.scheduler.api.SchedulerFacade;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SchedulerController.class)
class SchedulerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SchedulerFacade schedulerFacade;

    @MockitoBean
    private TestExecutionFacade testExecutionFacade;

    @Test
    void shouldReturn200() throws Exception {

        when(schedulerFacade.scheduleAndGenerate(any(), any(), any()))
                .thenReturn("ok");

        mockMvc.perform(post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "testNames": ["t1"],
                      "containerCount": 2,
                      "runId": "r1"
                    }
                """)
                        .param("strategy", "ROUNDROBIN"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSaveTestExecutionResult() throws Exception {

        mockMvc.perform(post("/schedule/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "testName": "t1",
                  "duration": 1.0,
                  "status": "PASSED",
                  "runId": "r1",
                  "affinityKey": "a"
                }
            """))
                .andExpect(status().isOk());

        verify(testExecutionFacade).save(
                eq("t1"),
                eq(1.0),
                any(),
                eq("r1"),
                eq("a")
        );
    }

    @Test
    void shouldCallSchedulerWithJsonFormat() throws Exception {

        when(schedulerFacade.scheduleAndGenerate(any(), any(), any()))
                .thenReturn("ok");

        mockMvc.perform(post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "testNames": ["t1"],
                  "containerCount": 2,
                  "runId": "r1"
                }
            """)
                        .param("strategy", "ROUNDROBIN")
                        .param("format", "json"))
                .andExpect(status().isOk());
    }
}