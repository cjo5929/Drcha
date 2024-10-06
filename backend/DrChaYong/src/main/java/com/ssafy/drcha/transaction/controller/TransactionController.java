package com.ssafy.drcha.transaction.controller;

import com.ssafy.drcha.global.api.dto.DepositResponse;
import com.ssafy.drcha.global.api.dto.TransferResponse;
import com.ssafy.drcha.global.api.dto.WithdrawResponse;
import com.ssafy.drcha.transaction.dto.DepositRequestDto;
import com.ssafy.drcha.transaction.dto.TransferRequestDto;
import com.ssafy.drcha.transaction.dto.VirtualAccountResponse;
import com.ssafy.drcha.transaction.dto.WithdrawRequestDto;
import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "거래 프로세스 관련 API")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "가상 계좌 생성", description = "차용증에 대한 가상 계좌를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "가상 계좌 생성 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = VirtualAccountResponse.class)))
    @PostMapping("/{iouId}")
    public ResponseEntity<?> createVirtualAccount(
            @Parameter(description = "차용증 ID", required = true)
            @PathVariable("iouId") Long iouId) {
        log.info("------ 차용증 ID : {}--------", iouId);
        VirtualAccount virtualAccount = transactionService.createVirtualAccount(iouId);
        VirtualAccountResponse response = VirtualAccountResponse.of(virtualAccount);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "사용자 계좌 입금", description = "사용자의 계좌에 입금을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "입금 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepositResponse.class)))
    @PostMapping("/deposit/member-account")
    public ResponseEntity<DepositResponse> depositMemberAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DepositRequestDto depositRequestDto) {
//        log.info("사용자 계좌 입금 요청 - 사용자: {}, 계좌번호: {}, 금액: {}",
//                 userDetails.getUsername(), depositRequestDto.getAccountNo(), depositRequestDto.getTransactionBalance());

        DepositResponse response = transactionService.depositMemberAccount(userDetails.getUsername(), depositRequestDto);

//        log.info("사용자 계좌 입금 완료 - 사용자: {}, 계좌번호: {}, 금액: {}",
//                 userDetails.getUsername(), depositRequestDto.getAccountNo(), depositRequestDto.getTransactionBalance());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "가상 계좌 입금", description = "특정 가상 계좌에 입금을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "입금 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DepositResponse.class)))
    @PostMapping("/deposit/virtual-account")
    public ResponseEntity<DepositResponse> deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody DepositRequestDto depositRequestDto) {
        DepositResponse response = transactionService.depositVirtualAccount(userDetails.getUsername(), depositRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "계좌 출금", description = "특정 계좌에서 출금을 수행합니다.")
    @ApiResponse(responseCode = "200", description = "출금 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WithdrawResponse.class)))
    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody WithdrawRequestDto withdrawRequestDto) {
        WithdrawResponse response = transactionService.withdraw(userDetails.getUsername(), withdrawRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "계좌 이체 test", description = "가상계좌 간 테스트 진행 -> 계좌이체 제대로 되는지 확인용")
    @ApiResponse(responseCode = "200", description = "이체 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransferResponse.class)))
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TransferRequestDto transferRequestDto) {
        TransferResponse response = transactionService.transfer(userDetails.getUsername(), transferRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "채무 상환", description = "채무자가 차용증의 가상계좌로 계좌이체를 진행합니다.")
    @ApiResponse(responseCode = "200", description = "이체 성공",
                 content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TransferResponse.class)))
    @PostMapping("/repay/{iouId}")
    public ResponseEntity<TransferResponse> repayDebt(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long iouId,
            @RequestParam("amount")BigDecimal amount) {
        TransferResponse response = transactionService.repayDebt(userDetails.getUsername(), iouId, amount);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}