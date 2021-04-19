package com.mikkaeru.request.card;

import com.mikkaeru.request.card.dto.CardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(name = "card-resource", url = "${card-resource-url}")
public interface CardResource {

    @RequestMapping(method = GET, value = "${card-resource-path}/${proposalId}")
    CardResponse processCard(@PathVariable String proposalId);
}
