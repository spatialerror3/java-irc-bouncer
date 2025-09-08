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
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
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

        JobDetail job2 = newJob(JIBQuartzHourly.class).withIdentity("job2", "group2").build();

        Trigger trigger2 = newTrigger().withIdentity("trigger2", "group2").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(3600).repeatForever()).build();

        try {
            scheduler.scheduleJob(job2, trigger2);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }

        JobDetail job3 = newJob(JIBQuartzTidy.class).withIdentity("job3", "group3").build();

        Trigger trigger3 = newTrigger().withIdentity("trigger3", "group3").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(300).repeatForever()).build();

        try {
            scheduler.scheduleJob(job3, trigger3);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }

        JobDetail job4 = newJob(JIBQuartzDecaminutely.class).withIdentity("job4", "group4").build();

        Trigger trigger4 = newTrigger().withIdentity("trigger4", "group4").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(600).repeatForever()).build();

        try {
            scheduler.scheduleJob(job4, trigger4);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }

        try {
            scheduler.start();
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
    }

    public void scheduleJob(JobDetail _job, Trigger _trigger) {
        try {
            scheduler.scheduleJob(_job, _trigger);
        } catch (SchedulerException ex) {
            log.error((String) null, ex);
        }
    }

    public void scheduleJobNoDuplicates(JobDetail _job, Trigger _trigger) {
        JobKey jk1 = _job.getKey();
        TriggerKey tk1 = _trigger.getKey();
        try {
            if (scheduler.checkExists(jk1)) {
                return;
            }
            if (scheduler.checkExists(tk1)) {
                return;
            }
            scheduler.scheduleJob(_job, _trigger);
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
