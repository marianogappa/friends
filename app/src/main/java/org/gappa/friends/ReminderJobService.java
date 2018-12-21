package org.gappa.friends;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class ReminderJobService extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ReminderJobService.this;
                ReminderTask.executeTask(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(params, true);
            }
        };
        Toast toast = Toast.makeText(getApplicationContext(),"Now", Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
        Log.e("GAPPAA", "Now");
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
