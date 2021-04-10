package com.mikkaeru.request;

import com.mikkaeru.request.dto.ReviewRequest;
import com.mikkaeru.request.dto.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "solicitation-review", url = "${solicitation-review-url}")
public interface SolicitationReview {

    @RequestMapping(method = POST, value = "${solicitation-review-path}")
    ReviewResponse solicitation(ReviewRequest reviewRequest);
}
