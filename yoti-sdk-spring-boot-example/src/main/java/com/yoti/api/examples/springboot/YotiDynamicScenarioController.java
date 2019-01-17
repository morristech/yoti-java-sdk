package com.yoti.api.examples.springboot;

import com.yoti.api.client.YotiClient;
import com.yoti.api.client.shareurl.DynamicScenario;
import com.yoti.api.client.shareurl.DynamicScenarioBuilder;
import com.yoti.api.client.shareurl.DynamicShareException;
import com.yoti.api.client.shareurl.ShareUrlResult;
import com.yoti.api.client.shareurl.policy.DynamicPolicy;
import com.yoti.api.client.shareurl.policy.DynamicPolicyBuilder;
import com.yoti.api.spring.YotiClientProperties;
import com.yoti.api.spring.YotiProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@ConditionalOnClass(YotiClient.class)
@EnableConfigurationProperties({ YotiClientProperties.class, YotiProperties.class })
@Controller
@EnableWebMvc
public class YotiDynamicScenarioController extends WebMvcConfigurerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(YotiDynamicScenarioController.class);

    private final YotiClient client;
    private final YotiProperties yotiProperties;
    private final YotiClientProperties yotiClientProperties;

    @Autowired
    public YotiDynamicScenarioController(final YotiClient client, YotiProperties yotiProperties, YotiClientProperties yotiClientProperties) {
        this.client = client;
        this.yotiProperties = yotiProperties;
        this.yotiClientProperties = yotiClientProperties;
    }

    /**
     * This endpoint displays the login page, based on a static scenario. It's a GET endpoint.
     */
    @RequestMapping("/dynamic-qr-code")
    public String loginWithDynamicQrCode(final Model model) throws DynamicShareException {
        DynamicPolicy dynamicPolicy = DynamicPolicyBuilder.newInstance()
                .withFullName()
                .withGivenNames()
                .withFamilyName()
                .withGender()
                .withDateOfBirth()
                .withAgeOver(18)
                .withAgeOver(21)
                .withAgeUnder(30)
                .withEmail()
                .withNationality()
                .withPhoneNumber()
                .withStructuredPostalAddress()
                .withWantedRememberMe(true)
                .build();
        DynamicScenario dynamicScenario = DynamicScenarioBuilder.newInstance()
                .withPolicy(dynamicPolicy)
                .withCallbackEndpoint("/login")
                .build();
        ShareUrlResult shareUrlResult = client.createShareUrl(dynamicScenario);

        model.addAttribute("qrCodeUrl", shareUrlResult.getUrl());
        model.addAttribute("sdkId", yotiClientProperties.getClientSdkId());
        model.addAttribute("scenarioId", yotiProperties.getScenarioId()); // We shouldn't need this, but browser.js curently requires it
        return "dynamicQrCode";
    }

}
