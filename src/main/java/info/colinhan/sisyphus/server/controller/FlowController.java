package info.colinhan.sisyphus.server.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/flows")
public class FlowController {
    @Data
    public static class FlowInfo {
        public String name;
        public String description;
    }
    @GetMapping("/")
    public List<FlowInfo> getFlows() {
        return Collections.emptyList();
    }
}
