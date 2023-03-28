package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private String nameAudio = "";
    private final String DATA_STREAM = "https://mp3melodii.ru/files_site_02/001/veselaya_melodiya_iz_peredachi_kalambur_derevnya_durakov.mp3";

    private MediaPlayer mediaPlayer;
    private Toast toast;

    private TextView textOut;
    private Switch switchLoop;

    private AudioManager audioManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOut = findViewById(R.id.textOut);
        switchLoop = findViewById(R.id.switchLoop);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(isChecked);
                }

            }
        });

    }

    public void onClickSource(View view) throws IOException  {

        releaseMediaPlayer();

        try {
            switch (view.getId()) {
                case R.id.btnStream :
                    toast = Toast.makeText(this, "Запущен поток аудио", Toast.LENGTH_SHORT);
                    toast.show();

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();
                    nameAudio = "Мелодия из телесериала";


                    break;
                case R.id.btnRAW :
                    toast = Toast.makeText(this, "Запущен аудио-файл с памяти телефона", Toast.LENGTH_SHORT);
                    toast.show();

                    mediaPlayer = MediaPlayer.create(this, R.raw.calm_melody);
                    mediaPlayer.start();

                    nameAudio = "Спокойная мелодия";

                    break;

            }
        } catch (IOException e) {
            e.printStackTrace();
            toast = Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT); // инициализация
            toast.show(); // демонстрация тоста на экране
        }

        if (mediaPlayer == null) return;

        mediaPlayer.setLooping(switchLoop.isChecked()); // включение / выключение повтора
        mediaPlayer.setOnCompletionListener(this); // слушатель окончания проигрывания
    }

    // слушатель управления воспроизведением контента
    public void onClick(View view) {

        if (mediaPlayer == null) return;

        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start(); // метод возобновления проигрывания
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // метод паузы
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop(); // метод остановки
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000); // переход к определённой позиции трека
                // mediaPlayer.getCurrentPosition() - метод получения текущей позиции
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000); // переход к определённой позиции трека
                break;
        }
        // информативный вывод информации
        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }



    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        toast = Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT); // инициализация тоста
        toast.show();

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toast = Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT); // инициализация тоста
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}