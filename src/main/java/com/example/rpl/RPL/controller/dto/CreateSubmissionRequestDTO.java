package com.example.rpl.RPL.controller.dto;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
class CreateSubmissionRequestDTO {

    @Valid
    private List<Map<String, String>> files;

}
