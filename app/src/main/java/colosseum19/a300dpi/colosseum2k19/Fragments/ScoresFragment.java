package colosseum19.a300dpi.colosseum2k19.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import colosseum19.a300dpi.colosseum2k19.Adapters.ScoreGameListAdapter;
import colosseum19.a300dpi.colosseum2k19.Interfaces.CallbackInterface;
import colosseum19.a300dpi.colosseum2k19.Model.Score;
import colosseum19.a300dpi.colosseum2k19.R;

public class ScoresFragment extends Fragment {


    private String TAG = ScoresFragment.class.getSimpleName();

    private RecyclerView gameNameList;
    private ScoreGameListAdapter gameListAdapter;
    private List<Score> scoreList = new ArrayList<>();
    private ArrayList<Score>scoreArrayList = new ArrayList<>();

    private Context context;
    private static ScoresFragment instance;

    public static ScoresFragment getInstance(){
        if(instance == null){
            instance = new ScoresFragment();
        }
        return instance;
    }

    public ScoresFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_scores,container,false);
        ButterKnife.bind(this,view);
        context = view.getContext();

        gameNameList = view.findViewById(R.id.score_game_name_list);
        gameListAdapter = new ScoreGameListAdapter(getActivity());
        gameNameList.setAdapter(gameListAdapter);
        gameNameList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //get list of  scores fora particular game
    //use callback to send the data back to recyclerview adaptar
    public  void getGameScores(String query, final CallbackInterface callbackInterface,final ScoreGameListAdapter.ScoreGameHolder scoreHolder){

        Log.d("TEST_QUERY",query);
        Query gameQuery = FirebaseFirestore.getInstance()
                .collection("scores")
                .whereEqualTo("game_name", query)
                .orderBy("timestamp");
        gameQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size() > 0){
                    scoreArrayList.clear();
                    Log.d(TAG,"TASK SUCCESSFULL");
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Score singleScore = doc.toObject(Score.class);
                        scoreArrayList.add(singleScore);
                    }
                    //QueryData.setData(fixtureArrayList);
                    callbackInterface.setScoreData(scoreArrayList,scoreHolder,false);
                }else{
                    callbackInterface.setScoreData(null,null,true);
                    Log.d("NULL","NULL");

                }
            }
        });
    }
}
