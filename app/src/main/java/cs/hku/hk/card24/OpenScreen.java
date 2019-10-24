package cs.hku.hk.card24;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OpenScreen extends AppCompatActivity {
    public static final String PASSED_VALUE = "cs.hku.hk.card24.PASSED_VALUE";
    private Button button;
    private EditText enteredNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        enteredNumber = (EditText) findViewById(R.id.enteredNumber_id);
        button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enteredNumber.length() == 0 || enteredNumber.length() > 5 ||
                    Integer.parseInt(enteredNumber.getText().toString()) == 0) {
                    Toast.makeText(OpenScreen.this,
                   "Please provide a different input", Toast.LENGTH_SHORT).show();
                }else {
                    openMainActivity();}
                }
        });
    }

    public void openMainActivity(){
        int entNum = Integer.parseInt(enteredNumber.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PASSED_VALUE, entNum);
        startActivity(intent);
    }
}
