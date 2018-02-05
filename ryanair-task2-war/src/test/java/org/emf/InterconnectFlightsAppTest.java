package org.emf;

import org.emf.services.routes.RoutesService;
import org.emf.services.schedules.SchedulesService;
import org.emf.testutils.RoutesMock;
import org.emf.testutils.ScheduleMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InterconnectFlightsAppTest {

    private final String TEST_REQUEST =
            "/interconnections?" +
                    "departure=DUB" +
                    "&arrival=WRO" +
                    "&departureDateTime=2018-03-01T07:00:00" +
                    "&arrivalDateTime=2018-03-01T22:00:00";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutesService routesService;

    @MockBean
    private SchedulesService schedulesService;

    @Before
    public void setUp(){

        Mockito.when(routesService.getRoutes()).thenAnswer(new RoutesMock());
        Mockito.when(schedulesService.getSchedule(anyString(), anyString(), anyInt(), anyInt())).thenAnswer(new ScheduleMock());
    }

    @Test
    public void getInterconnections() throws Exception{

        this.mockMvc.perform(get(TEST_REQUEST)).andExpect(status().isOk());

        Map<String, Object> params = new HashMap<>();
        params.put("departure", "DUB");
        params.put("arrival", "WRO");
        params.put("departureDateTime", "2018-03-01T:07:00:00");
        params.put("arrivalDateTime", "2018-03-01T:22:00:00");

    }
}