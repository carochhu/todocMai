package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.ClipData;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.database.dao.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {
    private TaskDao taskDao;
    private ProjectDao projectDao;

    // FOR DATA
    private TodocDatabase database;
    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Philippe", 11221149);


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        Context context = androidx.test.core.app.ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, TodocDatabase.class).build();
        taskDao = database.taskDao();
        projectDao = database.projectDao();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        //Adding a new task
        //Project project1 = new Project(4L,"Projet 1", 0xFFA3CED2);
        //projectDao.createProject(project1);
        Task task1 = new Task(1L, "task 1", new Date().getTime());
        Task task2 = new Task(2L, "task 2", new Date().getTime());
        taskDao.insertTask(task1);
        taskDao.insertTask(task2);
        // TEST
        List<Task> taskList = (List<Task>) LiveDataTestUtil.getValue((LiveData)database.taskDao().getTasks());
        assertEquals(2, taskList.size());
        assertEquals(1L, taskList.get(0).getProjectId());
        assertEquals(task1.getName(), taskList.get(0).getName());
        assertEquals(2L, taskList.get(1).getProjectId());
        assertEquals(task2.getName(), taskList.get(1).getName());
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        //Adding a new task
        Project project1 = new Project(4L,"Projet 1", 0xFFA3CED2);
        projectDao.createProject(project1);
        Task taskToDelete = new Task(1, "task 1", new Date().getTime());
        long taskId = taskDao.insertTask(taskToDelete);
        //TEST
        List<Task> tasks = taskDao.getTasks();
        taskDao.deleteTask(tasks.get((int)taskId));
        List<Task> taskList = (List<Task>) LiveDataTestUtil.getValue((LiveData)database.taskDao().getTasks());
        assertTrue(taskList.isEmpty());
    }


    @Test
    public void insertAndGetProject() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.projectDao().createProject(PROJECT_DEMO);

        // TEST
        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProject(PROJECT_ID));
        assertEquals(project.getId(),(PROJECT_DEMO.getId()));
        assertEquals(project.getId(), PROJECT_ID);
    }






}