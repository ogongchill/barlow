package com.barlow.app.api.controller.v1.term;

import com.barlow.app.support.response.ApiResponse;
import com.barlow.core.domain.account.term.Term;
import com.barlow.core.domain.account.term.TermManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/term")
public class TermController {

    private static final Logger log = LoggerFactory.getLogger(TermController.class);

    private final TermManager termManager;

    public TermController(TermManager termManager) {
        this.termManager = termManager;
    }

    @GetMapping("/active")
    public ApiResponse<ActiveTermResponse> getActiveTerms() {
        log.info("Received active term request.");
        List<Term> activeTerms = termManager.retrieveActiveTerms();
        return ApiResponse.success(ActiveTermResponse.fromTerms(activeTerms));
    }
}
