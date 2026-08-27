package com.neueda.leap.merchantportal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class MerchantController {

    @Autowired
    private PayoutRepository payoutRepository;

    @GetMapping("/api/payouts/{payoutId}")
    public ResponseEntity<PayoutRequest> getPayout(@PathVariable Long payoutId, Principal principal) {
        PayoutRequest payout = payoutRepository.findById(payoutId).orElse(null);
        // Return 404 for both "not found" and "not yours" so IDs can't be enumerated.
        if (payout == null || !isOwnedByCaller(payout, principal)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(payout);
    }

    private boolean isOwnedByCaller(PayoutRequest payout, Principal principal) {
        if (principal == null) {
            return false;
        }
        Long callerMerchantId = Long.valueOf(principal.getName());
        return callerMerchantId.equals(payout.getMerchantId());
    }
}
