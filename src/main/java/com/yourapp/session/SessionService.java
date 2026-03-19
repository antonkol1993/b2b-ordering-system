package com.yourapp.session;

import com.yourapp.common.AppConstants;
import com.yourapp.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SessionService {

    public void setSession(HttpSession session, User user) {
        session.setAttribute(AppConstants.SESSION_USER_ID, user.getId());
        session.setAttribute(AppConstants.SESSION_TENANT_ID, user.getTenant().getId());
        session.setAttribute(AppConstants.SESSION_ROLE, user.getRole());
    }

    public Long requireTenantId(HttpSession session) {
        Object value = session.getAttribute(AppConstants.SESSION_TENANT_ID);
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return (Long) value;
    }

    public Long requireUserId(HttpSession session) {
        Object value = session.getAttribute(AppConstants.SESSION_USER_ID);
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return (Long) value;
    }

    public String requireRole(HttpSession session) {
        Object value = session.getAttribute(AppConstants.SESSION_ROLE);
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        return (String) value;
    }

    public void requireAdmin(HttpSession session) {
        String role = requireRole(session);
        if (!AppConstants.ROLE_ADMIN.equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
    }

    public void clear(HttpSession session) {
        session.invalidate();
    }
}

