package info.colinhan.sisyphus.server.controller;

import info.colinhan.sisyphus.exception.ParserException;
import info.colinhan.sisyphus.jacal.model.Form;
import info.colinhan.sisyphus.jacal.parser.JacalParser;
import info.colinhan.sisyphus.server.dto.*;
import info.colinhan.sisyphus.server.exception.BadRequestException;
import info.colinhan.sisyphus.server.exception.E;
import info.colinhan.sisyphus.server.model.FormEntity;
import info.colinhan.sisyphus.server.model.FormVersionEntity;
import info.colinhan.sisyphus.server.repository.FormRepository;
import info.colinhan.sisyphus.server.repository.FormVersionRepository;
import info.colinhan.sisyphus.server.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forms")
public class FormController {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormVersionRepository formVersionRepository;

    @GetMapping("/")
    public Response<List<FormInfo>> getForms() {
        return Response.of(
                formRepository.findAll().stream()
                        .map(f -> new FormInfo(f,
                                formVersionRepository.findFirstByFormIdOrderByVersionDesc(f.getId()).orElse(null)))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/")
    public Response<FormInfo> createForm(
            @RequestBody CreateFlowRequest request,
            Principal userPrincipal
    ) {
        FormEntity flow = formRepository.save(
                FormEntity.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .createdByUsername(userPrincipal.getName())
                        .createdAt(new Timestamp(new Date().getTime()))
                        .build()
        );
        return Response.of(new FormInfo(flow, null));
    }

    @GetMapping("/{formId}")
    public Response<FormInfo> getForm(@PathVariable Long formId) {
        FormEntity FormEntity = E.assertPresent(formRepository.findById(formId),"Flow");
        FormVersionEntity version = formVersionRepository.findFirstByFormIdOrderByVersionDesc(formId).orElse(null);
        return Response.of(new FormInfo(FormEntity, version));
    }

    @PutMapping("/{formId}")
    public Response<FormInfo> updateForm(
            @PathVariable Long formId,
            @RequestBody FormInfo FormInfo,
            Principal userPrincipal
    ) {
        FormEntity FormEntity = formRepository.findById(formId).orElseThrow(() -> new RuntimeException("Flow not found"));
        if (!FormEntity.getName().equals(FormInfo.getName())) {
            throw new BadRequestException("Flow name is readonly!");
        }

        FormVersionEntity version = FormInfo.toVersion(userPrincipal.getName());
        version.setFormId(formId);
        version = formVersionRepository.save(version);

        FormEntity.setDescription(FormInfo.getDescription());
        FormEntity = formRepository.save(FormEntity);

        return Response.of(new FormInfo(FormEntity, version));
    }

    @PostMapping("/{formId}/model")
    public Response<GetFormModelResponse> getFormModel(
            @PathVariable Long formId,
            @RequestBody ParseCodeRequest request) {
        String code = request.getCode();
        if (code == null) {
            FormVersionEntity version = E.assertPresent(formVersionRepository.findFirstByFormIdOrderByVersionDesc(formId), "Version");
            code = version.getCode();
            if (code == null) {
                code = "";
            }
        }
        try {
            Form form = JacalParser.parse(code);
            return Response.of(new GetFormModelResponse(form));
        } catch (ParserException e) {
            return Response.of(new GetFormModelResponse(e.getErrors()));
        }
    }
}
