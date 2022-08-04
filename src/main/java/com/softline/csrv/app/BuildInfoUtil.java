package com.softline.csrv.app;

import io.jmix.core.Messages;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Получаем параметры сборки из JAR архива, файл /META-INF/build-info.properties
 */
@Service
public class BuildInfoUtil {
    @Autowired
    private Messages messages;

    /**
     * @param clazz
     * @param log
     */
    private Properties getBuildInfo(Class clazz, Logger log) {
        Properties properties = new Properties();
        try {
            String buildInfoPath = findResourcePath(clazz, "/META-INF/build-info.properties");
            log.debug("read build-info.properties :[{}]", buildInfoPath);
            if (buildInfoPath.startsWith("/")) {
                log.info("build-info.properties NOT FOUND. CHECK IN THE JAR '/META-INF/build-info.properties'");
                return properties;
            }

            URL url = new URL(buildInfoPath);
            InputStream is = url.openStream();
            if (is != null) {
                log.debug("Load property file:[{}]", url);
                properties.load(is);
                log.debug("properties: {}", properties);
            } else {
                log.debug("Error load properties file");
            }
        } catch (Exception e) {
            log.error("read properties file exception", e);
        }
        return properties;
    }

    private static String findResourcePath(Class clazz, String name) {
        String simpleClassName = clazz.getSimpleName() + ".class";
        String path = clazz.getResource(simpleClassName).toString();
        String rootPath = path.substring(0, path.lastIndexOf("!") + 1);
        String result = rootPath + name;
        return result;
    }

    public String buildInfoText(Class clazz, Logger log){
        Properties properties = getBuildInfo(clazz, log);
        String date = properties.getProperty("build.time");
        String buildName = properties.getProperty("build.name");
        String buildVersion = properties.getProperty("build.version");
        String messageTemplate = messages.getMessage(BuildInfoUtil.class, "buildInfoUtil.help");
        String message = String.format(messageTemplate, buildName + "-" + buildVersion, date);
        return message;
    }
}
