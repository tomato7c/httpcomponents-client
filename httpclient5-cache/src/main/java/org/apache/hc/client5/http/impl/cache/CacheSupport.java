/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.hc.client5.http.impl.cache;

import java.net.URI;
import java.util.BitSet;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.hc.client5.http.utils.URIUtils;
import org.apache.hc.core5.annotation.Internal;
import org.apache.hc.core5.http.FormattedHeader;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.MessageHeaders;
import org.apache.hc.core5.http.message.ParserCursor;
import org.apache.hc.core5.util.Args;
import org.apache.hc.core5.util.CharArrayBuffer;
import org.apache.hc.core5.util.TextUtils;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Tokenizer;

/**
 * HTTP cache support utilities.
 *
 * @since 5.3
 */
@Internal
public final class CacheSupport {

    private static final BitSet COMMA = Tokenizer.INIT_BITSET(',');

    // This method should be provided by MessageSupport from core
    public static void parseTokens(final CharSequence src, final ParserCursor cursor, final Consumer<String> consumer) {
        Args.notNull(src, "Source");
        Args.notNull(cursor, "Cursor");
        while (!cursor.atEnd()) {
            final int pos = cursor.getPos();
            if (src.charAt(pos) == ',') {
                cursor.updatePos(pos + 1);
            }
            final String token = Tokenizer.INSTANCE.parseToken(src, cursor, COMMA);
            if (consumer != null) {
                consumer.accept(token);
            }
        }
    }

    // This method should be provided by MessageSupport from core
    public static void parseTokens(final Header header, final Consumer<String> consumer) {
        Args.notNull(header, "Header");
        if (header instanceof FormattedHeader) {
            final CharArrayBuffer buf = ((FormattedHeader) header).getBuffer();
            final ParserCursor cursor = new ParserCursor(0, buf.length());
            cursor.updatePos(((FormattedHeader) header).getValuePos());
            parseTokens(buf, cursor, consumer);
        } else {
            final String value = header.getValue();
            final ParserCursor cursor = new ParserCursor(0, value.length());
            parseTokens(value, cursor, consumer);
        }
    }

    public static URI getLocationURI(final URI requestUri, final MessageHeaders response, final String headerName) {
        final Header h = response.getFirstHeader(headerName);
        if (h == null) {
            return null;
        }
        final URI locationUri = CacheKeyGenerator.normalize(h.getValue());
        if (locationUri == null) {
            return requestUri;
        }
        if (locationUri.isAbsolute()) {
            return locationUri;
        } else {
            return URIUtils.resolve(requestUri, locationUri);
        }
    }

    public static boolean isSameOrigin(final URI requestURI, final URI targetURI) {
        return targetURI.isAbsolute() && Objects.equals(requestURI.getAuthority(), targetURI.getAuthority());
    }

    public static final TimeValue MAX_AGE = TimeValue.ofSeconds(Integer.MAX_VALUE + 1L);

    public static long deltaSeconds(final String s) {
        if (TextUtils.isEmpty(s)) {
            return -1;
        }
        try {
            long ageValue = Long.parseLong(s);
            if (ageValue < 0) {
                ageValue = -1;  // Handle negative age values as invalid
            } else if (ageValue > Integer.MAX_VALUE) {
                ageValue = MAX_AGE.toSeconds();
            }
            return ageValue;
        } catch (final NumberFormatException ignore) {
        }
        return 0;
    }

}
