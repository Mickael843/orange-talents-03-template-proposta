package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "card-resource", url = "${card-resource-url}")
public interface CardResource {

    @PostMapping
    CardResponse processCard(CardRequest cardRequest);

    @PostMapping("${card-resource-path-block-card}")
    LockSolicitationResponse blockCard(@PathVariable String id, LockSolicitation request);

    @GetMapping
    CardResponse getCard(@RequestParam String idProposta);
}
