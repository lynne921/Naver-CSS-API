package com.example.myapplication;

import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private NaverTTSTask mNaverTTSTask;
    String[] mTextString;
    EditText etText;
    Button btTTS, btReset;

    //activity detail로 화면 보여주기
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MP3를 저장하기 때문에 저장공간 Permission을 줘야함.
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(DetailActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(DetailActivity.this, "권한이 거부되었습니다. 권한거부시 앱기능 일부분을 사용하실수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(DetailActivity.this)
                .setPermissionListener(permissionlistener)
                .setRationaleConfirmText("허용 부탁")
                .setDeniedMessage("권한 허용 거부 시 앱 사용이 불가합니다.\n\n설정 -> 권한에서 권한 설정을 허용해주십시오")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissions(Manifest.permission.INTERNET)
                .setPermissions(Manifest.permission.ACCESS_NETWORK_STATE)
                .check();

        //버튼 캐스팅하려는 뷰의 이름을 적지 않아도 된다.
        btTTS = findViewById(R.id.bt_tts);
        btReset = findViewById(R.id.bt_reset);
        etText = (EditText) findViewById(R.id.et_text);

        //버튼 클릭이벤트 - 클릭하면 저장된 글자를 네이버에 보낸다. ->api에 보낸다는 것인가?
        btTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mText;
                if (etText.getText().length() > 0) { //한글자 이상 1
                    mText = etText.getText().toString();
                    mTextString = new String[]{mText};

                    //AsyncTask 실행
                    mNaverTTSTask = new NaverTTSTask();
                    mNaverTTSTask.execute(mTextString);
                } else {
                    Toast.makeText(DetailActivity.this, "텍스트를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //리셋버튼
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etText.setText("");
                etText.setHint("텍스트를 입력하세요.");
            }
        });

    }
    class NaverTTSTask extends AsyncTask<String[], Void, String> {
        @Override
        protected String doInBackground(String[]... strings) {
            //여기서 서버에 요청
            APITTS.main(mTextString);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }
}
