package com.project.FitLink.auditing;

import com.project.FitLink.auth.FitLinkUserDetails;
import com.project.FitLink.utils.FitLinkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.of("SYSTEM");
        }

        FitLinkUserDetails user = FitLinkUtils.getCurrentUser();
        return Optional.of(user.getEmail());
    }
}