package com.ssafy.drcha.transaction.event;

import com.ssafy.drcha.iou.entity.Iou;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewDepositEvent {
    private Iou iou;
    private BigDecimal amount;
}