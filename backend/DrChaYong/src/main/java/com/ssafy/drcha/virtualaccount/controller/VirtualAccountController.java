package com.ssafy.drcha.virtualaccount.controller;

import com.ssafy.drcha.virtualaccount.dto.VirtualAccountResponse;
import com.ssafy.drcha.virtualaccount.entity.VirtualAccount;
import com.ssafy.drcha.virtualaccount.service.VirtualAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/virtual-account")
@RequiredArgsConstructor
@Tag(name = "Virtual Account", description = "무통장 계좌 API")
public class VirtualAccountController {

    private final VirtualAccountService virtualAccountService;

    @Operation(summary = "가상 계좌 생성", description = "차용증에 대한 가상 계좌를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "가상 계좌 생성 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VirtualAccountResponse.class)))
    @PostMapping("/{iouId}")
    public ResponseEntity<?> createVirtualAccount(
            @Parameter(description = "차용증 ID", required = true)
            @PathVariable("iouId") Long iouId) {
        VirtualAccount virtualAccount = virtualAccountService.createVirtualAccount(iouId);
        VirtualAccountResponse response = VirtualAccountResponse.of(virtualAccount);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}