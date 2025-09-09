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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author spatialerror3
 */
public class JIBPluginCore {

    private static final Logger log = LogManager.getLogger(JIBPluginCore.class);
    private ArrayList<JIBPlugin> plugins = null;

    public JIBPluginCore() {
        this.plugins = new ArrayList<JIBPlugin>();
    }

    public JIBPlugin load(String jarPath, String className) throws Exception {
        File jarFile = new File(jarPath);
        URL jarUrl = jarFile.toURI().toURL();
        JIBPlugin p = null;

        // Use the custom IsolatedClassLoader
        try (JIBClassLoader loader = new JIBClassLoader(jarUrl)) {
            Class<?> clazz = Class.forName(className, true, loader);
            p = (JIBPlugin) clazz.getDeclaredConstructor().newInstance();
            p.initialize();
            registerPlugin(p);
            return p;
        }
    }

    public void registerPlugin(JIBPlugin p) {
        List<JIBPlugin> _plugins = Collections.synchronizedList(plugins);
        synchronized (_plugins) {
            plugins.add(p);
        }
    }
}
