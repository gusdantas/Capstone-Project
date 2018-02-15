package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.adapters.SearchLineAdapter;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnChooseLineListener;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link PickLineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickLineFragment extends Fragment implements SearchView.OnQueryTextListener,
        OnChooseLineListener {
    private static final String NOTIFICATION = "notification";
    private static final String LINES = "lines";

    SearchView mSearchView;
    @BindView(R.id.lines_rv)
    RecyclerView mLinesRV;
    @BindView(R.id.line_code_selected_tv)
    TextView mLineCodeSelectedTV;
    @BindView(R.id.line_name_selected_tv)
    TextView mLineNameSelectedTV;

    private Notification mNotification;
    private ArrayList<String> mLines;

    private OnEditNotificationListener mListener;

    public PickLineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notification Parameter 1.
     * @return A new instance of fragment PickLineFragment.
     */
    public static PickLineFragment newInstance(Notification notification, ArrayList<String> lines) {
        PickLineFragment fragment = new PickLineFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        args.putStringArrayList(LINES, lines);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mLines = getArguments().getStringArrayList(LINES);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_line, container, false);
        ButterKnife.bind(this, view);
        mSearchView = view.findViewById(R.id.searchView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLinesRV.setLayoutManager(layoutManager);
        SearchLineAdapter searchLineAdapter = new SearchLineAdapter();
        searchLineAdapter.setChooseLineListener(this);
        searchLineAdapter.setLines(mLines);
        mLinesRV.setAdapter(searchLineAdapter);
        return view;
    }

    @OnClick(R.id.next_detail_bt)
    public void onNextPressed() {
        if (mListener != null && mNotification.getLine() != null) {
            mListener.toDetails(mNotification);
        }
    }

    @OnClick(R.id.back_eta_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toEta(mNotification);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditNotificationListener) {
            mListener = (OnEditNotificationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void lineChosen(String[] line) {
        mLineCodeSelectedTV.setText(line[0]);
        mLineNameSelectedTV.setText(line[3]);
        mNotification.setLine(line);
    }
}
