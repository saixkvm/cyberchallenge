package com.neueda.leap.merchantportal;

public class PayoutApprovalService {

    private PayoutRepository payoutRepository;

    public PayoutApprovalService(PayoutRepository payoutRepository) {
        this.payoutRepository = payoutRepository;
    }

    public void approve(Long payoutId, Long approvingUserId) {
        PayoutRequest payout = payoutRepository.findById(payoutId)
                .orElseThrow(() -> new RuntimeException("Payout not found"));

        // Prevent users from approving their own payouts
        if (payout.getRequestedByUserId().equals(approvingUserId)) {
            throw new RuntimeException("Users cannot approve their own payouts");
        }

        payout.setApprovalStatus("APPROVED");
        payout.setApprovedByUserId(approvingUserId);
        payoutRepository.save(payout);
    }
}

//this design has a fundamental flaw of not separating the roles 
//of the requester and the approver. So any user could approve their own payout.
//a06