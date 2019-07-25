package com.ranger.config;

import org.springframework.data.domain.AuditorAware;

/**
 * Created by mercury on 2016/12/26.
 */
public class NullAuditorBean implements AuditorAware {

    @Override
    public Object getCurrentAuditor() {
        return null;
    }
}
