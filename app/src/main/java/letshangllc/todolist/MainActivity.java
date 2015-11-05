package letshangllc.todolist;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "TodoTaskPrefs";

    private ArrayList<Task> array_tasks;

    private Set<String> setOfItems;

    private TaskAdapter taskAdapter;

    Toolbar toolbar;

    /* Todo
    swipe to remove item
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        setOfItems = prefs.getStringSet("todoSet", new HashSet<String>());

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        this.fillArrayList();

        taskAdapter = new TaskAdapter(array_tasks, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(taskAdapter);


    }

    private void fillArrayList(){
        /* Todo:
            pull from db or use set in adapter
         */
        array_tasks = new ArrayList<>();
        Iterator<String> it = setOfItems.iterator();
        while(it.hasNext()){
            array_tasks.add(new Task(it.next()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                Dialog_AddTask dialog_addTask = new Dialog_AddTask();
                dialog_addTask.setCallback(new Dialog_AddTask.AddItemListener() {
                    @Override
                    public void onDialogPositiveClick(String newName) {
                        if(!newName.isEmpty()){
                            array_tasks.add(new Task(newName));
                            taskAdapter.notifyDataSetChanged();
                            setOfItems.add(newName);

                            saveTasks();
                        }
                    }
                });
                dialog_addTask.show(getSupportFragmentManager(), "dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveTasks(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("todoSet", setOfItems);
        editor.commit();
    }
}
