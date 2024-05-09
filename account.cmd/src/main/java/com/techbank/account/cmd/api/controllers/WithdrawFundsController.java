package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/withdrawFunds")
public class WithdrawFundsController {

    private final CommandDispatcher commandDispatcher;

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable String id,
                                                      @RequestBody WithdrawFundsCommand command) {
        command.setId(id);
        commandDispatcher.send(command);
        return new ResponseEntity<>(new OpenAccountResponse("Withdraw funds request completed successfully", command.getId()), OK);
    }

}
