package jp.photorica.bingo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import jp.photorica.bingo.fragment.BingoFragment;

public class BingoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingo);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BingoFragment())
                    .commit();
        }
    }
}
