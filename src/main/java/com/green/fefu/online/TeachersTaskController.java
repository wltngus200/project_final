package com.green.fefu.online;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/teacher")
@Slf4j
public class TeachersTaskController {
    private final TeachersTaskService service;
}
