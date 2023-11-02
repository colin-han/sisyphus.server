package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.CreateProgramRequest;
import info.colinhan.sisyphus.server.dto.FlowError;
import info.colinhan.sisyphus.server.dto.GetProgramInfoResponse;
import info.colinhan.sisyphus.server.dto.ProgramInfo;
import info.colinhan.sisyphus.server.repository.FlowVersionRepository;
import info.colinhan.sisyphus.server.service.ProgramService;
import info.colinhan.sisyphus.util.ResultOrErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
    @Autowired
    private ProgramService programService;

    @GetMapping("/")
    public ResultOrErrors<GetProgramInfoResponse, String> getPrograms() {
        return ResultOrErrors.of(
                programService.buildProgramInfo()
        );
    }

    @PutMapping("/")
    public ResultOrErrors<ProgramInfo, FlowError> createProgram(
            @RequestBody CreateProgramRequest request,
            Principal userPrincipal
    ) {
        return programService.createProgram(request, userPrincipal.getName());
    }
}
