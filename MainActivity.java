package com.nalimkwon.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {

    String databaseName = "myDB";
    SQLiteDatabase database;  // database를 다루기 위한 SQLiteDatabase 객체 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = openOrCreateDatabase(databaseName,MODE_PRIVATE,null);    // 있으면 열고 없으면 DB를 생성
        ListView listView = (ListView) findViewById(R.id.listView);

        Button button = (Button) findViewById(R.id.button);   //insert
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name ="이름" ;
                int resId = 1;

                insertData(name, resId);
            }
        });


        //select
        listView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String tableName ="값" ;
                selectData(tableName);
            }
        });


        SingerAdapter adapter = new SingerAdapter();
        adapter.addItem(new SingerItem("댕댕이와 산책한 날", R.drawable.puppy));
        adapter.addItem(new SingerItem("댕댕이 생일",R.drawable.puppy));
        adapter.addItem(new SingerItem("심장사상충 맞는날",R.drawable.puppy));
        adapter.addItem(new SingerItem("오늘은 비가온다",R.drawable.puppy));
        adapter.addItem(new SingerItem("댕댕이는 잠꾸러기",R.drawable.puppy));
        listView.setAdapter(adapter);   // listView 객체에 Adapter를 붙인다


    }
    class SingerAdapter extends BaseAdapter {
        ArrayList<SingerItem> items = new ArrayList<SingerItem>();


        @Override
        public int getCount() {
            return items.size();    //리스트 뷰가 몇개 있는지
        }

        public void addItem(SingerItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {  //몇번째 데이터를 달라
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {  //Id값 넘겨달라
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingerItemView view = new SingerItemView(getApplicationContext());

            SingerItem item = items.get(position);
            view.setName(item.getName());
            view.setImage(item.getResId());
            return view;
        }
    }


    public void insertData(String name, int resId){

        if(database != null){
            String sql = "insert into customer(name, resId) values(?, ?)";
            Object[] params = { name, resId};

            database.execSQL(sql, params);

        }else{
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }


    public void selectData(String tableName){   // 항상 DB문을 쓸때는 예외처리(try-catch)를 해야한다
        println("selectData() 호출됨.");

        if(database !=null){
            String sql = "select name, resId from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            println("조회된 데이터 개수 : " + cursor.getCount());   // db에 저장된 행 개수를 읽어온다

            for(int i=0; i< cursor.getColumnCount();i++){
                cursor.moveToNext();    // 첫번째에서 다음 레코드가 없을때까지 읽음
                String name = cursor.getString(0);   // 첫번째 속성
                int resId = cursor.getInt(1); // 두번째 속성
                println("#" + i + " -> " + name + ", " + resId);  // 각각의 속성값들을 해당 배열의 i번째에 저장
            }

            cursor.close();
        }
    }

}
