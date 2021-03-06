package com.example.mario.testalgorithm;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static int UNASSIGNEDTASKPENALTY =3;

    private TextView getPlanInfo;
    private String showData="";
    private ArrayList<TechnicianInfo> chosenTechs;
    private ArrayList<Task> chosentasks;
    private ArrayList<Task> sortedTask;
    private ArrayList<TechnicianInfo> sortedTech;
    private LatLng startEnd=new LatLng(37.780148,-122.403158);
    //private LatLng startEnd=new LatLng(37.350044,-121.900939);
    private Double initialCost;
    private Double improveCost=10000000.0;
    private Double minimumCost=10000.0;

    private Button hillClimbing;
    private Button guidedLocalSearch;
    private int GLSiteration;
    private int findNeighborIteration;
    private long startTimeGLS;

    private Map<Task,TechnicianInfo> initialResult;
    private Map<Task,TechnicianInfo> improveResult;
    private Map<Task,TechnicianInfo> GLSresult;
    private Map<Task,TechnicianInfo> localOptima;

    private ArrayList<Integer> penalty;

    private Double minMinimumCost=5000000.0;
    private Double maxMinimumCost=0.0;
    private Double totalMinimumCost=0.0;


    private Button iteration10;
    private Button iteration30;
    private Button iteration100;
    private Button iteration300;

    private double lambda=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        assignData();

        GLSiteration=400;

        findNeighborIteration=50;


        sortTaskBySkill();
        sortTechnicianBySkill();

        basicSchedule();
        initialCost=calculateCost(initialResult);
        //calculate the cost of initial schedule (before hill climbing)
        //improveCost=calculateCost(improveResult);

        hillClimbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hillClimbing(initialResult);
                showScheduleInfo();
            }
        });

        guidedLocalSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    CostTimeTask costTimeTask=new CostTimeTask(MainActivity.this);
                    costTimeTask.execute();


                Log.d("max: ",maxMinimumCost+"");
                Log.d("min: ",minMinimumCost+"");
                Log.d("Average: ",totalMinimumCost/10+"");

            }
        });

        iteration10.setOnClickListener(this);
        iteration30.setOnClickListener(this);
        iteration100.setOnClickListener(this);
        iteration300.setOnClickListener(this);
        //showScheduleInfo();

    }

    private void assignData(){
        TechnicianInfo tech1=new TechnicianInfo(1,800,"tech1");
        TechnicianInfo tech2=new TechnicianInfo(2,800,"tech2");
        TechnicianInfo tech3=new TechnicianInfo(3,800,"tech3");
        TechnicianInfo tech4=new TechnicianInfo(1,800,"tech4");
        TechnicianInfo tech5=new TechnicianInfo(2,800,"tech5");
        TechnicianInfo tech6=new TechnicianInfo(3,800,"tech6");
        TechnicianInfo tech7=new TechnicianInfo(3,800,"tech7");


        chosenTechs.add(tech1);
        chosenTechs.add(tech2);
        chosenTechs.add(tech3);
        chosenTechs.add(tech4);
        chosenTechs.add(tech5);
        chosenTechs.add(tech6);
        chosenTechs.add(tech7);


        Task t1=new Task("task1",1,60,Station.STATION_8);
        Task t2=new Task("task2",1,60,Station.STATION_59);
        Task t3=new Task("task3",1,60,Station.STATION_60);
        Task t4=new Task("task4",1,60,Station.STATION_7);
        Task t5=new Task("task5",1,60,Station.STATION_64);
        Task t6=new Task("task6",1,60,Station.STATION_4);
        Task t7=new Task("task7",2,90,Station.STATION_14);
        Task t8=new Task("task8",2,90,Station.STATION_10);
        Task t9=new Task("task9",2,90,Station.STATION_9);
        Task t10=new Task("task10",2,90,Station.STATION_12);
        Task t11=new Task("task11",2,90,Station.STATION_1);
        Task t12=new Task("task12",2,90,Station.STATION_2);
        Task t13=new Task("task13",2,90,Station.STATION_3);
        Task t14=new Task("task14",2,90,Station.STATION_4);
        Task t15=new Task("task15",3,120,Station.STATION_5);
        Task t16=new Task("task16",3,120,Station.STATION_6);
        Task t17=new Task("task17",3,120,Station.STATION_67);
        Task t18=new Task("task18",3,120,Station.STATION_58);
        Task t19=new Task("task19",3,120,Station.STATION_13);
        Task t20=new Task("task20",3,120,Station.STATION_65);

//        Task t1=new Task("task1",1,60,Station.STATION_40);
//        Task t2=new Task("task2",1,60,Station.STATION_44);
//        Task t3=new Task("task3",1,60,Station.STATION_36);
//        Task t4=new Task("task4",1,60,Station.STATION_29);
//        Task t5=new Task("task5",1,60,Station.STATION_25);
//        Task t6=new Task("task6",1,60,Station.STATION_49);
//        Task t7=new Task("task7",2,90,Station.STATION_20);
//        Task t8=new Task("task8",2,90,Station.STATION_53);
//        Task t9=new Task("task9",2,90,Station.STATION_48);
//        Task t10=new Task("task10",2,90,Station.STATION_22);
//        Task t11=new Task("task11",2,90,Station.STATION_55);
//        Task t12=new Task("task12",2,90,Station.STATION_47);
//        Task t13=new Task("task13",2,90,Station.STATION_33);
//        Task t14=new Task("task14",2,90,Station.STATION_42);
//        Task t15=new Task("task15",3,120,Station.STATION_41);
//        Task t16=new Task("task16",3,120,Station.STATION_43);
//        Task t17=new Task("task17",3,120,Station.STATION_38);
//        Task t18=new Task("task18",3,120,Station.STATION_37);
//        Task t19=new Task("task19",3,120,Station.STATION_40);
//        Task t20=new Task("task20",3,120,Station.STATION_54);


        chosentasks.add(t1);
        chosentasks.add(t2);
        chosentasks.add(t3);
        chosentasks.add(t4);
        chosentasks.add(t5);
        chosentasks.add(t6);
        chosentasks.add(t7);
        chosentasks.add(t8);
        chosentasks.add(t9);
        chosentasks.add(t10);
        chosentasks.add(t11);
        chosentasks.add(t12);
        chosentasks.add(t13);
        chosentasks.add(t14);
        chosentasks.add(t15);
        chosentasks.add(t16);
        chosentasks.add(t17);
        chosentasks.add(t18);
        chosentasks.add(t19);
        chosentasks.add(t20);



    }


    private void initialize() {
        chosenTechs=new ArrayList<>();
        chosentasks=new ArrayList<>();
        getPlanInfo=(TextView)findViewById(R.id.getPlanInfo);
        initialResult=new HashMap<>();
        improveResult=new HashMap<>();
        localOptima=new HashMap<>();
        sortedTask=new ArrayList<>();
        sortedTech=new ArrayList<>();
        GLSresult=new HashMap<>();
        hillClimbing=(Button)findViewById(R.id.hillClimbing);
        guidedLocalSearch=(Button)findViewById(R.id.guidedLoacalSearch);
        penalty=new ArrayList<Integer>();
        penalty.add(0,0);
        penalty.add(1,0);
        penalty.add(2,0);

        iteration10=(Button)findViewById(R.id.iteration10);
        iteration30=(Button)findViewById(R.id.iteration30);
        iteration100=(Button)findViewById(R.id.iteration100);
        iteration300=(Button)findViewById(R.id.iteration300);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iteration10:
                GLSiteration=400; //3 s 内
                break;
            case R.id.iteration30:
                GLSiteration=1600;// 10s
                break;
            case R.id.iteration100:
                GLSiteration=20000; //1 min
                break;
            case R.id.iteration300:
                GLSiteration=50000; //3 min
                break;
        }
    }

    private class CostTimeTask extends AsyncTask<String,Integer,String> {
        private ProgressDialog dialog;


        public CostTimeTask(Context context){
            dialog=new ProgressDialog(context,0);

            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            startTimeGLS=System.currentTimeMillis();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("initial cost: ",initialCost+"");
            maxMinimumCost=0.0;
            minMinimumCost=5000000.0;
            totalMinimumCost=0.0;
            for(int i=0;i<10;i++) {
                penalty.clear();
                penalty.add(0, 0);
                penalty.add(1, 0);
                penalty.add(2, 0);
                minimumCost = 500000.0;
                GLSresult = new HashMap<>();
                localOptima = new HashMap<>();
                guidedLS();
                Log.d("cost:",minimumCost.shortValue()+"");
                if(minimumCost>=maxMinimumCost){
                    maxMinimumCost=minimumCost;
                }
                if(minimumCost<=minMinimumCost){
                    minMinimumCost=minimumCost;
                }
                totalMinimumCost+=minimumCost;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            getPlanInfo.setText("");
            //showScheduleInfo();
            dialog.dismiss();
            long endTime=System.currentTimeMillis();
            Log.d("Time","Running time:"+(endTime-startTimeGLS)+"ms");
            Log.d("max: ",maxMinimumCost+"");
            Log.d("min: ",minMinimumCost+"");
            Log.d("average: ",totalMinimumCost/10+"");
            getPlanInfo.setText("max: "+maxMinimumCost+",min: "+minMinimumCost+",average: "+totalMinimumCost/10);
        }
    }


    private void guidedLS(){
        int i=0;
        Map<Task,TechnicianInfo> initialSchedule=new HashMap<>();
        initialSchedule.putAll(initialResult);

        while (i<GLSiteration){
            modifiedHillClimbing(initialSchedule);
            ArrayList<Double> localOptimaCost=modifiedCost(localOptima);

            double util1=localOptimaCost.get(2)/(1+penalty.get(0));
            double util2=localOptimaCost.get(3)/(1+penalty.get(1));
           // double util3=localOptimaCost.get(3)/(1+penalty.get(2));
//            Log.d("util:",util1+","+util2+","+util3);
//            Log.d("penalty:",penalty.get(0)+","+penalty.get(1)+","+penalty.get(2));

//            if(util1<=util2){
//                if(util2<=util3){
//                    penalty.set(2,penalty.get(2)+1);
//                }else {
//                    penalty.set(1,penalty.get(1)+1);
//                }
//            }else if(util1<=util3){
//                penalty.set(2,penalty.get(2)+1);
//            }else {
//                penalty.set(0,penalty.get(0)+1);
//            }

            if(util1<=util2){
                penalty.set(1,penalty.get(1)+1);
            }else {
                penalty.set(0,penalty.get(0)+1);
            }



            if(calculateCost(localOptima)<minimumCost){
                minimumCost=calculateCost(localOptima);
                GLSresult=localOptima;
                Log.d("Alert:","Find minimum:"+minimumCost);
            }

            i++;
        }
    }



    /**
     * 需要异步
     * @param schedule
     */
    private void modifiedHillClimbing(Map<Task,TechnicianInfo> schedule) {
        ArrayList<Double> originCostResult=modifiedCost(schedule);
        Map<Task,TechnicianInfo> newNeighbor=new HashMap<>();

        boolean find=false;

        //Log.d("schedule length: ",schedule.size()+"");
        newNeighbor.clear();
        newNeighbor=swapToFindNeighbor(schedule);

        if(newNeighbor!=null){
            ArrayList<Double> newCostResult=modifiedCost(newNeighbor);
            if(newCostResult.get(0)<originCostResult.get(0)){
                modifiedHillClimbing(newNeighbor);
                find=true;
            }
        }

        if(!find){
            newNeighbor=assignToFindNeighbor(schedule);
            if(newNeighbor!=null) {
                ArrayList<Double> newCostResult = modifiedCost(newNeighbor);
                if (newCostResult.get(0) < originCostResult.get(0)) {
                    modifiedHillClimbing(newNeighbor);
                    find=true;
                }
            }
        }

        if(!find){
            if (schedule.size()<sortedTask.size()){
                newNeighbor=addUnassignedToFindNeighbor(schedule);
                if(newNeighbor!=null){
                    ArrayList<Double> newCostResult = modifiedCost(newNeighbor);
                   // Log.d("Test",newCostResult.get(0)+","+originCostResult.get(0));
                    if(newCostResult.get(0) < originCostResult.get(0)){
                        find=true;
                        //Log.d("Test","come");
                        modifiedHillClimbing(newNeighbor);
                    }
                }
            }
        }


        if(!find){
            //Log.d("Alert: ","find local optima");
            localOptima=schedule;
        }


    }


    /**
     * 找两个random的数，互换位置上的
     */
    private void hillClimbing(Map<Task,TechnicianInfo> schedule) {
        double originCost=calculateCost(schedule);
        Map<Task,TechnicianInfo> newNeighbor=new HashMap<>();

        boolean find=false;


        newNeighbor=swapToFindNeighbor(schedule);
        if(newNeighbor!=null){
            if(calculateCost(newNeighbor)<originCost){
                find=true;
                hillClimbing(newNeighbor);
            }
        }


        if(!find){
            newNeighbor=assignToFindNeighbor(schedule);
            if(newNeighbor!=null){
                if(calculateCost(newNeighbor)<originCost){
                    find=true;
                    hillClimbing(newNeighbor);
                }
            }
        }

        if(!find){
          //  Log.d("Test",sortedTask.size()+","+schedule.size());
            if (schedule.size()<sortedTask.size()){
            //    Log.d("Test","Enter");
                newNeighbor=addUnassignedToFindNeighbor(schedule);
                if(newNeighbor!=null){
                    if(calculateCost(newNeighbor)<originCost){
                        find=true;
                        hillClimbing(newNeighbor);
                    }
                }
            }
        }

        if(!find){
            improveResult=schedule;
        }
    }


    private Map<Task,TechnicianInfo> swapToFindNeighbor(Map<Task,TechnicianInfo> originSchedule){
        Map<Task,TechnicianInfo> neighbor=new HashMap<>();

        ArrayList<Task> tasks=new ArrayList<>();
        tasks.addAll(originSchedule.keySet());
        ArrayList<TechnicianInfo> techs=new ArrayList<>();
        techs.addAll(originSchedule.values());

        Boolean find=false;


        for(int k=0;k<findNeighborIteration;k++){

            int i= (int) Math.round(Math.random()*(originSchedule.size()-1));
            int j= (int) Math.round(Math.random()*(originSchedule.size()-1));

            if(i!=j&&techs.get(i)!=techs.get(j)&&tasks.get(i).getSkillRequirement()<=techs.get(j).getSkillLevel()&&tasks.get(j).getSkillRequirement()<=techs.get(i).getSkillLevel()){
                Map<Task,TechnicianInfo> newResult=new HashMap<>();
                newResult.putAll(originSchedule);
                newResult.put(tasks.get(i),techs.get(j));
                newResult.put(tasks.get(j),techs.get(i));
                if(calculateWorkHour(newResult,techs.get(j))<=techs.get(j).getWorkHour()&&calculateWorkHour(newResult,techs.get(i))<=techs.get(i).getWorkHour()){
                    neighbor.putAll(newResult);
                    find=true;
                    break;
                }
            }
        }

        if(find){
            return neighbor;
        }else {
            return  null;
        }
    }

    private Map<Task,TechnicianInfo> assignToFindNeighbor(Map<Task,TechnicianInfo> originSchedule){
        Map<Task,TechnicianInfo> neighbor=new HashMap<>();
        ArrayList<Task> tasks=new ArrayList<>();
        tasks.addAll(originSchedule.keySet());
        ArrayList<TechnicianInfo> techs=new ArrayList<>();
        techs.addAll(sortedTech);

        Boolean find=false;

        for(int k=0;k<findNeighborIteration;k++){
            int i= (int) Math.round(Math.random()*(originSchedule.size()-1));
            int j= (int) Math.round(Math.random()*(techs.size()-1));

            if(i!=j&&originSchedule.get(i)!=techs.get(j)&&tasks.get(i).getSkillRequirement()<=techs.get(j).getSkillLevel()){
                Map<Task,TechnicianInfo> newResult=new HashMap<>();
                newResult.putAll(originSchedule);
                newResult.put(tasks.get(i),techs.get(j));
                if(calculateWorkHour(newResult,techs.get(j))<=techs.get(j).getWorkHour()){
                    neighbor.putAll(newResult);
                    find=true;
                    break;
                }
            }
        }

        if(find){
            return neighbor;
        }else {
            return  null;
        }
    }


    private Map<Task,TechnicianInfo> addUnassignedToFindNeighbor(Map<Task,TechnicianInfo> originSchedule){
        Map<Task,TechnicianInfo> neighbor=new HashMap<>();
        ArrayList<Task> tasks=new ArrayList<>();
        tasks.addAll(originSchedule.keySet());
        ArrayList<TechnicianInfo> techs=new ArrayList<>();
        techs.addAll(sortedTech);

        ArrayList<Task> unassignedTask=new ArrayList<>();
        unassignedTask.addAll(sortedTask);

        for(int i=0;i<tasks.size();i++){
            unassignedTask.remove(tasks.get(i));
        }
        Boolean find=false;

        for(int k=0;k<findNeighborIteration;k++){
            int i= (int) Math.round(Math.random()*(unassignedTask.size()-1));
            int j= (int) Math.round(Math.random()*(techs.size()-1));

            if(unassignedTask.get(i).getSkillRequirement()<=techs.get(j).getSkillLevel()){
                Map<Task,TechnicianInfo> newResult=new HashMap<>();
                newResult.putAll(originSchedule);
                newResult.put(unassignedTask.get(i),techs.get(j));
                if(calculateWorkHour(newResult,techs.get(j))<=techs.get(j).getWorkHour()){
                    neighbor.putAll(newResult);
                  //  Log.d("Test","Found");
                    find=true;
                    break;
                }
            }
        }


        if(find){
            return neighbor;
        }else {
            return null;
        }
    }





    /**
     * Calculate the cost after the schedule.(hill climbing)
     */
    private Double calculateCost(Map<Task,TechnicianInfo> schedule) {
        ArrayList<Task> availableTask= new ArrayList<>();
        ArrayList<Task> unassignedTask=new ArrayList<>();
        unassignedTask.addAll(sortedTask);
        availableTask.addAll(schedule.keySet());
        //double maxCost=0;
        double cost=0;


        for(int j=0;j<sortedTech.size();j++) {
            ArrayList<Task> assignedTask = new ArrayList<>();
            if (schedule.containsValue(sortedTech.get(j))) {  //There already exist some task assigned to the technician j.
                for (int k = 0; k < schedule.size(); k++) {
                    if (schedule.get(availableTask.get(k)) == sortedTech.get(j)) {
                        cost += calculateDur(availableTask.get(k).getSkillRequirement(), sortedTech.get(j).getSkillLevel(),availableTask.get(k).getDuration());    //calculate estimate time which is the sum of all the assigned task for this specific technician.
                        assignedTask.add(availableTask.get(k));
                        unassignedTask.remove(availableTask.get(k));
                    }
                }

                if(assignedTask.size()>=1){
                    float dist = calculateTravelTime(assignedTask);
                    cost += (dist / 1000) * 10;  //assume drive speed is 6km/h.
                }

            }

//            if(cost>maxCost){
//                maxCost=cost;
//            }
        }

        if(!unassignedTask.isEmpty()){
            for(int i=0;i<unassignedTask.size();i++){
                cost+=UNASSIGNEDTASKPENALTY*unassignedTask.get(i).getDuration();
            }

            //Log.d("unassigned Task:",unassignedTask.size()+"");
        }
        return cost;
    }


    private ArrayList<Double> modifiedCost(Map<Task,TechnicianInfo> schedule){
        ArrayList<Task> availableTask= new ArrayList<>();
        ArrayList<Task> unassignedTask=new ArrayList<>();
        unassignedTask.addAll(sortedTask);
        availableTask.addAll(schedule.keySet());
        //double maxCost=0;
        ArrayList<Double> result=new ArrayList<>();
        double cost=0;
        double taskCost=0;
        double travelCost=0;
        double unassignedTaskCost=0;


        for(int j=0;j<sortedTech.size();j++) {
            ArrayList<Task> assignedTask = new ArrayList<>();
            if (schedule.containsValue(sortedTech.get(j))) {  //There already exist some task assigned to the technician j.
                for (int k = 0; k < schedule.size(); k++) {
                    if (schedule.get(availableTask.get(k)) == sortedTech.get(j)) {
                        cost += calculateDur(availableTask.get(k).getSkillRequirement(), sortedTech.get(j).getSkillLevel(),availableTask.get(k).getDuration());    //calculate estimate time which is the sum of all the assigned task for this specific technician.
                        taskCost += calculateDur(availableTask.get(k).getSkillRequirement(), sortedTech.get(j).getSkillLevel(),availableTask.get(k).getDuration());;
                        assignedTask.add(availableTask.get(k));
                        unassignedTask.remove(availableTask.get(k));
                    }
                }


                if(assignedTask.size()>=1){
                    float dist = calculateTravelTime(assignedTask);
                    cost += (dist / 1000) * 10;  //assume drive speed is 6km/h.
                    travelCost+=(dist / 1000) * 10;
                }
            }

        }

        if(!unassignedTask.isEmpty()){
            for(int i=0;i<unassignedTask.size();i++){
                cost+=UNASSIGNEDTASKPENALTY*unassignedTask.get(i).getDuration();
                unassignedTaskCost+=UNASSIGNEDTASKPENALTY*unassignedTask.get(i).getDuration();
            }
        }


        //cost+=(taskCost*penalty.get(0)+travelCost*penalty.get(1)+unassignedTaskCost*penalty.get(2))*lambda;
        cost+=(travelCost*penalty.get(0)+unassignedTaskCost*penalty.get(1))*lambda;

        result.add(0,cost);
        result.add(1,taskCost);
        result.add(2,travelCost);
        result.add(3,unassignedTaskCost);

        return result;

    }






    /**
     * Test
     */
    private void showScheduleInfo() {
        showData="";
        showData+="number of tasks:"+sortedTask.size()+", number of technicians:"+sortedTech.size()+"\n\n";

        if(improveResult.isEmpty()&&GLSresult.isEmpty()){
            ArrayList<Task> availableTask= new ArrayList<>();
            availableTask.addAll(initialResult.keySet());

            for(int i=0;i<availableTask.size();i++){
                showData+=availableTask.get(i).getName()+",skill:"+availableTask.get(i).getSkillRequirement()+",duration:"+availableTask.get(i).getDuration()+": "+initialResult.get(availableTask.get(i)).getFirstName()+", tech skill:"+initialResult.get(availableTask.get(i)).getSkillLevel()+", work hour:"+initialResult.get(availableTask.get(i)).getWorkHour()+"\n";
            }

            showData+="cost:"+initialCost.shortValue()+"\n";
        }


        if(!improveResult.isEmpty()){
            ArrayList<Task> improved=new ArrayList<>();
            improved.addAll(improveResult.keySet());
            for(int j=0;j<improved.size();j++){
                showData+=improved.get(j).getName()+",skill:"+improved.get(j).getSkillRequirement()+",duration:"+improved.get(j).getDuration()+": "+improveResult.get(improved.get(j)).getFirstName()+", tech skill:"+improveResult.get(improved.get(j)).getSkillLevel()+", work hour:"+improveResult.get(improved.get(j)).getWorkHour()+"\n";
            }
            improveCost=calculateCost(improveResult);
            showData+="cost:"+improveCost.shortValue()+"\n";
        }

        if(!GLSresult.isEmpty()){
            ArrayList<Task> improved=new ArrayList<>();
            improved.addAll(GLSresult.keySet());
            for(int j=0;j<improved.size();j++){
                showData+=improved.get(j).getName()+",skill:"+improved.get(j).getSkillRequirement()+",duration:"+improved.get(j).getDuration()+": "+GLSresult.get(improved.get(j)).getFirstName()+", tech skill:"+GLSresult.get(improved.get(j)).getSkillLevel()+", work hour:"+GLSresult.get(improved.get(j)).getWorkHour()+"\n";
            }
            Double finalCost=calculateCost(GLSresult);
            showData+="cost: "+minimumCost.shortValue()+"\n";
            Log.d("cost:",minimumCost.shortValue()+"");
        }




        getPlanInfo.setText(showData);

    }


    private void basicSchedule(){
        sortTaskBySkill();
        sortTechnicianBySkill();

        for(int i=0;i<sortedTask.size();i++){
            for(int j=0;j<sortedTech.size();j++){
                double estimateDur=0;
                ArrayList<Task> assignedTask=new ArrayList<>();

                if(sortedTask.get(i).getSkillRequirement()<=sortedTech.get(j).getSkillLevel()){     //if the estimate working duration < working hour,then assign this task to the technician.

                    if(initialResult.containsValue(sortedTech.get(j))){  //There already exist some task assigned to the technician j.
                        for(int k=0;k<i;k++){
                            if(initialResult.get(sortedTask.get(k))==sortedTech.get(j)){
                                estimateDur+=calculateDur(sortedTask.get(k).getSkillRequirement(),sortedTech.get(j).getSkillLevel(),sortedTask.get(k).getDuration());    //calculate estimate time which is the sum of all the assigned task for this specific technician.
                                assignedTask.add(sortedTask.get(k));
                            }
                        }
                        //Log.d("estimate pre:",estimateDur+"");
                        estimateDur+=calculateDur(sortedTask.get(i).getSkillRequirement(),sortedTech.get(j).getSkillLevel(),sortedTask.get(i).getDuration());           //Add the new task being schedules.
                        assignedTask.add(sortedTask.get(i));

                        float dist=calculateTravelTime(assignedTask);
                        estimateDur+=(dist/1000)*2;  //assume drive speed is 30km/h.
                        //Log.d("estimateDurAlready:"+j,estimateDur+" ");
                    }else {
                        estimateDur+=calculateDur(sortedTask.get(i).getSkillRequirement(),sortedTech.get(j).getSkillLevel(),sortedTask.get(i).getDuration());           //Add the new task being schedules.
                        assignedTask.add(sortedTask.get(i));

                        float dist=calculateTravelTime(assignedTask);
                        estimateDur+=(dist/1000)*2;  //assume drive speed is 30km/h.
                        //Log.d("estimateDur:"+j,estimateDur+" ");
                    }


                    if(estimateDur<sortedTech.get(j).getWorkHour()){
                        initialResult.put(sortedTask.get(i),sortedTech.get(j));
                        break;
                    }
                }
            }
        }
    }


    private float calculateWorkHour(Map<Task,TechnicianInfo> schedule,TechnicianInfo tech){
        ArrayList<Task> tasks=new ArrayList<>();
        ArrayList<Task> scheduledTask=new ArrayList<>();
        scheduledTask.addAll(schedule.keySet());
        for(int i=0;i<schedule.size();i++){
            if(schedule.get(scheduledTask.get(i))==tech){
                tasks.add(scheduledTask.get(i));
            }
        }
        float cost=0;


        for(int i=0;i<tasks.size();i++){
            cost+=calculateDur(tasks.get(i).getSkillRequirement(),tech.getSkillLevel(),tasks.get(i).getDuration());
        }
        float dist=calculateTravelTime(tasks);
        cost+=(dist/1000)*2;



        return cost;

    }

    private float calculateTravelTime(ArrayList<Task> travelTasks) {

        float total=0;
        for(int i=0;i<travelTasks.size()-1;i++){
            float[] distBetweenTwoNodes=new float[1];
            Location.distanceBetween(travelTasks.get(i).getPosition().latitude,travelTasks.get(i).getPosition().longitude
                    ,travelTasks.get(i+1).getPosition().latitude,travelTasks.get(i+1).getPosition().longitude,distBetweenTwoNodes);
            total+=distBetweenTwoNodes[0];
        }
        float[] baseToStart=new float[1];
        float[] endToBase=new float[1];
        if(travelTasks.size()!=0){
            Location.distanceBetween(startEnd.latitude,startEnd.longitude,travelTasks.get(0).getPosition().latitude,travelTasks.get(0).getPosition().longitude,baseToStart);
            Location.distanceBetween(startEnd.latitude,startEnd.longitude,travelTasks.get(travelTasks.size()-1).getPosition().latitude,travelTasks.get(travelTasks.size()-1).getPosition().longitude,endToBase);
            total=total+baseToStart[0]+endToBase[0];
        }


        //Log.d("travel time:",(total/1000)*10+"");

        return total;
    }


    private double calculateDur(int taskSkillReq,int techSkillLevel,int taskDuration ){
        double result=(double) taskSkillReq/(double) techSkillLevel;

        return result*taskDuration;
    }

    private void sortTaskBySkill() {
        int minSkillRequire=100;
        int minCount=0;
        if(chosentasks.size()>0){
            for(int i=0;i<chosentasks.size();i++){
                int skillRequire=chosentasks.get(i).getSkillRequirement();
                if(skillRequire<=minSkillRequire){
                    minSkillRequire=skillRequire;
                    minCount=i;
                }
            }
            Task t=chosentasks.get(minCount);
            sortedTask.add(t);
            chosentasks.remove(minCount);
            sortTaskBySkill();
        }
    }

    private void sortTechnicianBySkill(){
        int minSkillLevel=100;
        int minCount=0;
        if(chosenTechs.size()>0){
            for(int i=0;i<chosenTechs.size();i++){
                int skillLevel=chosenTechs.get(i).getSkillLevel();
                if(skillLevel<=minSkillLevel){
                    minSkillLevel=skillLevel;
                    minCount=i;
                }
            }
            TechnicianInfo t=chosenTechs.get(minCount);
            sortedTech.add(t);
            chosenTechs.remove(minCount);
            sortTechnicianBySkill();
        }
    }





}
