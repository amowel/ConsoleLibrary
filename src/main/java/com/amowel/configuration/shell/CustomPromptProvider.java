package com.amowel.configuration.shell;

import org.hibernate.internal.CriteriaImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * Created by amowel on 30.04.17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomPromptProvider implements PromptProvider {
    @Override
    public String getPrompt() {
        return "library> ";
    }

    @Override
    public String getProviderName() {
        return "custom provider";
    }
}
