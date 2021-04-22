package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.LockSolicitation;
import com.mikkaeru.request.card.dto.LockSolicitationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "card-resource", url = "${card-resource-url}")
public interface CardResource {

    @RequestMapping(method = POST, value = "${card-resource-path}")
    CardResponse processCard(CardRequest cardRequest);

    @RequestMapping(method = POST, value = "${card-resource-path-block-card}")
    LockSolicitationResponse blockCard(@PathVariable String id, LockSolicitation request);

    @RequestMapping(method = GET, value = "${card-resource-path}/")
    CardResponse getCard(@PathVariable String cardId);
}
