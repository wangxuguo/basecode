package basecode.com.basecode.activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import basecode.com.basecode.R;
import basecode.com.basecode.ui.view.LargeTouchableAreasView;

import static basecode.com.basecode.model.entity.Cheeses.CHEESES;

/**
 * 点击区域扩大
 */
public class LargeTouchableAreasListActivity extends ListActivity {

    private static final String STAR_STATES = "listviewtipsandtricks:star_states";
    private static final String SELECTION_STATES = "listviewtipsandtricks:selection_states";

    private boolean[] mStarStates;
    private boolean[] mSelectionStates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LargeTouchableAreasAdapter adapter = new LargeTouchableAreasAdapter();

        if (savedInstanceState != null) {
            mStarStates = savedInstanceState.getBooleanArray(STAR_STATES);
            mSelectionStates = savedInstanceState.getBooleanArray(SELECTION_STATES);
        } else {
            mStarStates = new boolean[adapter.getCount()];
            mSelectionStates = new boolean[adapter.getCount()];
        }

        setListAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(SELECTION_STATES, mSelectionStates);
        outState.putBooleanArray(STAR_STATES, mStarStates);
    }

    private class LargeTouchableAreasAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return CHEESES.length;
        }

        @Override
        public String getItem(int position) {
            return CHEESES[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final LargeTouchableAreasView view;

            if (convertView == null) {
                view = (LargeTouchableAreasView) getLayoutInflater().inflate(R.layout.large_touchable_area_item, parent, false);
                view.setOnLargeTouchableAreasListener(mOnLargeTouchableAreasListener);
            } else {
                view = (LargeTouchableAreasView) convertView;
            }

            view.setItemViewStarred(mStarStates[position]);
            view.setItemViewSelected(mSelectionStates[position]);
            view.getTextView().setText(getItem(position));

            return view;
        }
    }

    private LargeTouchableAreasView.OnLargeTouchableAreasListener mOnLargeTouchableAreasListener = new LargeTouchableAreasView.OnLargeTouchableAreasListener() {

        @Override
        public void onSelected(LargeTouchableAreasView view, boolean selected) {
            final int position = getListView().getPositionForView(view);
            if (position != ListView.INVALID_POSITION) {
                mSelectionStates[position] = selected;
            }
        }

        @Override
        public void onStarred(LargeTouchableAreasView view, boolean starred) {
            final int position = getListView().getPositionForView(view);
            if (position != ListView.INVALID_POSITION) {
                mStarStates[position] = starred;
            }
        }
    };
}