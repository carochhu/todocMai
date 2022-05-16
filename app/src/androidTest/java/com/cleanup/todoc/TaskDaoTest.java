package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
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
    private static final long PROJECT_ID = 1;
    private static final Project PROJECT_DEMO = new Project(PROJECT_ID, "Philippe", 11221149);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
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
        Project project1 = new Project(4L,"Projet 1", 0xFFA3CED2);
        projectDao.createProject(project1);
        Task task1 = new Task(4L, "task 1", new Date().getTime());
        Task task2 = new Task(4L, "task 2", new Date().getTime());
        taskDao.insertTask(task1);
        taskDao.insertTask(task2);
        List<Task> tasks2 = taskDao.getTasks();
        // TEST
        assertEquals(2, tasks2.size());
        assertEquals(4L, tasks2.get(0).getProjectId());
        assertEquals(task1.getName(), tasks2.get(0).getName());
        assertEquals(4L, tasks2.get(1).getProjectId());
        assertEquals(task2.getName(), tasks2.get(1).getName());
    }

    @Test
    public void insertAndDeleteTask() throws InterruptedException {
        //Adding a new task
        Project project1 = new Project(4L,"Projet 1", 0xFFA3CED2);
        projectDao.createProject(project1);
        Task taskToDelete = new Task(4L, "task 10", new Date().getTime());
        taskDao.insertTask(taskToDelete);
        List<Task> tasks1 = taskDao.getTasks();
        long taskId = taskToDelete.getId();
        //TEST
        taskDao.deleteTask(tasks1.get((int)taskId));
        List<Task> tasks2 = taskDao.getTasks();
        assertEquals(0,tasks2.size());
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