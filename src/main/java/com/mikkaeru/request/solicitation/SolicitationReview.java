package com.mikkaeru.request.solicitation;

import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "solicitation-review", url = "${solicitation-review-url}")
public interface SolicitationReview {

    @RequestMapping(method = POST, value = "${solicitation-review-path}")
    ReviewResponse solicitation(ReviewRequest reviewRequest);
}
