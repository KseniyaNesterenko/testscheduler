package ru.itmo.testscheduler.config.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.testscheduler.api.dto.ContainerDto;
import ru.itmo.testscheduler.config.api.ConfigFacade;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigFacadeImpl implements ConfigFacade {

    private final ConfigGenerator generator;

    @Override
    public String generate(List<ContainerDto> containers, String format) {

        if ("yaml".equalsIgnoreCase(format)) {
            return generator.toYaml(containers);
        }

        return generator.toJson(containers);
    }
}