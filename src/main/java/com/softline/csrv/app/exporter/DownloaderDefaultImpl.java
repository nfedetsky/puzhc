package com.softline.csrv.app.exporter;

import io.jmix.ui.download.DownloaderImpl;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Make Primary bean for {@link DownloaderImpl}
 */
@Primary
@Component("ui_DownloaderDefault")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DownloaderDefaultImpl extends DownloaderImpl {
}