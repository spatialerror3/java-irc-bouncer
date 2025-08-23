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
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.JobKey.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.DateBuilder.*;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.impl.matchers.KeyMatcher.*;
import static org.quartz.impl.matchers.GroupMatcher.*;
import static org.quartz.impl.matchers.AndMatcher.*;
import static org.quartz.impl.matchers.OrMatcher.*;
import static org.quartz.impl.matchers.EverythingMatcher.*;

/**
 *
 * @author spatialerror3
 */
public class JIBQuartz {

    private static final Logger log = LogManager.getLogger(JIBQuartz.class);
    private SchedulerFactory sf = null;
    private Scheduler scheduler = null;
    
    public JIBQuartz() {
        sf = new StdSchedulerFactory();
        try {
            scheduler = sf.getScheduler();
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
    }
    
    public void init() {
        JobDetail job1 = newJob(JIBQuartzJob.class).withIdentity("job1", "group1").build();
        
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(120).repeatForever()).build();
        
        try {
            scheduler.scheduleJob(job1, trigger);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
        
        try {
            scheduler.start();
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
    }
    
    public void shutdown() {
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
    }
}
