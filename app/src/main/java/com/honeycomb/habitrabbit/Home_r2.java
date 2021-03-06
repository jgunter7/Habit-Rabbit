package com.honeycomb.habitrabbit;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Home_r2 extends AppCompatActivity {
    public MyApp appData;
    public ListView lsvHabits;
    public List<c_Habit> habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_r2);

        appData = (MyApp)this.getApplicationContext();

        habits = new ArrayList<>();

        showList();
    }

    private void showList() {
        habits = appData._mController.dbsrv.GetAllHabits();

        lsvHabits = (ListView)findViewById(R.id.lsvHabits); // grab listview object from view
        lsvHabits.setAdapter(new CustomListAdapter(this, habits));
        registerForContextMenu(lsvHabits);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lsvHabits) {
            getMenuInflater().inflate(R.menu.delete_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        int index;
        switch (item.getItemId()) {
            case R.id.View_Context:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                index = info.position;
                ViewDetailsForHabit(index);
                return true;
            case R.id.Delete_Context:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                index = info.position;
                DeleteHabitFromStorage(habits.get(index));
            case R.id.Edit_Context:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                index = info.position;
                EditDetailsForHabit(index);
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Phasing Out the Context Menu here - jgunter

    private void DeleteHabitFromStorage(c_Habit h) {
        final c_Habit habitToDelete = h;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete this habit?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                appData._mController.dbsrv.DeleteHabit(habitToDelete);
                showList();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ViewDetailsForHabit(int index) {
        appData.currentHabit = habits.get(index);
        Intent intent = new Intent(getApplicationContext(), Details_r2.class);
        startActivity(intent);
    }

    public void EditDetailsForHabit(int index) {
        appData.currentHabit = habits.get(index);
        Intent intent = new Intent(getApplicationContext(), EditHabit_r2.class);
        startActivity(intent);
    }

    public void AddNewHabit(View v) {
        Intent intent = new Intent(getApplicationContext(), Add_r2.class);
        startActivity(intent);
    }

    public void ShowSettings(View v) {
        Intent intent = new Intent(getApplicationContext(), Settings_r2.class);
        startActivity(intent);
    }

}
