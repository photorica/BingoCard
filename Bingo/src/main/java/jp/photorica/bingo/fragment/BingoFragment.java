package jp.photorica.bingo.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

import jp.photorica.bingo.R;
import jp.photorica.bingo.adapter.BingoAdapter;

public class BingoFragment extends Fragment implements OnItemClickListener {

    private static final int MAX_COUNT = 25;
    public static final int CENTER_NUMBER = 12;
    private GridView mGridView;
    private final ArrayList<Integer> mIndexList = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bingo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * Viewの初期化をする.
     */
    private void initView() {
        mGridView = (GridView) getView().findViewById(android.R.id.list);
        mGridView.setOnItemClickListener(this);
        refreshData();
    }

    /**
     * データをリフレッシュする.
     */
    private void refreshData() {
        mIndexList.clear();
        mIndexList.add(CENTER_NUMBER);

        ArrayList<String> list = createNumbers();
        BingoAdapter adapter = new BingoAdapter(
                getActivity().getApplicationContext(), 0, list);
        mGridView.setAdapter(null);
        mGridView.setEnabled(true);
        mGridView.setAdapter(adapter);
    }

    /**
     * 番号の生成.
     * @return Number List
     */
    private ArrayList<String> createNumbers() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < MAX_COUNT; i++) {
            int minValue;
            int maxValue;

            if (CENTER_NUMBER == i) {
                list.add(getString(R.string.label_center));
                continue;
            }

            switch ((i + 1) % 5) {
                case 1:
                    minValue = 1;
                    maxValue = 15;
                    break;
                case 2:
                    minValue = 16;
                    maxValue = 30;
                    break;
                case 3:
                    minValue = 31;
                    maxValue = 45;
                    break;
                case 4:
                    minValue = 46;
                    maxValue = 60;
                    break;
                case 0:
                default:
                    minValue = 61;
                    maxValue = 75;
                    break;
            }
            while (true) {
                int value =  ((int) Math.floor(Math.random() * (maxValue - minValue + 1))) + minValue;
                if (!list.contains(Integer.toString(value))) {
                    list.add(Integer.toString(value));
                    break;
                }
            }
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!view.isEnabled()) {
            return;
        }

        view.setBackgroundColor(Color.argb(255, 85, 85, 85));
        view.setEnabled(false);
        mIndexList.add(position);

        boolean isBingo = isBingo();
        // ビンゴかどうかの判定
        if (isBingo) {
            showBingoDialog();
        }
    }

    /**
     * ビンゴかどうか判定
     * @return isBingo
     */
    private boolean isBingo() {
        // 規定の数に達していない場合はfalse
        if (mIndexList.size() < 4) {
            return false;
        }

        // 縦のチェック
        for (int i = 0; i < MAX_COUNT; i++) {
            if (mIndexList.contains(i)
                    && mIndexList.contains(i + 5) && mIndexList.contains(i + 10)
                    && mIndexList.contains(i + 15) && mIndexList.contains(i + 20)) {
                return true;
            }
        }
        // 横のチェック
        for (int i = 0; i < MAX_COUNT; i = i + 5) {
            if (mIndexList.contains(i)
                    && mIndexList.contains(i + 1) && mIndexList.contains(i + 2)
                    && mIndexList.contains(i + 3) && mIndexList.contains(i + 4)) {
                return true;
            }
        }
        // 斜めのチェック
        if (mIndexList.contains(CENTER_NUMBER)) {
            if (mIndexList.contains(0) && mIndexList.contains(6)
                    && mIndexList.contains(18) && mIndexList.contains(24)) {
                return true;
            }
            if (mIndexList.contains(4) && mIndexList.contains(8)
                    && mIndexList.contains(16) && mIndexList.contains(20)) {
                return true;
            }
        }

        return false;
    }

    /**
     * ビンゴダイアログを表示する.
     */
    private void showBingoDialog() {
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.title_bingo);
        builder.setMessage(R.string.msg_bingo);
        builder.setPositiveButton(R.string.btn_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGridView.setEnabled(false);
            }
        });
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bingo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restart:
                showRestartDailog();
                break;
            case R.id.action_undo:
                undoNumber();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 再スタートダイアログを表示する.
     */
    private void showRestartDailog() {
        Builder builder = new Builder(getActivity());
        builder.setTitle(R.string.title_warning);
        builder.setMessage(R.string.msg_restart);
        builder.setPositiveButton(R.string.btn_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshData();
            }
        });
        builder.setNegativeButton(R.string.btn_no, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /**
     * アンドゥを行う.
     */
    private void undoNumber() {
        if (mIndexList.size() < 2) {
            return;
        }

        int targetIndex = mIndexList.get(mIndexList.size() - 1);
        View targetItem = mGridView.getChildAt(targetIndex);
        if (targetItem != null) {
            targetItem.setBackgroundResource(R.drawable.bg_frame_border);
            targetItem.setEnabled(true);
            mIndexList.remove(mIndexList.size() - 1);

            mGridView.setEnabled(true);
        }
    }
}