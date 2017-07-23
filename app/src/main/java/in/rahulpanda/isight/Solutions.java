package in.rahulpanda.isight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Solutions extends AppCompatActivity {

    String title[]={"Digital Safety", "First Time Buyer Mortgages", "Accessible Banking", "Barclays Direct Investing", "Barclays Minute That Matters", "Helping your business", "Barclays Short Stories", "Barclays Mobile Banking app", "Students", "Barclays Products and Services", "Barclays Quick List", "Barclays How To's", "Barclays Jargon Busters", "LifeSkills", "Better Off Online", "Everyday digital banking", "Becoming Your Bank", "Going Digital", "Building Tomorrows Bank Together", "Products & Services that work for you", "Comic Relief", "Helping with the big decisions"};
    String pids[]={"PLecqH2uhOR0ZK5WFrGy30v86MzmvDvC9p", "PLecqH2uhOR0bYyl8wLuG9KTySrekP5hbj", "PLecqH2uhOR0Zb31X7hh5BzWJv4KGLnuUy", "PLecqH2uhOR0ZEO-qXwQc_Es0WNBeLPxcC", "PLecqH2uhOR0bwXRsHQGKK2F-xsk3TDchg", "PLecqH2uhOR0ZH42PclO4ADBrUdIj8N7yb", "PLecqH2uhOR0buvzThU3-walpIl3tUiwoe", "PLecqH2uhOR0YOgBz7WCyPMbA9KddaN2ch", "PLecqH2uhOR0aiIgcLmkbrwy0dGiYxVn1O", "PLecqH2uhOR0bb3DEodWhF2g0bNNdwkMwL", "PLecqH2uhOR0bLxXPCJ-3E4C0OxPm7H1jR", "PLecqH2uhOR0ZvnUZ0ceWTPzLV6Wy20yNL", "PLecqH2uhOR0bYSkeoft9iW_KdKwjyWEZd", "PLecqH2uhOR0ZS6NsFbVd7hKZx8ugj5gLj", "PLecqH2uhOR0Z41bHxSWr50WCUFSMaoYVG", "PLecqH2uhOR0Ypbh5FEnvGMtYAfEB7wLp6", "PLecqH2uhOR0YSYxwEzUeGKKhGAWujAKzj", "PLecqH2uhOR0aQ3tEB7pzq3WjLWtapF-Kb", "PLecqH2uhOR0Z7W8mNkoswfx0L68YrpYdW", "PLecqH2uhOR0Z1KchdnpheJ90JijaVvYm9", "PLecqH2uhOR0Yli58eVKEsXUp2T3ZWUmQa", "PLecqH2uhOR0bybetcDfxm3uAeYX65NVnY"};

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solutions);
        lv = (ListView)findViewById(R.id.listView);
        ArrayAdapter <String> ad = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,title);
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Solutions.this,YouTubeFrag.class);
                i.putExtra("pid",pids[position]);
                startActivity(i);
            }
        });
    }
}
