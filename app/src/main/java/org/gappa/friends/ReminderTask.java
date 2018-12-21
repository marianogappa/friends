package org.gappa.friends;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class ReminderTask {
    private static final int REMINDER_INTERVAL_SECONDS = 10;
    private static final int SYNC_FLEXTIME_SECONDS = 10;
    private static final String REMINDER_JOB_TAG = "reminder_tag";
    private static boolean sInitialized;
    private static int mJobId = 0;
    private static ComponentName mServiceComponent;

    synchronized public static void scheduleReminder(@NonNull final Context context) {
        if (sInitialized) {
            return;
        }
        sInitialized = true;

        mServiceComponent = new ComponentName(context, ReminderJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServiceComponent);
        builder.setMinimumLatency(1000); // wait at least
        builder.setOverrideDeadline(3 * 1000); // maximum delay
        JobScheduler tm = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int res = tm.schedule(builder.build());



//
//
//
//        Driver driver = new GooglePlayDriver(context);
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
//        Job constraintReminderJob = dispatcher.newJobBuilder()
//                .setService(ReminderFirebaseJobService.class)
//                .setTag(REMINDER_JOB_TAG)
//                .setLifetime(Lifetime.FOREVER)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,
//                        REMINDER_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS))
//                .setReplaceCurrent(true)
//                .build();
//        int res = dispatcher.schedule(constraintReminderJob);
        Toast toast = Toast.makeText(context.getApplicationContext(),"Result of scheduling: "+res
                , Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }


    public static final String ACTION_REMINDER = "reminder";
    private static final String FILENAME = "test.txt";

    public static void executeTask(Context context) {
        issueReminder(context);
    }

    private static void issueReminder(Context context) {
        int i = readValue(context);
        i+=1;
        writeValue(context, i);
//        TextView mainTextView = context.findViewById(R.id.main_text_view);
        NotificationUtils.notify(context, "Current value is "+i);

//        mainTextView.setText(""+i);

    }


    protected static int readValue(Context context) {
        try {
            FileInputStream file = context.openFileInput(FILENAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String line = br.readLine();
            return Integer.parseInt(line);
        } catch (Exception e) {
        }
        return 0;
    }


    protected static void writeValue(Context context, int i) {
        try {
            FileOutputStream outputStream;
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write((""+i).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
