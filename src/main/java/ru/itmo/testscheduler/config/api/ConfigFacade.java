package ru.itmo.testscheduler.config.api;

import ru.itmo.testscheduler.api.dto.ContainerDto;

import java.util.List;

public interface ConfigFacade {
    String generate(List<ContainerDto> containers, String format);
}