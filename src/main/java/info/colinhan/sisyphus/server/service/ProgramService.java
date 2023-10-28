package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.dto.FlowError;
import info.colinhan.sisyphus.server.dto.FlowInfo;
import info.colinhan.sisyphus.server.dto.GetProgramInfoResponse;
import info.colinhan.sisyphus.server.dto.ProgramInfo;
import info.colinhan.sisyphus.server.model.FlowVersionEntity;
import info.colinhan.sisyphus.server.model.ProgramEntity;
import info.colinhan.sisyphus.util.ResultOrErrors;
import info.colinhan.sisyphus.util.ResultWithErrors;

import java.util.List;

public interface ProgramService {
    GetProgramInfoResponse buildProgramInfo(List<ProgramEntity> programs);

    List<ResultWithErrors<FlowInfo, FlowError>> getAvailableFlows();

    ResultOrErrors<ProgramInfo, FlowError> createProgram(FlowVersionEntity flowVersion, String name, String name1);
}
