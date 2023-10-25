package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.*;
import info.colinhan.sisyphus.server.exception.E;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.repository.FlowVersionRepository;
import info.colinhan.sisyphus.server.repository.ProgramRepository;
import info.colinhan.sisyphus.server.service.ProgramService;
import info.colinhan.sisyphus.server.utils.ProgramStatus;
import info.colinhan.sisyphus.util.ResultOrErrors;
import info.colinhan.sisyphus.util.ResultOrErrors;
import info.colinhan.sisyphus.util.ResultWithErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequestMapping("/api/programs")
public class ProgramController {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private FlowVersionRepository flowVersionRepository;
    @Autowired
    private ProgramService programService;

    @GetMapping("/available-flow")
    public ResultOrErrors<List<ResultWithErrors<FlowInfo, FlowError>>, String> getAvailableFlow() {
        return ResultOrErrors.of(
                programService.getAvailableFlows()
        );
    }

    @GetMapping("/")
    public ResultOrErrors<GetProgramInfoResponse, String> getPrograms() {
        return ResultOrErrors.of(
                GetProgramInfoResponse.from(programRepository.findAllByStatus(ProgramStatus.IN_PROGRESS))
        );
    }

    @PutMapping("/")
    public ResultOrErrors<ProgramInfo, FlowError> createProgram(
            @RequestBody CreateProgramRequest request,
            UserPrincipal userPrincipal
    ) {
        FlowVersionEntity version = E.assertPresent(flowVersionRepository.findOneByFlowIdAndVersion(request.getFlowId(), request.getVersion()));
        return programService.createProgram(version, request.getName(), userPrincipal.getName());
    }
}
