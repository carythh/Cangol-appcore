/**
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cangol
 */
public class UrlUtils {
    private UrlUtils() {
    }

    /**
     * 判断是否是url
     *
     * @param value
     * @return
     */
    public static boolean isUrl(String value) {
        if (value != null && !"".equals(value)) {
            return value
                    .matches("(((http|ftp|https|file)://)?([\\w\\-]+\\.)+[\\w\\-]+(/[\\w\\u4e00-\\u9fa5\\-\\./?\\@\\%\\!\\&=\\+\\~\\:\\#\\;\\,]*)?)");
        } else {
            return false;
        }
    }

    /**
     * 从url获取主机
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {

        try {
            return new URL(url).getHost();
        } catch (MalformedURLException e) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 从url获取参数map
     *
     * @param url
     * @return Map
     */
    public static Map<String, String> getParams(String url) {

        String query = "";
        try {
            query = new URL(url).getQuery();
        } catch (MalformedURLException e) {
            query = "";
        }

        Map<String, String> queries = new HashMap<String, String>();
        if (query == null) {
            return queries;
        }

        for (String entry : query.split("&")) {
            String[] keyvalue = entry.split("=");
            if (keyvalue.length != 2) {
                continue;
            }
            queries.put(keyvalue[0], keyvalue[1]);
        }
        return queries;
    }

    /**
     * @param url
     * @return
     */
    public static String getPath(String url) {
        String command = null;
        if (url != null && url.contains("://")) {
            if (url.contains("?")) {
                command = url.substring(url.indexOf("://") + 3, url.indexOf("?"));
            } else {
                command = url.substring(url.indexOf("://") + 3, url.length());
            }
        }
        return command;
    }

    /**
     * @param url
     * @return
     */
    public static String getScheme(String url) {
        String command = null;
        if (url != null && url.contains("://")) {
            command = url.substring(0, url.indexOf("://"));
        }
        return command;
    }
}
