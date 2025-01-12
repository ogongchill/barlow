package com.barlow.core.api.controller.v1.committee;

import com.barlow.core.domain.committee.CommitteeNotificationSubscriptionRetrieveService;
import com.barlow.core.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/committee")
public class CommitteeSubscriptionController {

    private final CommitteeNotificationSubscriptionRetrieveService committeeNotificationSubscriptionRetrieveService;

    public CommitteeSubscriptionController(CommitteeNotificationSubscriptionRetrieveService committeeNotificationSubscriptionRetrieveService) {
        this.committeeNotificationSubscriptionRetrieveService = committeeNotificationSubscriptionRetrieveService;
    }

    @GetMapping("/infos")
    public ApiResponse<CommitteeSubscriptionResponse> retrieveCommitteeAccountSubscribeInfo() {
        // 여기에 인증되면 구독목록 조회후 리턴, 인증 안되어있으면 모두 false로 응답하는 내용

        return null;
    }
}
