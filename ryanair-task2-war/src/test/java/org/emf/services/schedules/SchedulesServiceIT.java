package org.emf.services.schedules;

import org.emf.model.schedules.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

/**
 * RyanAir Schedules API integration test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulesServiceIT {

    @Autowired
    private SchedulesService schedulesService;

    @Test
    public void getSchedules() throws Exception{

        Schedule schedule = this.schedulesService.getSchedule("DUB", "WRO", 2018, 6);
        assertNotNull(schedule);
    }
}
