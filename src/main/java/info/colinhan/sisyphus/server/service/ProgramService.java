package info.colinhan.sisyphus.server.service;

import info.colinhan.sisyphus.server.dto.*;
import info.colinhan.sisyphus.util.ResultOrErrors;

public interface ProgramService {
    GetProgramInfoResponse buildProgramInfo();

    ResultOrErrors<ProgramInfo, FlowError> createProgram(CreateProgramRequest request, String creator);
}
