/*
 * Copyright (C) 2025 spatialerror3
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.spatialerror3.jib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBObjectUtil {

    private static final Logger log = LogManager.getLogger(JIBObjectUtil.class);

    public JIBObjectUtil() {

    }

    public String objectToXml(Object x) {
        String r = null;
        ObjectMapper xmlMapper = new XmlMapper();

        try {
            r = xmlMapper.writeValueAsString(x);
        } catch (JsonProcessingException ex) {
            log.error((String) null, ex);
        }

        return r;
    }

    public Object xmlToObject(String xml, Class clazz) {
        Object r = null;
        ObjectMapper xmlMapper = new XmlMapper();

        try {
            r = (Object) xmlMapper.readValue(xml, clazz);
        } catch (JsonProcessingException ex) {
            log.error((String) null, ex);
        }

        return r;
    }
}
