package com.amowel.configuration.shell;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

/**
 * Created by amowel on 30.04.17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomBannerProvider implements BannerProvider {

    @Override
    public String getBanner() {
        return null;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getWelcomeMessage() {
        return null;
    }

    @Override
    public String getProviderName() {
        return "amowel";
    }
}
