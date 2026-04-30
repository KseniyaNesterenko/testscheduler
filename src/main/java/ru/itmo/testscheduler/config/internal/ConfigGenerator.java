package ru.itmo.testscheduler.config.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itmo.testscheduler.api.dto.ContainerDto;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfigGenerator {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private final CommandBuilder commandBuilder;

    public String toJson(List<ContainerDto> containers) {
        log.debug("Generating JSON config: containers={}", containers.size());

        try {
            String result = jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(build(containers));

            log.debug("JSON config generated successfully");

            return result;

        } catch (Exception e) {
            log.error("Failed to generate JSON config", e);
            throw new RuntimeException(e);
        }
    }

    public String toYaml(List<ContainerDto> containers) {
        log.debug("Generating YAML config: containers={}", containers.size());

        try {
            String result = yamlMapper.writeValueAsString(build(containers));

            log.debug("YAML config generated successfully");

            return result;

        } catch (Exception e) {
            log.error("Failed to generate YAML config", e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> build(List<ContainerDto> containers) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("containers", containers.stream().map(c -> {
            Map<String, Object> m = new LinkedHashMap<>();

            m.put("tests", c.getTests());
            m.put("total_time", c.getTotalTime());
            m.put("commands", commandBuilder.buildCommands(c.getTests()));

            return m;
        }).toList());

        return result;
    }
}