package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.server.dto.CreateProgramRequest;
import info.colinhan.sisyphus.server.dto.CreateProgramResponse;
import info.colinhan.sisyphus.server.dto.FlowEntityDto;
import info.colinhan.sisyphus.server.dto.GetProgramInfoResponse;
import info.colinhan.sisyphus.server.exception.E;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.model.ProgramEntity;
import info.colinhan.sisyphus.server.repository.FlowVersionRepository;
import info.colinhan.sisyphus.server.repository.ProgramRepository;
import info.colinhan.sisyphus.server.repository.ProgramVariableRepository;
import info.colinhan.sisyphus.server.utils.ProgramStatus;
import info.colinhan.sisyphus.server.utils.Response;
import info.colinhan.sisyphus.server.utils.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flows")
public class ProgramController {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramVariableRepository programVariableRepository;
    @Autowired
    private FlowVersionRepository flowVersionRepository;

    @GetMapping("/")
    public Response<GetProgramInfoResponse> getPrograms() {
        return Response.of(
                GetProgramInfoResponse.from(programRepository.findAllByStatus(ProgramStatus.IN_PROGRESS))
        );
    }

    @PutMapping("/")
    public Response createProgram(
            @RequestBody CreateProgramRequest request,
            UserPrincipal userPrincipal
    ) {
        FlowVersionEntity version = E.assertPresent(flowVersionRepository.findOneByFlowIdAndVersion(request.getFlowId(), request.getVersion()));
        if (version.getModel() == null) {
            modelCompileService.compile(version);
        }
        programRepository.save(ProgramEntity.builder()
                        .name(request.getName())
                        .flowVersionId(version.getId())
                        .createdByUsername(userPrincipal.getName())
                        .createdAt(U.timeNow())
                        .updatedAt(U.timeNow())
                        .status(ProgramStatus.IN_PROGRESS)
                        .currentOwner(userPrincipal.getName())
                .build())
    }
}
