package com.econo_4factorial.newproject.pathway.controller;

import com.econo_4factorial.newproject.pathway.service.PathwayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pathways")
public class PathwayController {
    private final PathwayService pathwayService;
}
