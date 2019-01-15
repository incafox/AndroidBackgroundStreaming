package com.example.abraham.playerservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerService extends Service implements ExoPlayer.EventListener {

    String CHANNEL_ID = "com.app.app";

    private static Timer timer = new Timer();
    private Context ctx;

    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }
    private Handler mainHandler;
    private RenderersFactory renderersFactory;
    private BandwidthMeter bandwidthMeter;
    private LoadControl loadControl;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource mediaSource;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private final String streamUrl = "http://14003.live.streamtheworld.com/CRP_OAS_SC"; //bbc world service url
    private TrackSelector trackSelector;

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate()
    {
        super.onCreate();
        ctx = this;

        //funka





        /*
        Notification notification = new Notification(2, "ffa",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //notification.setLatestEventInfo(this, "mierda",
        //        "dddd", pendingIntent);
        startForeground(1337, notification);
*/


        startService();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Do your service work
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("New mail from " )
                .setContentText("acacsc")
                .setSmallIcon(R.drawable.exo_controls_pause)
                //.setLargeIcon(aBitmap)
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);

        return START_NOT_STICKY; //or return START_REDELIVER_INTENT;
    }

    /*
    public PlayerService()
    {
        super("PlayerService");
    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    /*
    @Override
    protected void onHandleIntent( Intent intent) {
        startService();
    }*/

    private void startService()
    {
        //timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
        renderersFactory = new DefaultRenderersFactory(getApplicationContext());
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        player.addListener(this);

        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), "ExoplayerDemo");
        extractorsFactory = new DefaultExtractorsFactory();
        mainHandler = new Handler();
        mediaSource = new ExtractorMediaSource(Uri.parse(streamUrl),
                dataSourceFactory,
                extractorsFactory,
                mainHandler,
                null);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        Log.v("servicio lanzado","tmr");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    /*
    public void onDestroy()
    {
        //super.onDestroy();
        Toast.makeText(this, "servicio detenido ...", Toast.LENGTH_SHORT).show();
    }
    */

}
