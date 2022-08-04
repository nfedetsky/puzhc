package com.softline.csrv.screen.xwikidoc;

import com.softline.csrv.entity.Request;
import com.softline.csrv.xwiki.XWikiDoc;
import io.jmix.core.DataManager;
import io.jmix.core.Sort;
import io.jmix.ui.component.Button;
import io.jmix.ui.navigation.UrlRouting;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.util.List;

@UiController("XWikiDoc.browse")
@UiDescriptor("x-wiki-doc-browse.xml")
@LookupComponent("xWikiDocsTable")
public class XWikiDocBrowse extends StandardLookup<XWikiDoc> {

    private static final Logger log = LoggerFactory.getLogger(XWikiDocBrowse.class);

    @Subscribe("changeLinks")
    protected void changeLinks(Button.ClickEvent event) {
        changeLinks();
    }

    @Autowired
    protected UrlRouting urlRouting;

    @Autowired
    private DataManager dataManager;

    private void changeLinks() {

        int offset = 0;
        List<XWikiDoc> batch;
        do {
            batch = dataManager.load(XWikiDoc.class)
                    .all()
                    .firstResult(offset)
                    .maxResults(10)
                    .sort(Sort.by("xwdId"))
                    .list();
            for (XWikiDoc xWikiDoc : batch) {
                try {
                    xWikiDoc.setXwdContent(change(xWikiDoc.getXwdContent()));
                    xWikiDoc.setXwdTitle(change(xWikiDoc.getXwdTitle()));
                } catch (MalformedURLException e) {
                    log.error("Ошибка при замене ссылок на странице {}.", xWikiDoc.getXwdId(), e);
                } catch (IllegalStateException ex) {
                    log.error("Ошибка при замене ссылок на странице {}. " +
                            "Требуется замена ссылок для страницы вручную.", xWikiDoc.getXwdId(), ex);
                }
                dataManager.save(xWikiDoc);
            }
            offset = offset + 10;
        } while (!batch.isEmpty());
    }

    /**
     * Заменяеть ссылки, которые содержат адрес Jira(lc.sk.roskazna.ru)
     * на заявки из ПУЖЦ.
     * @param text - полный текст страницы или заголовок страницы.
     * @return
     * @throws MalformedURLException
     */
    private String change(String text) throws MalformedURLException {
        String[] strs = text.split("url:");
        String newStr = "";
        for (String str : strs) {
            if (str.contains("lc.sk.roskazna.ru")) {
                int idx = str.indexOf("||");
                String link = str.substring(0, idx);
                idx = link.lastIndexOf("/");
                String keyNum = link.substring(idx + 1);

                Request request = dataManager.load(Request.class)
                        .query("select r from Request r where r.keyNum =:keyNum")
                        .parameter("keyNum", keyNum)
                        .optional().orElse(null);
                if (request == null) {
                    throw new IllegalStateException("Нет заявки с таким номером в таблице fklis001_request_card: " + keyNum);
                }

                String newUrl = urlRouting.getRouteGenerator().getEditorRoute(request);
                str = str.replace(link, newUrl);

            }
            str = "url:" + str;
            newStr = newStr + str;
        }

        return newStr;
    }
}