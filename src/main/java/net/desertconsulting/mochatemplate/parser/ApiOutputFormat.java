/*
 * Copyright 2016 Patrizio Bruno <desertconsulting@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.desertconsulting.mochatemplate.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expected output type when requesting an api "template".
 * @author Patrizio Bruno {@literal <desertconsulting@gmail.com>}
 */
public enum ApiOutputFormat {
    JSON,
    XML;

    private final static Pattern MTYPE_PATTERN = Pattern.compile("^[a-z_\\-]+/([a-z_\\-]+)", Pattern.CASE_INSENSITIVE);

    /**
     * Parses a mimetype into a {@link ApiOutputFormat}. Only application/json and application/xml are supported.
     * @param mimeType mimetype string to be parsed
     * @return the value corresponding to the mimetype
     * @throws ApiOutputFormatException if the mimetype is not supported
     */
    public static ApiOutputFormat parse(String mimeType) {
        Matcher matcher = MTYPE_PATTERN.matcher(mimeType);
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                String type = matcher.group(1).toUpperCase();
                try {
                    return valueOf(type);
                } catch (IllegalArgumentException ex) {
                    throw new ApiOutputFormatException(String.format("'%s' is not a supported output format", mimeType), ex);
                }
            }
        }
        throw new ApiOutputFormatException(String.format("'%s' is not a supported output format", mimeType));
    }
}
