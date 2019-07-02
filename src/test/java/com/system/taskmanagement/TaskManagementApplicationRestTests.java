package com.system.taskmanagement;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.system.taskmanagement.domain.model.Task;
import com.system.taskmanagement.domain.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TaskManagementApplication.class) // Check if the name of this class is correct or not
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TaskManagementApplicationRestTests {
    @Autowired
    TaskRepository repo;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void addTask() throws Exception {
        Task task = new Task();
        task.setName("testName");
        task.setDescription("testDescription");
        mockMvc.perform(post("/api/tasks").content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void modifyTask() throws Exception {
        Task task = new Task();
        task.setName("testName");
        task.setDescription("testDescription");
        repo.save(task);

        Task modifiedTask = new Task();
        modifiedTask.setName("modifiedName");
        modifiedTask.setDescription("modifiedDescription");
        mockMvc.perform(put("/api/tasks/1").content(mapper.writeValueAsString(modifiedTask)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


        task = repo.findById(1l).orElse(null);
        assertThat(task.getName(), equalTo(modifiedTask.getName()));
    }
    @Test
    public void completeTask() throws Exception {
        Task task = new Task();
        task.setName("testName");
        task.setDescription("testDescription");
        repo.save(task);

        mockMvc.perform(patch("/api/tasks/1").content(mapper.writeValueAsString(null)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        task = repo.findById(1l).orElse(null);
        assertThat(task.getStatus().toString(), equalTo("COMPLETED"));
    }

    @Test
    public void deleteTask() throws Exception {
        Task task = new Task();
        task.setName("testName");
        task.setDescription("testDescription");
        repo.save(task);

        mockMvc.perform(delete("/api/tasks/1").content(mapper.writeValueAsString(null)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        task = repo.findById(1l).orElse(null);
        assertThat(task.getStatus().toString(), equalTo("DELETED"));
    }


}
