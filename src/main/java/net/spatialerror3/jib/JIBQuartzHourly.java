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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author spatialerror3
 */
public class JIBQuartzHourly implements Job {

    private static final Logger log = LogManager.getLogger(JIBQuartzHourly.class);

    public JIBQuartzHourly() {

    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JavaIrcBouncer.jibDbUtil.clearLogOlderThan2Days();
        JIBEvent<String, Object> event = new JIBEvent<String, Object>();
        event.put("TYPE", "HOURLY");
        JavaIrcBouncer.jibEventCore.handleEvent(event);
    }
}
