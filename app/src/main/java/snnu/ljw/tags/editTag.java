package snnu.ljw.tags;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import snnu.ljw.tags.dao.ApiUse;
import snnu.ljw.tags.dao.TagDAO;

public class editTag extends AppCompatActivity {

    TagDAO tagDAO = new TagDAO(this);

    EditText name,describ;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        name = findViewById(R.id.tag_name);
        describ = findViewById(R.id.tag_describe);
        add = findViewById(R.id.add);

        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tag tag = new Tag(name.getText().toString(),describ.getText().toString());
                        System.out.println(tag.toString());
                        addTag(tag);
                    }
                }
        );
    }

    private void addTag(Tag tag){
        tagDAO.addAnTag(tag, new ApiUse<Tag>() {
            @Override
            public void onSuccess(Tag result) {
                Toast.makeText(editTag.this,"添加tag成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(editTag.this,MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFail(Exception e) {
                super.onFail(e);
                Toast.makeText(editTag.this,"添加失败，tag已经存在或者tag标题为空，请修改tag",Toast.LENGTH_LONG).show();
            }
        });
    }
}