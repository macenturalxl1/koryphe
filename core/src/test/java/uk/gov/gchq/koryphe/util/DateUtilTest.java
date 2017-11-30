/*
 * Copyright 2016 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.koryphe.util;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DateUtilTest {
    @Test
    public void shouldParseDates() throws IOException, ParseException {
        // When / Then
        assertDate("2017", "2017", "yyyy");
        assertDate("2017-01", "2017-01", "yyyy-MM");
        assertDate("2017-01", "2017 01", "yyyy-MM");
        assertDate("2017-01", "2017/01", "yyyy-MM");
        assertDate("2017-01", "2017.01", "yyyy-MM");
        assertDate("2017-01-02", "2017-01-02", "yyyy-MM-dd");
        assertDate("2017-01-02", "2017/01.02", "yyyy-MM-dd");
        assertDate("2017-01-02", "2017-01 02", "yyyy-MM-dd");
        assertDate("2017-01-02 01", "2017-01-02 01", "yyyy-MM-dd HH");
        assertDate("2017-01-02 01", "2017.01.02/01", "yyyy-MM-dd HH");
        assertDate("2017-01-02 01:02", "2017.01.02 01:02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02", "2017.01.02 01 02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02", "2017/01/02 01 02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02:30", "2017/01/02 01:02:30", "yyyy-MM-dd HH:mm:ss");
        assertDate("2017-01-02 01:02:30", "2017-01-02-01:02:30", "yyyy-MM-dd HH:mm:ss");
        assertDate("2017-01-02 01:02:30", "20170102010230", "yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void shouldNotParseInvalidDate() throws IOException, ParseException {
        // When / Then
        try {
            DateUtil.parse("20171");
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("The provided date string 20171 could not be parsed"));
        }
    }

    private void assertDate(final String expected, final String testDate, final String format) throws ParseException {
        assertEquals(DateUtils.parseDate(expected, Locale.getDefault(), format), DateUtil.parse(testDate));
    }
}
