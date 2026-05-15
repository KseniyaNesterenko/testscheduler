package ru.itmo.testscheduler.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.itmo.testscheduler.collector.api.CollectorFacade;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectorFacade collectorFacade;

    @Test
    void shouldUploadReportSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "report.xml",
                "text/xml",
                "<xml/>".getBytes()
        );

        mockMvc.perform(multipart("/reports")
                        .file(file)
                        .param("runId", "r1"))
                .andExpect(status().isOk());

        verify(collectorFacade)
                .collect(any(), eq("r1"), eq("junit"));
    }

    @Test
    void shouldReturn500_whenCollectorFails() throws Exception {

        doThrow(new RuntimeException("fail"))
                .when(collectorFacade)
                .collect(any(), anyString(), anyString());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "report.xml",
                "text/xml",
                "<xml/>".getBytes()
        );

        mockMvc.perform(multipart("/reports")
                        .file(file)
                        .param("runId", "r1"))
                .andExpect(status().is5xxServerError());
    }
}
