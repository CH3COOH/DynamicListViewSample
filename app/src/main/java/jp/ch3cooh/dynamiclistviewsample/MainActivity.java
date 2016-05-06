package jp.ch3cooh.dynamiclistviewsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements OnItemMovedListener, OnDismissCallback {

    @InjectView(R.id.listView)
    DynamicListView listView;

    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        /* Setup the adapter */
        listAdapter = new ListAdapter(this);
        SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(listAdapter, this, this);
        AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
        animAdapter.setAbsListView(listView);
        assert animAdapter.getViewAnimator() != null;
        animAdapter.getViewAnimator().setInitialDelayMillis(300);
        listView.setAdapter(animAdapter);

        /* Enable drag and drop functionality */
        listView.enableDragAndDrop();
        listView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        listView.setOnItemMovedListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView != null) {
                    listView.startDragging(position);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDismiss(@NonNull ViewGroup listView, @NonNull int[] reverseSortedPositions) {

    }

    @Override
    public void onItemMoved(int originalPosition, int newPosition) {
        // リストの並び替えをおこなう
    }


    private class ListAdapter extends ArrayAdapter<String> implements UndoAdapter {

        private final Context mContext;

        ListAdapter(final Context context) {
            mContext = context;

            for (int i = 0; i < 20; i++) {
                add(String.format(Locale.US, "item: %d", i));
            }
        }

        @Override
        public long getItemId(final int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            SampleCell cell = null;

            if (convertView instanceof SampleCell) {
                cell = (SampleCell) convertView;
            } else {
                cell = (SampleCell) getLayoutInflater().inflate(R.layout.cell_sample, null);
            }

            final String title = getItem(position);
            cell.titleView.setText(title);

            return cell;
        }

        @NonNull
        @Override
        public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.cell_sample, parent, false);
            }
            return view;
        }

        @NonNull
        @Override
        public View getUndoClickView(@NonNull final View view) {
            return view.findViewById(R.id.titleView);
        }
    }
}
