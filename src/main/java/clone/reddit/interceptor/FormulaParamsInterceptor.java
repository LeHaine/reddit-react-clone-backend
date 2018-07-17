package clone.reddit.interceptor;

import org.hibernate.EmptyInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by colt on 7/17/18.
 */
@Component
public class FormulaParamsInterceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        String newSql = sql;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (username.equalsIgnoreCase("anonymousUser")) {
                username = "";
            }
            newSql = newSql.replace("{USERNAME}", username);
            return super.onPrepareStatement(newSql);
        }

        return super.onPrepareStatement(sql);
    }
}
