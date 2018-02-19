package com.gustavohidalgo.quaiscalingudum.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.interfaces.OnEditNotificationListener;
import com.gustavohidalgo.quaiscalingudum.models.Notification;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEditNotificationListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener {
    private static final String NOTIFICATION = "notification";
    private static final String STOP_TIMES = "stop_times";
    private static final String FREQUENCIES = "frequencies";

    private Notification mNotification;
    private String[] mLine;
    private ArrayList<String> mStops, mFrequencies;
    private ArrayList<String> mStopsFiltered = new ArrayList<>();
    private ArrayList<String> mFrequenciesFiltered = new ArrayList<>();
    private DateTime mDateTime;

    @BindView(R.id.line_code_tv)
    TextView mLineCodeTV;
    @BindView(R.id.line_name_tv)
    TextView mLineNameTV;
//    @BindView(R.id.destination_rg)
//    RadioGroup mDestinationRG;
//    @BindView(R.id.destination_before_rb)
//    RadioButton mDestinationBeforeRB;
//    @BindView(R.id.destination_after_rb)
//    RadioButton mDestinationAfterRB;
    @BindView(R.id.departure_stop_sp)
    Spinner mDepartureStopSP;
    @BindView(R.id.departure_time_rg)
    RadioGroup mDepartureTimeRG;
    @BindView(R.id.departure_time_before_rb)
    RadioButton mDepartureTimeBeforeRB;
    @BindView(R.id.departure_time_after_rb)
    RadioButton mDepartureTimeAfterRB;

    private OnEditNotificationListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notification Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    public static DetailsFragment newInstance(Notification notification, ArrayList<String> stops,
                                              ArrayList<String> frequencies) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTIFICATION, notification);
        args.putStringArrayList(STOP_TIMES, stops);
        args.putStringArrayList(FREQUENCIES, frequencies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNotification = getArguments().getParcelable(NOTIFICATION);
            mStops = getArguments().getStringArrayList(STOP_TIMES);
            mStops.remove(0);
            mFrequencies = getArguments().getStringArrayList(FREQUENCIES);
            mFrequencies.remove(0);
            mLine = mNotification.getLine();
            listFilter(mLine[2].replaceAll("\"", ""), 0, mStops, mStopsFiltered);
            listFilter(mLine[2].replaceAll("\"", ""), 0, mFrequencies, mFrequenciesFiltered);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        mLineCodeTV.setText(mLine[0].replaceAll("\"", ""));
        mLineNameTV.setText(mLine[3].replaceAll("\"", ""));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, mStopsFiltered);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mDepartureStopSP.setOnItemSelectedListener(this);
        mDepartureStopSP.setAdapter(adapter);
        return view;
    }

    @OnClick(R.id.next_notifications_bt)
    public void onNextPressed() {
        if (mListener != null) {
            mNotification.setDateTime(mDateTime);
            mListener.toSetNotifications(mNotification);
        }
    }

    @OnClick(R.id.back_pickline_bt)
    public void onBackPressed() {
        if (mListener != null) {
            mListener.toPickLine(mNotification);
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

    public static void listFilter(String filter, int indexColumn, ArrayList<String> in,
                                  ArrayList<String> out) {
        out.clear();
        if(filter.isEmpty()){
            out.addAll(in);
        } else {
            filter = filter.toLowerCase();
            for(String item : in){
                String[] stop = item.split(",");
                if(stop[indexColumn].toLowerCase().replaceAll("\"", "").equals(filter)){
                    out.add(item);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mNotification.setStop(mStopsFiltered.get(position).split(","));

        // time get by the bus from start to the choosen stop
        int[] refSaidaTerminalTabela = stringToTime(mStopsFiltered.get(0).split(","));
        DateTime refSaidaTerminal = new DateTime(0, 1, 1, 0,
                refSaidaTerminalTabela[1], 0);

        int[] refChegadaPontoTabela = stringToTime(mNotification.getStop());
        DateTime refChegadaPonto = new DateTime(0, 1, 1,
                (refChegadaPontoTabela[0] - refSaidaTerminalTabela[0]), refChegadaPontoTabela[1],
                0);

        DateTime tempoViagem = new DateTime(refChegadaPonto.getMillis() - refSaidaTerminal.getMillis());

        // get the start time considering the bus arriving at the choosen point in the choosen time
        DateTime horaChegadaDesejada = new DateTime(0,1,1,
                mNotification.getDateTime().getHourOfDay(),
                mNotification.getDateTime().getMinuteOfHour(), 0);
        DateTime horaSaidaDesejada = new DateTime(horaChegadaDesejada.getMillis() - tempoViagem.getMillis());

        // get the headway_sec atr the time of the bus departuring
        int intervalo = 0;
        for (String freq : mFrequencies) {
            int hour = Integer.parseInt(freq.split(",")[1]
                    .replaceAll("\"", "").split(":")[0]);
            if (hour == horaSaidaDesejada.getHourOfDay()){
                intervalo = 1000 * Integer.parseInt(freq.split(",")[3]
                        .replaceAll("\"", ""));
                break;
            }
        }

        while((horaSaidaDesejada.getMillis() < horaChegadaDesejada.getMillis())){
            horaSaidaDesejada = new DateTime(horaSaidaDesejada.getMillis() + intervalo);
        }

        DateTime horaSaidaAnterior = new DateTime(horaSaidaDesejada.getMillis() - intervalo);
        DateTime horaSaidaPosterior = new DateTime(horaSaidaDesejada.getMillis());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
        mDepartureTimeBeforeRB.setText(horaSaidaAnterior.toString(fmt));
        mDepartureTimeAfterRB.setText(horaSaidaPosterior.toString(fmt));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int[] stringToTime(String[] strings){
        int hour = Integer.parseInt(strings[1].replaceAll("\"", "").split(":")[0]);
        int minute = Integer.parseInt(strings[1].replaceAll("\"", "").split(":")[1]);
        return new int[]{hour, minute};
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton button = group.findViewById(checkedId);
        String[] choosenTime = button.getText().toString().split(":");
        mDateTime = new DateTime(mNotification.getDateTime().getYear(),
                mNotification.getDateTime().getMonthOfYear(),
                mNotification.getDateTime().getDayOfMonth(),
                Integer.parseInt(choosenTime[0]), Integer.parseInt(choosenTime[0]),0);
    }
}
