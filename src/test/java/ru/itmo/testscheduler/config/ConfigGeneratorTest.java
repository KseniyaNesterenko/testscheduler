package ru.itmo.testscheduler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.itmo.testscheduler.api.dto.ContainerDto;
import ru.itmo.testscheduler.collector.internal.JUnitCollector;
import ru.itmo.testscheduler.config.internal.ConfigFacadeImpl;
import ru.itmo.testscheduler.config.internal.ConfigGenerator;
import ru.itmo.testscheduler.testexecution.api.TestExecutionFacade;
import ru.itmo.testscheduler.testexecution.model.TestStatus;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigGeneratorTest {

    private final ConfigGenerator generator = new ConfigGenerator();

    @Test
    void shouldGenerateJson() {

        ContainerDto dto = ContainerDto.builder()
                .tests(List.of("t1"))
                .totalTime(1.0)
                .build();

        String result = generator.toJson(List.of(dto));

        assertThat(result).contains("containers");
    }

    @Test
    void shouldGenerateYaml() {

        ContainerDto dto = ContainerDto.builder()
                .tests(List.of("t1"))
                .totalTime(1.0)
                .build();

        String result = generator.toYaml(List.of(dto));

        assertThat(result).contains("containers");
    }

    @Test
    void shouldGenerateYamlForMultipleContainers() {

        ConfigGenerator gen = new ConfigGenerator();

        ContainerDto dto1 = ContainerDto.builder()
                .tests(List.of("t1"))
                .totalTime(1.0)
                .build();

        ContainerDto dto2 = ContainerDto.builder()
                .tests(List.of("t2"))
                .totalTime(2.0)
                .build();

        var result = gen.toYaml(List.of(dto1, dto2));

        assertThat(result).contains("containers");
    }

    @Test
    void shouldUseJsonByDefault() {

        ConfigFacadeImpl facade = new ConfigFacadeImpl(new ConfigGenerator());

        var result = facade.generate(List.of(), "json");

        assertThat(result).isNotNull();
    }

    @Test
    void shouldUseYamlWhenSpecified() {

        ConfigFacadeImpl facade = new ConfigFacadeImpl(new ConfigGenerator());

        var result = facade.generate(List.of(), "yaml");

        assertThat(result).isNotNull();
    }


    @Test
    void shouldSerializeRealJsonStructure() {

        ConfigGenerator gen = new ConfigGenerator();

        ContainerDto dto = ContainerDto.builder()
                .tests(List.of("t1", "t2"))
                .totalTime(12.5)
                .build();

        String json = gen.toJson(List.of(dto));

        assertThat(json).contains("t1");
        assertThat(json).contains("total_time");
    }

    @Test
    void shouldSerializeYamlStructureProperly() {

        ConfigGenerator gen = new ConfigGenerator();

        ContainerDto dto = ContainerDto.builder()
                .tests(List.of("a", "b", "c"))
                .totalTime(5.0)
                .build();

        String yaml = gen.toYaml(List.of(dto));

        assertThat(yaml).contains("containers");
        assertThat(yaml).contains("tests");
    }

    @Test
    void shouldThrowRuntimeException_whenJsonFails() throws Exception {

        ObjectMapper mapper = mock(ObjectMapper.class);
        ObjectMapper yaml = new ObjectMapper();

        ConfigGenerator gen = new ConfigGenerator(mapper, yaml);

        when(mapper.writerWithDefaultPrettyPrinter())
                .thenThrow(new RuntimeException("boom"));

        assertThatThrownBy(() -> gen.toJson(List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowRuntimeException_whenYamlFails() throws Exception {

        ObjectMapper json = new ObjectMapper();
        ObjectMapper yaml = mock(ObjectMapper.class);

        ConfigGenerator gen = new ConfigGenerator(json, yaml);

        when(yaml.writeValueAsString(any()))
                .thenThrow(new RuntimeException("boom"));

        assertThatThrownBy(() -> gen.toYaml(List.of()))
                .isInstanceOf(RuntimeException.class);
    }
}