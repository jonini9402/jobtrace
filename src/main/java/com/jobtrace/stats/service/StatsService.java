package com.jobtrace.stats.service;

import com.jobtrace.domain.User;
import com.jobtrace.stats.dto.StatsResponse;

public interface StatsService {
    StatsResponse getStats(User user);
}
