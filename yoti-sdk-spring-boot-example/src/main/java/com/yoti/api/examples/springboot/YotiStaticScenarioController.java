package com.yoti.api.examples.springboot;

import com.yoti.api.client.YotiClient;
import com.yoti.api.spring.YotiClientProperties;
import com.yoti.api.spring.YotiProperties;

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
public class YotiStaticScenarioController extends WebMvcConfigurerAdapter {

    private final YotiProperties yotiProperties;
    private final YotiClientProperties yotiClientProperties;

    @Autowired
    public YotiStaticScenarioController(YotiProperties yotiProperties, YotiClientProperties yotiClientProperties) {
        this.yotiProperties = yotiProperties;
        this.yotiClientProperties = yotiClientProperties;
    }

    /**
     * This endpoint displays the login page, based on a static scenario. It's a GET endpoint.
     */
    @RequestMapping({ "/", "/static-scenario-demo"})
    public String loginWithStaticQrCode(final Model model) {
        model.addAttribute("applicationId", yotiClientProperties.getApplicationId());
        model.addAttribute("scenarioId", yotiProperties.getScenarioId());
        return "staticScenarioDemo";
    }

}
