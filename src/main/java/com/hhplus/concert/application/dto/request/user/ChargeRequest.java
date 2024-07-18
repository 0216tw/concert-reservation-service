package com.hhplus.concert.application.dto.request.user;

import com.hhplus.concert.common.constants.MessageEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public class ChargeRequest {

    @Min(value = 1)
    private long charge;

    public ChargeRequest(long charge) {
        this.charge = charge;
    }

    public ChargeRequest() {
    }

    public long getCharge() {
        return charge;
    }
}
