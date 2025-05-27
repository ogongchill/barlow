package com.barlow.app.api.controller.v1.version;

import com.barlow.app.support.response.ApiResponse;
import com.barlow.core.domain.version.ClientVersionQuery;
import com.barlow.core.domain.version.ClientVersionService;
import com.barlow.core.enumerate.ClientVersionStatus;
import com.barlow.core.enumerate.DeviceOs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client-version")
public class ClientVersionCheckController {

    private final ClientVersionService clientVersionService;

    public ClientVersionCheckController(ClientVersionService clientVersionService) {
        this.clientVersionService = clientVersionService;
    }

    @GetMapping("/check")
    public ApiResponse<ClientVersionCheckResponse> retrieveVersionCheckResponse(
            @RequestHeader("X-App-Version") String appVersion,
            @RequestHeader("X-Device-Os") String deviceOs
    ) {
        ClientVersionStatus status = clientVersionService.checkClientVersion(new ClientVersionQuery(DeviceOs.valueOf(deviceOs),appVersion));
        ClientVersionCheckApiSpecComposer composer = new ClientVersionCheckApiSpecComposer(status);
        return ApiResponse.success(composer.compose());
    }
}
